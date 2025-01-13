package icu.samnyan.aqua.sega.maimai2

import ext.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketTimeoutException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.set
import kotlin.concurrent.withLock


const val PROTO_VERSION = 1
const val MAX_STREAMS = 10
const val SO_TIMEOUT = 10000

private object Command {
    // Control plane
    const val CTL_START = 1
    const val CTL_BIND = 2
    const val CTL_HEARTBEAT = 3
    const val CTL_TCP_CONNECT = 4  // Accept a new multiplexed TCP stream
    const val CTL_TCP_ACCEPT = 5
    const val CTL_TCP_ACCEPT_ACK = 6
    const val CTL_TCP_CLOSE = 7

    // Data plane
    const val DATA_SEND = 21
    const val DATA_BROADCAST = 22
}

private object Proto {
    const val TCP = 6
    const val UDP = 17
}

data class Message(
    val cmd: Int,
    val proto: Int? = null, // Protocol, TCP or UDP
    val sid: Int? = null, // Stream ID, only applicable in TCP streams, 0 in UDP

    // Src and dst should be simulated ipv4 addresses and 0-65535 ports
    val src: UInt? = null,
    val sPort: Int? = null,
    val dst: UInt? = null,
    val dPort: Int? = null,

    val data: Any? = null
)
fun ctlMsg(cmd: Int, data: Any? = null) = Message(cmd, data = data)

data class ActiveClient(
    val clientKey: String,
    val socket: Socket,
    val reader: BufferedReader,
    val writer: BufferedWriter,
    // <Stream ID, Destination client stub IP>
    val tcpStreams: MutableMap<Int, UInt> = mutableMapOf(),
    val pendingStreams: MutableSet<Int> = mutableSetOf(),
) {
    val log = logger()
    val stubIp = keychipToStubIp(clientKey)
    val writeMutex = ReentrantLock()

    var lastHeartbeat = millis()

    fun send(msg: Message) {
        writeMutex.withLock {
            writer.write(msg.toJson())
            writer.newLine()
            writer.flush()
        }
    }
}

fun ActiveClient.handle(msg: Message) {
    // Find target by dst IP address or TCP stream ID
    val target = (msg.dst ?: msg.sid?.let { tcpStreams[it] } )?.let { clients[it] }

    when (msg.cmd) {
        Command.CTL_HEARTBEAT -> lastHeartbeat = millis()
        Command.DATA_BROADCAST -> {
            // Broadcast to all clients. This is only used in UDP so SID is always 0
            if (msg.proto != Proto.UDP) return log.warn("TCP Broadcast received, something is wrong.")
            clients.values.filter { it.clientKey != clientKey }.forEach { it.send(msg.copy(src = stubIp)) }
        }
        Command.DATA_SEND, Command.CTL_TCP_ACCEPT_ACK -> {
            target ?: return log.warn("Send: Target not found: ${msg.dst}")

            if (msg.proto == Proto.TCP && msg.sid !in tcpStreams)
                return log.warn("Stream ID not found: ${msg.sid}")

            target.send(msg.copy(src = stubIp, dst = target.stubIp))

            // 1020844165
            // 2245580860
        }
        Command.CTL_TCP_CONNECT -> {
            target ?: return log.warn("Connect: Target not found: ${msg.dst}")
            msg.sid ?: return log.warn("Connect: Stream ID not found")

            if (msg.sid in tcpStreams || msg.sid in pendingStreams)
                return log.warn("Stream ID already in use: ${msg.sid}")

            // Add the stream to the pending list
            pendingStreams.add(msg.sid)
            if (pendingStreams.size > MAX_STREAMS) {
                log.warn("Too many pending streams, closing connection")
                return socket.close()
            }

            target.send(msg.copy(src = stubIp, dst = target.stubIp))
        }
        Command.CTL_TCP_ACCEPT -> {
            target ?: return log.warn("Accept: Target not found: ${msg.dst}")
            msg.sid ?: return log.warn("Accept: Stream ID not found")

            if (msg.sid !in target.pendingStreams)
                return log.warn("Stream ID not found in pending list: ${msg.sid}")

            // Add the stream to the active list
            target.pendingStreams.remove(msg.sid)
            target.tcpStreams[msg.sid] = stubIp
            tcpStreams[msg.sid] = target.stubIp

            target.send(msg.copy(src = stubIp, dst = target.stubIp))
        }
    }
}

fun keychipToStubIp(keychip: String) = "1${keychip.substring(2)}".toUInt()

// Keychip ID to Socket
val clients = ConcurrentHashMap<UInt, ActiveClient>()

/**
 * Service for the party linker for AquaMai
 */
class MaimaiFutari(private val port: Int = 20101) {
    val log = logger()

    fun start() {
        val serverSocket = ServerSocket(port)
        log.info("Server started on port $port")

        while (true) {
            val clientSocket = serverSocket.accept().apply {
                soTimeout = SO_TIMEOUT
                log.info("[+] Client connected: $remoteSocketAddress")
            }
            thread { handleClient(clientSocket) }
        }
    }

    fun handleClient(socket: Socket) {
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
        var handler: ActiveClient? = null

        try {
            while (true) {
                val input = (reader.readLine() ?: continue).trim('\uFEFF')
                log.debug("Received: $input")
                val message = input.json<Message>()

                when (message.cmd) {
                    // Start: Register the client. Payload is the keychip
                    Command.CTL_START -> {
                        val id = message.data as String
                        val client = ActiveClient(id, socket, reader, writer)
                        clients[client.stubIp]?.socket?.close()
                        clients[client.stubIp] = client
                        handler = clients[client.stubIp]
                        log.info("[+] Client registered: ${socket.remoteSocketAddress} -> $id")

                        // Send back the version
                        handler?.send(ctlMsg(Command.CTL_START, mapOf("version" to PROTO_VERSION)))
                    }

                    // Handle any other command using the handler
                    else -> {
                        (handler ?: throw Exception("Client not registered")).handle(message)
                    }
                }
            }
        } catch (e: Exception) {
            if (e.message != "Connection reset" && e !is SocketTimeoutException)
                log.error("Error in client handler", e)
        } finally {
            // Remove client
            handler?.stubIp?.let { clients.remove(it) }
            socket.close()
            log.info("[-] Client disconnected: ${handler?.clientKey}")
        }
    }
}

fun main() = MaimaiFutari().start()
