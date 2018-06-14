package game;

import java.util.Observable;
import java.util.Stack;

import static game.PieceColor.*;

public class Board extends Observable {

    Board() {
        clear();
    }

    Board(Board board) {

    }

    void clear() {
        _gameover = false;
        _whoseMove = BLACK;
        _log = new Stack<>();
        _table = new String[SIDE][SIDE];

    }

    void setPieces(String str, PieceColor pieceColor) {
        if (pieceColor == EMPTY || pieceColor == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{25}")) {
            throw new IllegalArgumentException("bad board description");
        }

        _whoseMove = pieceColor;

        for (int k = 0; k < str.length(); k += 1) {
            switch (str.charAt(k)) {
                case '-':
                    set(k, EMPTY);
                    break;
                case 'b':
                case 'B':
                    set(k, BLACK);
                    break;
                case 'w':
                case 'W':
                    set(k, WHITE);
                    break;
                default:
                    break;
            }
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Set get(K) to V, where K is the linearized index of a square.
     */
    private void set(int k, PieceColor v) {

    }

    Boolean gameOver() {
        return null;
    }

    void play(Piece piece) {

    }

    /**
     * Return the color of the player who has the next move.  The
     * value is arbitrary if gameOver().
     */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** The index of column. */
    private static int col(int k) {
        return k % SIDE;
    }

    /** The index of row. */
    private static int row(int k) {
        return k / SIDE;
    }

    /** Size of a side of the board. */
    static final int SIDE = 15;

    /** Maximum linearized index. */
    private final int MAX_INDEX = SIDE * SIDE - 1;

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** True Iff game is over. */
    private boolean _gameover;

    /** Represent the whole table. */
    private String[][] _table;

    /** Used to record the each step. */
    private Stack<Piece> _log;
}
