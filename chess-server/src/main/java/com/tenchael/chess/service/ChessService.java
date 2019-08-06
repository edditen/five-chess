package com.tenchael.chess.service;

import com.tenchael.chess.ChessRoom;
import com.tenchael.chess.Chesslet;
import com.tenchael.chess.dto.ChessDto;
import com.tenchael.chess.dto.ChessHeader;
import com.tenchael.chess.dto.Operation;
import com.tenchael.chess.dto.Type;
import com.tenchael.chess.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ChessService implements Operations {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChessService.class);
    private final Map<String, ChessRoom> chessRoomTable;
    private final EvaluateService evaluateService;

    public ChessService(Map<String, ChessRoom> chessRoomTable) {
        this.chessRoomTable = chessRoomTable;
        this.evaluateService = new EvaluateService(chessRoomTable);
    }

    @Override
    public ChessDto handle(ChessDto requestDto) {
        ChessRoom room = chessRoomTable.get(requestDto.getHeader().getRoomId());

        List<Map<String, Object>> chesses = (List<Map<String, Object>>) (requestDto.getBody()
                .get("chesses"));
        boolean haveFive = false;
        Set<Chesslet> newChesslets = new HashSet<>();
        for (Map<String, Object> chess : chesses) {
            Chesslet chesslet = BeanUtils.mapToObject(chess, Chesslet.class);
            newChesslets.add(chesslet);
            room.putChesslet(chesslet);
            room.getCount().incrementAndGet();
            haveFive = evaluateService.haveFive(room.getRoomId(), chesslet);
            if (haveFive) {
                LOGGER.debug("room: {} has a winner, the latest location<{}, {}, {}>",
                        room.getRoomId(), chesslet.getX(), chesslet.getY(), chesslet.getRole());
                break;
            }
        }

        ChessHeader respHeader = new ChessHeader(requestDto.getHeader());
        respHeader.setType(Type.resp);
        if (haveFive) {
            respHeader.setOperation(Operation.stop);
        }

        Map<String, Object> respBody = new HashMap<>();
        respBody.put("count", room.getCount().get());
        respBody.put("chesses", newChesslets);

        return new ChessDto(respHeader, respBody);
    }
}
