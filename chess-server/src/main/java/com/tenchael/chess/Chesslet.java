package com.tenchael.chess;

public class Chesslet {
    private int x;
    private int y;
    private Side side;

    public Chesslet() {
    }

    public Chesslet(int x, int y, Side side) {
        this.x = x;
        this.y = y;
        this.side = side;
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

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }
}
