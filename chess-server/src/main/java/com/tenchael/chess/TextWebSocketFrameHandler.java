package com.tenchael.chess;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextWebSocketFrameHandler
        extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TextWebSocketFrameHandler.class);

    private final ChannelGroup group;

    public TextWebSocketFrameHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            LOGGER.debug("HandshakeComplete event");
            ctx.pipeline().remove(HttpRequestHandler.class);
            group.writeAndFlush(
                    new TextWebSocketFrame("{\"type\":\"init\",\"msg\":\"a new client join\"}"));
            group.add(ctx.channel());
        } else {
            LOGGER.debug("user event trigger: {}", evt);
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        LOGGER.debug("read message: {}", msg.text());
        group.writeAndFlush(msg.retain());
    }
}
