package dgt.server

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.socket.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.logging.LogLevel

class Server {
	final int port

	public Server(int port) {
		this.port = port
	}

	public void	run() {
		ServerBootstrap bootstrap = new ServerBootstrap()
		try {
			bootstrap.with {
//				two thread pools
//				boss threads: 	connect/bind sockets and then pass them to worker threads
//								one boss thread per listening socket
//				worker threads: perform the async IO
				group(new NioEventLoopGroup(), new NioEventLoopGroup())
//				the communication channel
				channel(new NioServerSocketChannel())
				localAddress(new InetSocketAddress(port))
				handler(new LoggingHandler(LogLevel.INFO))
				childHandler(new ServerPipelineFactory())
			}

			bootstrap.bind().sync().channel().closeFuture().sync()
		} finally {
			bootstrap.shutdown()
		}
	}

	public static void main(String[] args) {
		int port = 9999
		if (args.length > 0) port = args[0].toInteger()

		new Server(port).run()
	}
}
