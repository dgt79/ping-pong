package dgt.server

import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundMessageHandlerAdapter

import java.util.logging.Level
import java.util.logging.Logger

@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundMessageHandlerAdapter<String> {
	static final Logger logger = Logger.getLogger(ServerHandler.class.name)

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// Send greeting for a new connection.
//		ctx.write("Welcome to " + InetAddress.localHost.hostName + "!\r\n");
		ctx.write("Welcome to ${InetAddress.localHost.hostName}!\r\n".toString());
		ctx.write("It is " + new Date() + " now.\r\n");
	}

	@Override
	void messageReceived(ChannelHandlerContext ctx, String msg) {
		String response
		boolean close = false

		if (msg.length() == 0) {
			response = "Please type something.\r\n"
		} else if (msg.toLowerCase() == 'bye') {
			response = "Have a good day!\r\n"
			close = true
		} else {
			response = "Server's response: ${msg}\r\n"
		}

		def future = ctx.write(response)

		if (close) future.addListener(ChannelFutureListener.CLOSE)
	}

	@Override
	void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.log(Level.WARNING, "Unexpected exception", cause)
		ctx.close()
	}
}
