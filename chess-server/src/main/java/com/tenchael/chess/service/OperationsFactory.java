package com.tenchael.chess.service;

import com.tenchael.chess.ChessRoom;
import com.tenchael.chess.dto.Operation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OperationsFactory {

    private final Map<String, ChessRoom> chessSessionTable;
    private final Map<Operation, Operations> statChessServiceTable = new ConcurrentHashMap<>();


    public OperationsFactory(Map<String, ChessRoom> chessSessionTable) {
        this.chessSessionTable = chessSessionTable;
        init();
    }

    public void init() {
        statChessServiceTable.put(Operation.init, new InitService(chessSessionTable));
        statChessServiceTable.put(Operation.chess, new ChessService(chessSessionTable));
    }


    public Operations getStatChessService(Operation operation) {
        return statChessServiceTable.get(operation);
    }

}
