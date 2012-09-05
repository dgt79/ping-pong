package dgt.client

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundMessageHandlerAdapter

import java.util.logging.Level
import java.util.logging.Logger
import io.netty.channel.ChannelHandler

@ChannelHandler.Sharable
class ClientHandler extends ChannelInboundMessageHandlerAdapter<String> {
	static final Logger logger = Logger.getLogger(ClientHandler.class.name)

	@Override
	void messageReceived(ChannelHandlerContext ctx, String msg) {
		logger.log(Level.INFO, msg)
		println msg
	}

	@Override
	void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.log(Level.WARNING, 'Unexpected exception', cause)
		ctx.close()
	}
}
