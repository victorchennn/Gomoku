package game;

import java.util.Formatter;
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
        String initial = "";
        for (int i = 0; i <= MAX_INDEX; i++) {
            initial += '-';
        }
        setPieces(initial, BLACK);

    }

    /**
     * Set my contents as defined by STR.  STR consists of 225 characters,
     * each of which is b, w, or -, optionally interspersed with whitespace.
     * These give the contents of the Board in row-major order, starting
     * with the bottom row (row 1) and left column (column 1).
     * PIECECOLOR indicates whose move it is.
     */
    void setPieces(String str, PieceColor pieceColor) {
        if (pieceColor == EMPTY || pieceColor == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{225}")) {
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

    /** Set K to V, where K is the linearized index of a square. */
    public void set(int k, PieceColor v) {
        assert 0 <= k && k <= MAX_INDEX;
        _table[row(k)][col(k)] = v.shortName();
    }

    /** Set (R, C) to V, where (R, C) is the coordinate of a square. */
    public void set(int c, int r, PieceColor v) {
        assert 0 <= c && c < SIDE;
        assert 0 <= r && r < SIDE;
        _table[r][c] = v.shortName();
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

    @Override
    public String toString() {
        Formatter out = new Formatter();
        for(int r = SIDE - 1; r >= 0; r--) {
            if (r < 9) {
                out.format("%d ", r + 1);
            } else {
                out.format("%d", r + 1);
            }
            for (int c = 0; c < SIDE; c++) {
                if (c == SIDE - 1) {
                    out.format("  %s%n", _table[r][c]);
                } else {
                    out.format("  %s", _table[r][c]);
                }
            }
        }
        for (int col = 1; col <= SIDE; col++) {
            if (col == 1) {
                out.format(" %4d", col);
            } else if (col > 9) {
                out.format(" %d", col);
            } else {
                out.format("%3d", col);
            }
        }
        return out.toString();
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
