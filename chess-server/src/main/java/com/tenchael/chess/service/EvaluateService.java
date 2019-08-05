package com.tenchael.chess.service;

import com.tenchael.chess.ChessRoom;
import com.tenchael.chess.Chesslet;
import com.tenchael.chess.dto.Role;
import com.tenchael.chess.utils.MixAll;

import java.util.Map;

public class EvaluateService {

    private final Map<String, ChessRoom> chessRoomTable;

    public EvaluateService(Map<String, ChessRoom> chessRoomTable) {
        this.chessRoomTable = chessRoomTable;
    }


    public boolean haveFive(String roomId, Chesslet lastestChesslet) {
        //no winner
        ChessRoom chessRoom = chessRoomTable.get(roomId);
        int maxDepth = maxDepth(chessRoom, lastestChesslet);
        return maxDepth >= 5;
    }

    public int maxDepth(ChessRoom room, Chesslet chesslet) {
        Role role = chesslet.getRole();
        int d1 = depthOfDirection(room, chesslet, role, Direction.LEFT)
                + depthOfDirection(room, chesslet, role, Direction.RIGHT) - 1;
        int d2 = depthOfDirection(room, chesslet, role, Direction.UP)
                + depthOfDirection(room, chesslet, role, Direction.DOWN) - 1;
        int d3 = depthOfDirection(room, chesslet, role, Direction.LEFT_UP)
                + depthOfDirection(room, chesslet, role, Direction.RIGHT_DOWN) - 1;
        int d4 = depthOfDirection(room, chesslet, role, Direction.LEFT_DOWN)
                + depthOfDirection(room, chesslet, role, Direction.RIGHT_UP) - 1;
        return maxValue(d1, d2, d3, d4);
    }

    private int maxValue(int... values) {
        int max = values[0];
        for (int val : values) {
            if (val > max) {
                max = val;
            }
        }
        return max;
    }


    public int depthOfDirection(ChessRoom room, Chesslet chesslet,
                                Role role, Direction direction) {
        if (chesslet == null) {
            return 0;
        }
        if (chesslet.getRole() != role) {
            return 0;
        }

        int x = chesslet.getX() + direction.getDx();
        int y = chesslet.getY() + direction.getDy();

        int nextKey = MixAll.genKey(x, y);

        Chesslet next = room.getChesslets().get(nextKey);

        return depthOfDirection(room, next, role, direction) + 1;
    }


    enum Direction {
        LEFT(-1, 0), RIGHT(1, 0),
        UP(0, -1), DOWN(0, 1),
        LEFT_UP(-1, -1), RIGHT_UP(1, -1),
        LEFT_DOWN(-1, 1), RIGHT_DOWN(1, 1);

        private int dx;
        private int dy;


        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        public int getDx() {
            return dx;
        }

        public int getDy() {
            return dy;
        }
    }
}
