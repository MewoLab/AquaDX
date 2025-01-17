package icu.samnyan.aqua.sega.maimai2.worldslink

import ext.logger
import ext.md5
import ext.millis
import ext.thread
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
const val SO_TIMEOUT = 20000
//const val SO_TIMEOUT = 10000000

fun ctlMsg(cmd: UInt, data: String? = null) = Msg(cmd, data = data)

data class ActiveClient(
    val clientKey: String,
    val socket: Socket,
    val reader: BufferedReader,
    val writer: BufferedWriter,
    val thread: Thread = Thread.currentThread(),
    // <Stream ID, Destination client stub IP>
    val tcpStreams: MutableMap<UInt, UInt> = mutableMapOf(),
    val pendingStreams: MutableSet<UInt> = mutableSetOf(),
) {
    val log = logger()
    val stubIp = keychipToStubIp(clientKey)
    val writeMutex = ReentrantLock()

    var lastHeartbeat = millis()

    fun send(msg: Msg) {
        writeMutex.withLock {
            try {
                writer.write(msg.toString())
                writer.newLine()
                writer.flush()
            }
            catch (e: Exception) {
                log.error("Error sending message", e)
                socket.close()
                thread.interrupt()
            }
        }
    }
}

fun ActiveClient.handle(msg: Msg) {
    // Find target by dst IP address or TCP stream ID
    val target = (msg.sid?.let { tcpStreams[it] } ?: msg.dst)?.let { clients[it] }

    when (msg.cmd) {
        Command.CTL_HEARTBEAT -> {
            lastHeartbeat = millis()
            send(ctlMsg(Command.CTL_HEARTBEAT))
        }
        Command.DATA_BROADCAST -> {
            // Broadcast to all clients. This is only used in UDP so SID is always 0
            if (msg.proto != Proto.UDP) return log.warn("TCP Broadcast received, something is wrong.")
            clients.values.forEach { it.send(msg.copy(src = stubIp)) }
        }
        Command.DATA_SEND -> {
            target ?: return log.warn("Send: Target not found: ${msg.dst}")

            if (msg.proto == Proto.TCP && msg.sid !in tcpStreams)
                return log.warn("Stream ID not found: ${msg.sid}")

            target.send(msg.copy(src = stubIp, dst = target.stubIp))
        }
        Command.CTL_TCP_CONNECT -> {
            target ?: return log.warn("Connect: Target not found: ${msg.dst}")
            val sid = msg.sid ?: return log.warn("Connect: Stream ID not found")

            if (sid in tcpStreams || sid in pendingStreams)
                return log.warn("Stream ID already in use: $sid")

            // Add the stream to the pending list
            pendingStreams.add(sid)
            if (pendingStreams.size > MAX_STREAMS) {
                log.warn("Too many pending streams, closing connection")
                return socket.close()
            }

            target.send(msg.copy(src = stubIp, dst = target.stubIp))
        }
        Command.CTL_TCP_ACCEPT -> {
            target ?: return log.warn("Accept: Target not found: ${msg.dst}")
            val sid = msg.sid ?: return log.warn("Accept: Stream ID not found")

            if (sid !in target.pendingStreams)
                return log.warn("Stream ID not found in pending list: $sid")

            // Add the stream to the active list
            target.pendingStreams.remove(sid)
            target.tcpStreams[sid] = stubIp
            tcpStreams[sid] = target.stubIp

            target.send(msg.copy(src = stubIp, dst = target.stubIp))
        }
    }
}

fun String.hashToUInt() = md5().let {
    ((it[0].toUInt() and 0xFFu) shl 24) or
    ((it[1].toUInt() and 0xFFu) shl 16) or
    ((it[2].toUInt() and 0xFFu) shl 8) or
    (it[3].toUInt() and 0xFFu)
}

fun keychipToStubIp(keychip: String) = keychip.hashToUInt()

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
                val input = (reader.readLine() ?: if (Thread.interrupted()) break else continue).trim('\uFEFF')
                if (input != "1,3") log.info("${socket.remoteSocketAddress} (${handler?.clientKey}) <<< $input")
                val message = Msg.fromString(input)

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
                        handler?.send(ctlMsg(Command.CTL_START, "version=$PROTO_VERSION"))
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
