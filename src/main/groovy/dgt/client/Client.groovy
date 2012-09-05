package dgt.client

import io.netty.bootstrap.Bootstrap
import io.netty.channel.socket.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture

class Client {
	final String host
	final int port

	public Client(String host, int port) {
		this.host = host
		this.port = port
	}

	public void run() {
		Bootstrap bootstrap = new Bootstrap()
		try {
			bootstrap.with {
				group(new NioEventLoopGroup())
				channel(new NioSocketChannel())
				remoteAddress(new InetSocketAddress(host, port))
				handler(new ClientInitializer())
			}

			Channel ch = bootstrap.connect().sync().channel()

			def future = null
			def input = new BufferedReader(new InputStreamReader(System.in))
			while (true) {
				def line = input.readLine()
				if (line == null) {	break }

				future = ch.write(line + "\r\n")

				if (line.toLowerCase().equals("bye")) {
					ch.closeFuture().sync();
					break;
				}
			}

			// Wait until all messages are flushed before closing the channel.
			if (future != null) {
				future.sync();
			}
		} finally {
			bootstrap.shutdown()
		}
	}

	public static void main(String[] args) {
		final String host = "0.0.0.0"
		final int port = 9999

		new Client(host, port).run()
	}
}
