package com.tenchael.chess;

import com.tenchael.chess.dto.ClientInfo;
import com.tenchael.chess.utils.MixAll;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ChessRoom {
    private String roomId;
    private AtomicInteger count = new AtomicInteger(0);
    private Set<ClientInfo> clients = new HashSet<>();
    private Map<Integer, Chesslet> chesslets = new HashMap<>();

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

    public Set<Chesslet> getChessletSet() {
        return chesslets.values()
                .stream().collect(Collectors.toSet());
    }

    public Map<Integer, Chesslet> getChesslets() {
        return chesslets;
    }

    public void putChesslet(Chesslet chesslet) {
        if (chesslets == null) {
            return;
        }

        if (chesslet.getX() < 0 || chesslet.getY() < 0) {
            return;
        }

        int key = MixAll.genKey(chesslet.getX(), chesslet.getY());
        this.chesslets.putIfAbsent(key, chesslet);

    }
}
