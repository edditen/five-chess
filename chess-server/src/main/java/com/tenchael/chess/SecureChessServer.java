package com.tenchael.chess;

import com.tenchael.chess.config.Configs;
import com.tenchael.chess.config.Constants;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class SecureChessServer extends ChessServer {


    private static final Logger LOGGER = LoggerFactory.getLogger(SecureChessServer.class);

    private final SslContext context;

    public SecureChessServer(SslContext context) {
        this.context = context;
    }

    public static void main(String[] args) throws Exception {
        SelfSignedCertificate cert = new SelfSignedCertificate();
        SslContext context = SslContextBuilder
                .forServer(cert.certificate(), cert.privateKey())
                .build();
        final SecureChessServer endpoint = new SecureChessServer(context);

        int port = Configs.getInt(Constants.PORT, 8080);
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));
        LOGGER.info("started chess server, listen on: {}", port);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                endpoint.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }

    @Override
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup group) {
        return new SecureChessServerIntializer(group, context);
    }
}
