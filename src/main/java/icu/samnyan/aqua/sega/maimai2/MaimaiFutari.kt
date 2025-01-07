package icu.samnyan.aqua.sega.maimai2

import ext.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.collections.set
import kotlin.concurrent.withLock


const val PROTO_VERSION = 1

private object Command {
    // Control plane
    const val CTL_START = 1
    // const val CTL_BIND = 2  // Actually maybe we don't need BIND
    const val CTL_HEARTBEAT = 3
    const val CTL_ACCEPT = 4  // Accept a new multiplexed TCP stream

    // Data plane
    const val DATA_SEND = 21
    const val DATA_BROADCAST = 22
    const val DATA_POLL = 23

    // Server-to-client message
    const val INCOMING_DATA = 41
    const val INCOMING_CONN = 42
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
    val src: Int? = null,
    val sPort: Int? = null,
    val dst: Int? = null,
    val dPort: Int? = null,

    val data: Any? = null
)
fun ctlMsg(cmd: Int, data: Any? = null) = Message(cmd, 0, data = data)

data class ActiveClient(
    val clientKey: String,
    val socket: Socket,
    val reader: BufferedReader,
    val writer: BufferedWriter
) {
    val log = logger()
    val stubIp by lazy { keychipToStubIp(clientKey) }
    val mutex = ReentrantLock()

    var lastHeartbeat = millis()

    fun send(msg: Message) {
        mutex.withLock {
            writer.write(msg.toJson())
            writer.newLine()
            writer.flush()
        }
    }
}

fun ActiveClient.handle(message: Message) {
    when (message.cmd) {
        Command.CTL_HEARTBEAT -> lastHeartbeat = millis()
        Command.DATA_SEND -> {
            // Find target by dst IP address
            val tId = stubIpToKeychip(message.dst ?: return log.warn("No destination IP"))
            val target = clients[tId] ?: return log.warn("Target not found: $tId")

            // Send to target TODO: SID
            target.send(message.copy(cmd = Command.INCOMING_DATA, sid = 0))
        }
        Command.DATA_BROADCAST -> {
            // Broadcast to all clients. This is only used in UDP so SID is always 0
            assert(message.proto == Proto.UDP)
            clients.values.filter { it.clientKey != clientKey }
                .forEach { it.send(message.copy(cmd = Command.INCOMING_DATA, sid = 0, src = stubIp)) }
        }
    }
}

fun keychipToStubIp(keychip: String) = "1${keychip.substring(1)}".toInt()
fun stubIpToKeychip(ip: Int) = "A${ip.toString().substring(1)}"

// Keychip ID to Socket
val clients = ConcurrentHashMap<String, ActiveClient>()

/**
 * Service for the party linker for AquaMai
 */
class MaimaiFutari(private val port: Int = 20101) {
    val log = logger()

    fun start() {
        val serverSocket = ServerSocket(port)
        log.info("Server started on port $port")

        while (true) {
            val clientSocket = serverSocket.accept()
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
                        clients[id] = ActiveClient(id, socket, reader, writer)
                        handler = clients[id]
                        log.info("[+] Client registered: $id")

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
            if (e.message == "Connection reset") {
                log.info("[-] Client disconnected: ${handler?.clientKey}")
            } else log.error("Error in client handler", e)
        } finally {
            // Remove client
            handler?.clientKey?.let { clients.remove(it) }
            socket.close()
        }
    }
}

fun main() = MaimaiFutari().start()
