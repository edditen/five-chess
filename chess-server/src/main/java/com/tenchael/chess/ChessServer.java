package com.tenchael.chess;

import com.tenchael.chess.config.Configs;
import com.tenchael.chess.config.Constants;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class ChessServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessServer.class);

    private final ChannelGroup channelGroup = new DefaultChannelGroup(
            ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private Channel channel;


    public static void main(String[] args) throws Exception {
        final ChessServer chessServer = new ChessServer();

        int port = Configs.getInt(Constants.PORT, 8080);
        ChannelFuture future = chessServer.start(new InetSocketAddress(port));

        LOGGER.info("started chess server, listen on: {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                chessServer.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }

    public ChannelFuture start(InetSocketAddress address) {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(createInitializer(channelGroup));
        ChannelFuture future = bootstrap.bind(address);
        future.syncUninterruptibly();
        channel = future.channel();
        return future;
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
        return new ChessServerInitializer(group);
    }

    public void destroy() {
        if (channel != null) {
            channel.close();
        }
        channelGroup.close();
        group.shutdownGracefully();
    }

}
