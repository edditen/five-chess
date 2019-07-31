package com.tenchael.chess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ChessSession {
    private String sessionId;
    private AtomicInteger count = new AtomicInteger(0);
    private Set<String> channelIds = new HashSet<>();
    private List<Chesslet> chesslets = new ArrayList<>();

    public ChessSession() {
    }

    public ChessSession(String sessionId) {
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

    public Set<String> getChannelIds() {
        return channelIds;
    }

    public void setChannelIds(Set<String> channelIds) {
        this.channelIds = channelIds;
    }

    public List<Chesslet> getChesslets() {
        return chesslets;
    }

    public void setChesslets(List<Chesslet> chesslets) {
        this.chesslets = chesslets;
    }
}
