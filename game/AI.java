package game;

import static game.PieceColor.*;

public class AI extends Player {

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Piece next() {
        Board b = new Board(board());
        if (myColor() == WHITE) {
            findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, 1);
        } else {
            findPiece(b, MAX_DEPTH, -INFINITY, INFINITY, -1);
        }
        return _lastStep;
    }


    private int findPiece(Board board, int depth, int alpha, int beta, int step) {
        return Integer.parseInt(null);
    }

    /**
     * Used to count heuristic score on the board to determine which step
     * is best.
     */
    private int score(Board board) {
        return Integer.parseInt(null);
    }

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 5;

    /** A magnitude greater than a normal value. */
    private static final int INFINITY = Integer.MAX_VALUE;

    /** The piece found by the last call to findMove method. */
    private Piece _lastStep;
}
