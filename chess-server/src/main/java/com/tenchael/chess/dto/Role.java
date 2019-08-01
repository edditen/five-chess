package com.tenchael.chess.dto;

public enum Role {
    white(1), black(2), observer(3);

    private int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
