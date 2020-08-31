package whohim.netty.several.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author WhomHim
 * <p>客户端</p>
 */
public class SingleClient {
    //多个tcp服务器的端口
    private static List<Integer> PORTS = Arrays.asList(8080, 8089);

    static final String HOST = System.getProperty("host", "127.0.0.1");

    public static void main(String[] args) {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast("decoder", new StringDecoder());
                            p.addLast("encoder", new StringEncoder());
                            p.addLast(new SingleClientHandler());
                        }
                    });


            PORTS.parallelStream().forEach(port -> {
                ChannelFuture future;
                try {
                    future = bootstrap.connect(HOST, port).sync();
                    future.channel().writeAndFlush(String.format("Hello Multi Server ,I am a common client[%d]", port));
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });


        } finally {
            group.shutdownGracefully();
        }
    }
}
