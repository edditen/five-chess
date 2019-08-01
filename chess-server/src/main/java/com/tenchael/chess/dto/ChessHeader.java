package com.tenchael.chess.dto;

public class ChessHeader {
    private Type type;
    private String roomId;
    private String clientId;
    private Operation operation;
    private Role role;

    public ChessHeader() {
    }

    public ChessHeader(ChessHeader header) {
        setType(header.getType());
        setRoomId(header.getRoomId());
        setClientId(header.getClientId());
        setOperation(header.getOperation());
        setRole(header.getRole());
    }


    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
