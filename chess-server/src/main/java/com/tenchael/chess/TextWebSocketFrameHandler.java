package com.tenchael.chess;

import com.tenchael.chess.utils.BeanUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TextWebSocketFrameHandler
        extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(TextWebSocketFrameHandler.class);
    private static final Map<String, ChessSession> chessSessionTable = new ConcurrentHashMap<>();
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
        Map<String, Object> requestMap = BeanUtils.jsonToMap(text);

        String type = requestMap.get("type").toString();
        String sessionId = requestMap.get("sessionId").toString();
        Map<String, Object> response = new HashMap<>();
        ChessSession session = chessSessionTable.getOrDefault(sessionId, new ChessSession());
        if (Stats.init.name().equals(type)) {
            Side side = Side.white;
            if (session.getChannelIds().size() % 2 == 0) {
                side = Side.black;
            }
            session.getChannelIds().add(ctx.channel().id().asShortText());
            chessSessionTable.putIfAbsent(sessionId, session);

            response.put("type", Stats.init.name());
            response.put("sessionId", sessionId);
            response.put("side", side);
            response.put("count", session.getCount().get());
            response.put("chesses", session.getChesslets());

            String respText = BeanUtils.objectToJson(response);
            LOGGER.debug("response to ch: {}, msg: {}", ctx.channel().id().asShortText(), respText);
            ctx.writeAndFlush(new TextWebSocketFrame(respText));
        } else if (Stats.process.name().equals(type)) {
            List<Map<String, Object>> chesses = (List<Map<String, Object>>) requestMap.get("chesses");
            chesses.stream().forEach(chess -> {
                Chesslet chesslet = (Chesslet) BeanUtils.mapToObject(chess, Chesslet.class);
                session.getChesslets().add(chesslet);
                session.getCount().incrementAndGet();
            });
            response.putAll(requestMap);
            response.put("count", session.getCount().get());

            group.parallelStream()
                    .filter(ch -> session.getChannelIds().contains(ch.id().asShortText()))
                    .forEach(ch -> {
                        String respText = BeanUtils.objectToJson(response);
                        LOGGER.debug("response to ch: {}, msg: {}", ch.id().asShortText(), respText);
                        ch.writeAndFlush(new TextWebSocketFrame(respText));
                    });
        } else {
            LOGGER.debug("done nothing");
        }

    }
}
