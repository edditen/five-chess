package com.tenchael.chess;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TextWebSocketFrameHandler
        extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TextWebSocketFrameHandler.class);

    private final ChannelGroup group;

    private final Map<String, Set<String>> sessionChannelId = new ConcurrentHashMap<>();

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
                    new TextWebSocketFrame("{\"type\":\"pong\",\"msg\":\"a new client join\"}"));
            group.add(ctx.channel());
        } else {
            LOGGER.debug("user event trigger: {}", evt);
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        String text = msg.text();
        LOGGER.debug("read message: {}", text);
        Map<String, Object> requestMap = JSON.parseObject(text,
                new TypeReference<Map<String, Object>>() {
                });

        String type = requestMap.get("type").toString();
        String sessionId = requestMap.get("sessionId").toString();
        Map<String, Object> response = new HashMap<>();
        Set<String> channalIds = sessionChannelId.getOrDefault(sessionId, new HashSet<>());
        if (Stats.init.name().equals(type)) {
            String side = "white";
            if (channalIds.isEmpty()) {
                side = "black";
            }
            channalIds.add(ctx.channel().id().asShortText());
            sessionChannelId.putIfAbsent(sessionId, channalIds);

            response.put("type", Stats.init.name());
            response.put("side", side);
            response.put("chesses", new ArrayList<>());

        } else if (Stats.process.name().equals(type)) {
            requestMap.entrySet().stream().forEach(entry -> {
                response.put(entry.getKey(), entry.getValue());
            });
        } else {
            LOGGER.debug("done nothing");
        }

        group.parallelStream()
                .filter(ch -> channalIds.contains(ch.id().asShortText()))
                .forEach(ch -> {
                    String respText = JSON.toJSONString(response);
                    LOGGER.debug("response to ch: {}, msg: {}", ch.id(), respText);
                    ch.writeAndFlush(new TextWebSocketFrame(respText));
                });
    }
}
