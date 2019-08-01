package com.tenchael.chess.handlers;

import com.tenchael.chess.ChessRoom;
import com.tenchael.chess.dto.ChessDto;
import com.tenchael.chess.dto.ChessHeader;
import com.tenchael.chess.dto.ClientInfo;
import com.tenchael.chess.dto.Operation;
import com.tenchael.chess.service.Operations;
import com.tenchael.chess.service.OperationsFactory;
import com.tenchael.chess.utils.BeanUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChessHandler extends SimpleChannelInboundHandler<ChessDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessHandler.class);

    private static final Map<String, ChessRoom> CHESS_ROOM_TABLE = new ConcurrentHashMap<>();
    private static final Map<String, String> CHANNEL_ROOM_TABLE = new HashMap<>();
    private static OperationsFactory factory = new OperationsFactory(CHESS_ROOM_TABLE);
    private final ChannelGroup group;

    public ChessHandler(ChannelGroup group) {
        this.group = group;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChessDto msg)
            throws Exception {
        ChessHeader reqHeader = msg.getHeader();
        CHANNEL_ROOM_TABLE.put(reqHeader.getClientId(), reqHeader.getRoomId());
        Operations operations = factory.getStatChessService(reqHeader.getOperation());

        ChessDto respDto = operations.handle(msg);
        ChessHeader respHeader = respDto.getHeader();
        if (Operation.init == respHeader.getOperation()) {
            String respText = BeanUtils.objectToJson(respDto);
            LOGGER.debug("response to ch: {}, msg: {}", ctx.channel().id().asShortText(), respText);
            ctx.writeAndFlush(new TextWebSocketFrame(respText));
        } else if (Operation.chess == respHeader.getOperation()) {
            String roomId = reqHeader.getRoomId();
            group.parallelStream()
                    .filter(ch -> channelIdsInRoom(roomId).contains(ch.id().asShortText()))
                    .forEach(ch -> {
                        String respText = BeanUtils.objectToJson(respDto);
                        LOGGER.debug("response to ch: {}, msg: {}", ch.id().asShortText(), respText);
                        ch.writeAndFlush(new TextWebSocketFrame(respText));
                    });
        } else {
            LOGGER.debug("done nothing");
        }
    }


    public Set<String> channelIdsInRoom(String roomId) {
        return CHESS_ROOM_TABLE.get(roomId).getClients()
                .stream()
                .map(client -> client.getClientId())
                .collect(Collectors.toSet());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        LOGGER.debug("channelUnregistered: {}", ctx.channel().id().asShortText());
        String channelId = ctx.channel().id().asShortText();
        String roomId = CHANNEL_ROOM_TABLE.get(channelId);
        Set<ClientInfo> clients = CHESS_ROOM_TABLE.get(roomId).getClients();
        for (ClientInfo client : clients) {
            if (client.getClientId().equalsIgnoreCase(channelId)) {
                clients.remove(client);
            }
        }

        if(clients.isEmpty()){
            CHESS_ROOM_TABLE.remove(roomId);
        }

        CHANNEL_ROOM_TABLE.remove(channelId);
        super.channelUnregistered(ctx);
    }


}
