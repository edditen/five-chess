package com.tenchael.chess.service;

import com.tenchael.chess.ChessRoom;
import com.tenchael.chess.Chesslet;
import com.tenchael.chess.dto.ChessDto;
import com.tenchael.chess.dto.ChessHeader;
import com.tenchael.chess.dto.Type;
import com.tenchael.chess.utils.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessService implements Operations {

    private final Map<String, ChessRoom> chessSessionTable;

    public ChessService(Map<String, ChessRoom> chessSessionTable) {
        this.chessSessionTable = chessSessionTable;
    }

    @Override
    public ChessDto handle(ChessDto requestDto) {
        ChessRoom room = chessSessionTable.get(requestDto.getHeader().getRoomId());

        List<Map<String, Object>> chesses = (List<Map<String, Object>>) (requestDto.getBody()
                .get("chesses"));
        chesses.stream().forEach(chess -> {
            Chesslet chesslet = BeanUtils.mapToObject(chess, Chesslet.class);
            room.getChesslets().add(chesslet);
            room.getCount().incrementAndGet();
        });

        //TODO handle logic

        ChessHeader respHeader = new ChessHeader(requestDto.getHeader());
        respHeader.setType(Type.resp);

        Map<String, Object> respBody = new HashMap<>();
        respBody.put("count", room.getCount().get());
        respBody.put("chesses", room.getChesslets());

        return new ChessDto(respHeader, respBody);
    }
}
