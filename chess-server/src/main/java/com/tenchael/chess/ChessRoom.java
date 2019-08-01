package com.tenchael.chess;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ChessRoom {
    private String sessionId;
    private AtomicInteger count = new AtomicInteger(0);
    private Set<String> clientIds = new HashSet<>();
    private Set<Chesslet> chesslets = new HashSet<>();

    public ChessRoom() {
    }

    public ChessRoom(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    public Set<String> getClientIds() {
        return clientIds;
    }

    public void setClientIds(Set<String> clientIds) {
        this.clientIds = clientIds;
    }

    public Set<Chesslet> getChesslets() {
        return chesslets;
    }

    public void setChesslets(Set<Chesslet> chesslets) {
        this.chesslets = chesslets;
    }
}
