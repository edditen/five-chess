package com.tenchael.chess;

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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

public class ChessServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessServer.class);

    private final ChannelGroup channelGroup = new DefaultChannelGroup(
            ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final Properties properties = new Properties();
    private Channel channel;

    public ChessServer() {
        String pathProp = System.getProperty("configFile");
        if (pathProp == null || pathProp.trim().length() == 0) {
            try (InputStream input = ChessServer.class.getClassLoader()
                    .getResourceAsStream("app.properties")) {
                properties.load(input);
            } catch (IOException ex) {
                LOGGER.error("read properties error", ex);
            }
        } else {
            try (InputStream input = new FileInputStream(pathProp)) {
                properties.load(input);
            } catch (IOException ex) {
                LOGGER.error("read properties error", ex);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final ChessServer chessServer = new ChessServer();
        int port = Integer.valueOf(chessServer.properties.get("port").toString());

        ChannelFuture future = chessServer.start(new InetSocketAddress(port));

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


    private void properties(String[] args) {


    }


}
