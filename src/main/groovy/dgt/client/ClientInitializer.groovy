package dgt.client

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.logging.LogLevel

class ClientInitializer extends ChannelInitializer<SocketChannel> {
	static final StringDecoder DECODER = new StringDecoder()
	static final StringEncoder ENCODER = new StringEncoder()
	static final ClientHandler CLIENT_HANDLER = new ClientHandler()


	@Override
	void initChannel(SocketChannel ch) {
		ch.pipeline().with {
			addLast('framer', new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))
			addLast('decoder', DECODER)
			addLast('encoder', ENCODER)
			addLast(new LoggingHandler(LogLevel.INFO))
			addLast('handler', CLIENT_HANDLER)
		}
	}
}
