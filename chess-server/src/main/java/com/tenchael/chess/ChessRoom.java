package com.tenchael.chess;

import com.tenchael.chess.dto.ClientInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ChessRoom {
    private String roomId;
    private AtomicInteger count = new AtomicInteger(0);
    private Set<ClientInfo> clients = new HashSet<>();
    private Set<Chesslet> chesslets = new HashSet<>();

    public ChessRoom() {
    }

    public ChessRoom(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public AtomicInteger getCount() {
        return count;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    public Set<ClientInfo> getClients() {
        return clients;
    }

    public void setClients(Set<ClientInfo> clients) {
        this.clients = clients;
    }

    public Set<Chesslet> getChesslets() {
        return chesslets;
    }

    public void setChesslets(Set<Chesslet> chesslets) {
        this.chesslets = chesslets;
    }
}
