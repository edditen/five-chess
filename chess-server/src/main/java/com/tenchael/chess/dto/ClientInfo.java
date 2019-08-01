package com.tenchael.chess.dto;

public class ClientInfo {
    private String clientId;
    private Role role;

    public ClientInfo() {
    }

    public ClientInfo(String clientId, Role role) {
        this.clientId = clientId;
        this.role = role;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
