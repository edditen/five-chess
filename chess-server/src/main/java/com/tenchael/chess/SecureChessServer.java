package com.tenchael.chess;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.net.InetSocketAddress;

public class SecureChessServer extends ChessServer {

    private final SslContext context;

    public SecureChessServer(SslContext context) {
        this.context = context;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 1) {
//            System.err.println("Please give port as argument");
//            System.exit(1);
//        }
        int port = 8082;
        SelfSignedCertificate cert = new SelfSignedCertificate();
//        SslContext context = SslContext.newServerContext(cert.certificate(), cert.privateKey());
        SslContext context = SslContextBuilder
                .forServer(cert.certificate(), cert.privateKey())
                .build();
        final SecureChessServer endpoint = new SecureChessServer(context);
        ChannelFuture future = endpoint.start(new InetSocketAddress(port));

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
