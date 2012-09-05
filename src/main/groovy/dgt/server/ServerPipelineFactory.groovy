package dgt.server

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder

class ServerPipelineFactory extends ChannelInitializer<SocketChannel> {
	static final StringDecoder DECODER = new StringDecoder()
	static final StringEncoder ENCODER = new StringEncoder()
	static final ServerHandler SERVER_HANDLER = new ServerHandler()

	@Override
	public void initChannel(SocketChannel ch) {
//		a pipeline is a stack of interceptors that can manipulate the data passed on the channel
		ch.pipeline().with {
			// Add the text line codec combination first,
			addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()))

			// the encoder and decoder are static as these are sharable
//			converts a ChannelBuffer into a String
			addLast("decoder", DECODER)
//			converts a String into a ChannelBuffer
			addLast("encoder", ENCODER)

			// and then business logic.
			addLast("handler", SERVER_HANDLER)
		}
	}
}
