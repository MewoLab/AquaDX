package icu.samnyan.aqua.sega.aimedb

import ext.logger
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.UnknownHostException

@Configuration
@ConfigurationProperties(prefix = "aimedb.server")
class AimeDbProps {
    var enable = true
    var address = "0.0.0.0"
    var port = 22345
}

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
@Component
class AimeDbServer(
    val initializer: AimeDbServerInitializer,
    val props: AimeDbProps
) {
    val logger = logger()

    fun start() {
        if (!props.enable) return logger.info("Aime DB is disabled.")

        val bootstrap = ServerBootstrap()
            .group(NioEventLoopGroup(), NioEventLoopGroup())
            .handler(LoggingHandler(LogLevel.DEBUG))
            .channel(NioServerSocketChannel::class.java)
            .childHandler(initializer)
            .option(ChannelOption.SO_BACKLOG, 128)

        if (props.address.isBlank()) props.address = "0.0.0.0"

        try {
            val socket = InetSocketAddress(InetAddress.getByName(props.address), props.port)

            val f = bootstrap.bind(socket).sync()
            logger.info("Aime DB start up on $socket")
            f.channel().closeFuture()
        }
        catch (e: UnknownHostException) {
            logger.error("UnknownHostException, please check you have set a correct aimedb.server.address.")
            throw e
        }
    }
}

@Component
class AimeDbServerInitializer(
    val aimeDbRequestHandler: AimeDB
) : ChannelInitializer<SocketChannel>() {
    override fun initChannel(ch: SocketChannel) {
        ch.pipeline().apply {
            addLast("encoder", AimeDbEncoder())
            addLast("decoder", AimeDbDecoder())
            addLast("handler", aimeDbRequestHandler)
        }
    }
}
