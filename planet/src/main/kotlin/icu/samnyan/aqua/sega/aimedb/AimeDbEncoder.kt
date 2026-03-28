package icu.samnyan.aqua.sega.aimedb

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToByteEncoder

/**
 * @author samnyan (privateamusement@protonmail.com)
 */
class AimeDbEncoder : MessageToByteEncoder<ByteBuf>() {
    override fun encode(ctx: ChannelHandlerContext, msg: ByteBuf, out: ByteBuf) {
        msg.writerIndex(0)
        msg.writeShortLE(0xa13e)
        msg.writeShortLE(0x3087)
        msg.setShortLE(0x0006, msg.capacity())
        val encryptedResp = AimeDbEncryption.encrypt(msg)
        ctx.writeAndFlush(encryptedResp)
    }
}

