package com.tenchael.chess.service;

import com.tenchael.chess.ChessRoom;
import com.tenchael.chess.dto.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InitService implements Operations {

    private final Map<String, ChessRoom> chessSessionTable;

    public InitService(Map<String, ChessRoom> chessSessionTable) {
        this.chessSessionTable = chessSessionTable;
    }

    @Override
    public ChessDto handle(ChessDto requestDto) {
        ChessHeader reqHeader = requestDto.getHeader();
        ChessRoom room = chessSessionTable.getOrDefault(reqHeader.getRoomId(), new ChessRoom());
        Role role = disputeRole(room);

        room.setRoomId(reqHeader.getRoomId());
        ClientInfo client = new ClientInfo(reqHeader.getClientId(), role);
        room.getClients().add(client);
        chessSessionTable.putIfAbsent(reqHeader.getRoomId(), room);

        ChessHeader respHeader = new ChessHeader();
        respHeader.setType(Type.resp);
        respHeader.setRole(role);
        respHeader.setRoomId(reqHeader.getRoomId());
        respHeader.setClientId(reqHeader.getClientId());
        respHeader.setOperation(reqHeader.getOperation());

        Map<String, Object> respBody = new HashMap<>();
        respBody.put("count", room.getCount().get());
        respBody.put("chesses", room.getChessletSet());
        return new ChessDto(respHeader, respBody);
    }

    private Role disputeRole(ChessRoom room) {
        Set<ClientInfo> clients = room.getClients();
        if (clients.stream()
                .filter(client -> client.getRole() == Role.black)
                .collect(Collectors.toSet()).isEmpty()) {
            return Role.black;
        }
        if (clients.stream()
                .filter(client -> client.getRole() == Role.white)
                .collect(Collectors.toSet()).isEmpty()) {
            return Role.white;
        }
        return Role.observer;
    }

}
