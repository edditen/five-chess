package com.tenchael.chess.service;

import com.tenchael.chess.ChessRoom;
import com.tenchael.chess.dto.ChessDto;
import com.tenchael.chess.dto.ChessHeader;
import com.tenchael.chess.dto.Role;
import com.tenchael.chess.dto.Type;

import java.util.HashMap;
import java.util.Map;

public class InitService implements Operations {

    private final Map<String, ChessRoom> chessSessionTable;

    public InitService(Map<String, ChessRoom> chessSessionTable) {
        this.chessSessionTable = chessSessionTable;
    }

    @Override
    public ChessDto handle(ChessDto requestDto) {
        ChessHeader reqHeader = requestDto.getHeader();
        ChessRoom room = chessSessionTable.getOrDefault(requestDto, new ChessRoom());
        Role role = Role.white;
        if (room.getClientIds().size() % 2 == 0) {
            role = Role.black;
        }

        room.getClientIds().add(reqHeader.getClientId());
        chessSessionTable.putIfAbsent(reqHeader.getRoomId(), room);

        ChessHeader respHeader = new ChessHeader();
        respHeader.setType(Type.resp);
        respHeader.setRole(role);
        respHeader.setRoomId(reqHeader.getRoomId());
        respHeader.setClientId(reqHeader.getClientId());
        respHeader.setOperation(reqHeader.getOperation());

        Map<String, Object> respBody = new HashMap<>();
        respBody.put("count", room.getCount().get());
        respBody.put("chesses", room.getChesslets());
        return new ChessDto(respHeader, respBody);
    }
}
