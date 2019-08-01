package com.tenchael.chess;

import com.tenchael.chess.dto.Role;

public class Chesslet {
    private int x;
    private int y;
    private Role role;

    public Chesslet() {
    }

    public Chesslet(int x, int y, Role role) {
        this.x = x;
        this.y = y;
        this.role = role;
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return x * 1000 + y * 10 + ((role == null)
                ? 0
                : role.getValue());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        Chesslet chesslet = (Chesslet) obj;

        return (chesslet.x == this.x
                && chesslet.y == this.y
                && chesslet.role == this.role);
    }
}
