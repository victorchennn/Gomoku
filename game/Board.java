package game;

import java.util.Observable;
import java.util.Set;
import java.util.HashSet;
import java.util.Formatter;
import java.util.Stack;

import static game.PieceColor.*;

/**
 * A Gomoku board.   The squares are labeled by column and row, both of them are
 * integer range from 1 to 15. A standard board size is 15*15.
 * <p>
 * For some purposes, it is useful to refer to squares using a single
 * integer, which we call its "linearized index".  This is simply the
 * number of the square in row-major order (with row 0 being the bottom row)
 * counting from 0).
 * <p>
 * pieces on this board are denoted by Pieces.
 *
 * @author Victor
 */
public class Board extends Observable {

    /** A new, cleared board at the start of the game. */
    Board() {
        clear();
    }

    /** A replicated version of my board. */
    Board(Board board) {
        copy(board);
    }

    /** Copy everything on the BOARD to me. */
    private void copy(Board board) {
        _log = board._log;
        _gameover = board._gameover;
        _whoseMove = board._whoseMove;
        _table = new String[SIDE][SIDE];
        for (int i = 0; i <= MAX_INDEX; i++) {
            _table[row(i)][col(i)] = board.get(i).shortName();
        }
    }

    /** Clear me to my starting state, with no piece on the board. */
    void clear() {
        _gameover = false;
        _log = new Stack<>();
        _table = new String[SIDE][SIDE];
        String initial = "";
        for (int i = 0; i <= MAX_INDEX; i++) {
            initial += '-';
        }
        setPieces(initial, BLACK);
        setChanged();
        notifyObservers();
    }

    /**
     * Start the game by setting the new piece to the board and push each
     * piece to the stack in case of undoing.
     */
    void play(Piece piece) {
        if (!_table[piece.row() - 1][piece.col() - 1].equals("-")) {
            System.err.println("Illegal position.");
        } else {
            this.set(piece.row(), piece.col(), piece.color());
            _log.push(piece);
            _whoseMove = _whoseMove.opposite();
        }
        setChanged();
        notifyObservers();
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
            throw new IllegalArgumentException("bad player color.");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{225}")) {
            throw new IllegalArgumentException("bad board description.");
        }
        _whoseMove = pieceColor;
        for (int k = 0; k < str.length(); k += 1) {
            switch (str.charAt(k)) {
                case '-':
                    set(k, EMPTY);
                    break;
                case 'b':
                    set(k, BLACK);
                    break;
                case 'w':
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
     * Return the current contents of the square at coordinate (R, C).
     */
    PieceColor get(int r, int c) {
        assert 0 < c && c <= SIDE;
        assert 0 < r && r <= SIDE;
        if (_table[r - 1][c - 1].equals("b")) {
            return BLACK;
        } else if (_table[r - 1][c - 1].equals("w")) {
            return WHITE;
        } else {
            return EMPTY;
        }
    }

    /**
     * Return the current contents of the square at linearized index K.
     */
    PieceColor get(int k) {
        assert validSquare(k);
        return get(row(k) + 1, col(k) + 1);
    }

    /** Set K to V, where K is the linearized index of a square. */
    void set(int k, PieceColor v) {
        assert validSquare(k);
        set(row(k) + 1, col(k) + 1, v);
    }

    /** Set (R, C) to V, where (R, C) is the coordinate of a square. */
    void set(int r, int c, PieceColor v) {
        assert 0 < c && c <= SIDE;
        assert 0 < r && r <= SIDE;
        _table[r - 1][c - 1] = v.shortName();
    }

    /**
     * Return true iff the game is over: first player to form an unbroken
     * chain of five pieces horizontally, vertically, or diagonally.
     */
    Boolean gameOver() {
        for (int i = 0; i <= MAX_INDEX; i++) {
            if (get(i).isPiece()) {
                for (int t = 1; t <= 4; t++) {
                    _gameover = check(i, t);
                    if (_gameover) {
                        return _gameover;
                    }
                }
            }
        }
        return _gameover;
    }

    /**
     * Help to check four directions(360/45/2=4)，up/right/upright/upleft/
     * and see if it has an unbroken chain of five pieces with same color.
     * Due to the method of checking from index 0 to MAX, other four directions
     * will also be checked by lower index so it is useless. But in the formal
     * game actually check all the eight directions are necessary, in that case
     * we do not need to iterate all the index, just checking the last index and
     * see if its eight directions can form an unbroken chain of same pieces.
     */
    private Boolean check(int k, int t) {
        PieceColor mycolor = get(k);
        switch (t) {
            case 1:
                if (col(k) > 10) {
                    return false;
                }
                for (int i = 1; i < 5; i++) {
                    if (!validSquare(k + i)
                            || !mycolor.equals(get(k + i))) {
                        return false;
                    }
                }
                break;
            case 2:
                for (int i = 1; i < 5; i++) {
                    if (!validSquare(k + i * SIDE)
                            || !mycolor.equals(get(k + i * SIDE))) {
                        return false;
                    }
                }
                break;
            case 3:
                if (col(k) > 10) {
                    return false;
                }
                for (int i = 1; i < 5; i++) {
                    if (!validSquare(k + i + i * SIDE)
                            || !mycolor.equals(get(k + i + i * SIDE))) {
                        return false;
                    }
                }
                break;
            case 4:
                if (col(k) < 4) {
                    return false;
                }
                for (int i = 1; i < 5; i++) {
                    if (!validSquare(k - i + i * SIDE)
                            || !mycolor.equals(get(k - i + i * SIDE))) {
                        return false;
                    }
                }
                break;
//            case 5:
//                for (int i = 1; i < 5; i++) {
//                    if (!validSquare(k - i)
//                            || !mycolor.equals(get(k - i))) {
//                        return false;
//                    }
//                }
//                break;
//            case 6:
//                for (int i = 1; i < 5; i++) {
//                    if (!validSquare(k - i * SIDE)
//                            || !mycolor.equals(get(k - i * SIDE))) {
//                        return false;
//                    }
//                }
//                break;
//            case 7:
//                for (int i = 1; i < 5; i++) {
//                    if (!validSquare(k - i - i * SIDE)
//                            || !mycolor.equals(get(k - i - i * SIDE))) {
//                        return false;
//                    }
//                }
//                break;
//            case 8:
//                for (int i = 1; i < 5; i++) {
//                    if (!validSquare(k + i - i * SIDE)
//                            || !mycolor.equals(get(k + i - i * SIDE))) {
//                        return false;
//                    }
//                }
//                break;
        }
        return true;
    }


    PieceColor[][] count(int k) {
        assert validSquare(k);
        PieceColor score[][] = new PieceColor[4][9];
        for (int i = 1; i <= 4; i++) {
            /* Y axis, from top to bottom. */
            score[0][4] = get(k);
            score[0][4 - i] = (validSquare(k + i * SIDE)? get(k + i * SIDE) : EMPTY);
            score[0][4 + i] = (validSquare(k - i * SIDE)? get(k - i * SIDE) : EMPTY);
            /* X axis, from right to left. */
            score[1][4] = get(k);
            score[1][4 - i] = (validSquare(k + i) &&
                    row(k) == row(k + i) ? get(k + i) : EMPTY);
            score[1][4 + i] = (validSquare(k - i) &&
                    row(k) == row(k - i) ? get(k - i) : EMPTY);
            /* 45° diagonal, from top-right to bottom-left. */
            score[2][4] = get(k);
            score[2][4 - i] = (validSquare(k + i + i * SIDE) &&
                    row(k) + i == row(k + i + i * SIDE) ? get(k + i + i * SIDE) : EMPTY);
            score[2][4 + i] = (validSquare(k - i - i * SIDE) &&
                    row(k) - i == row(k - i - i * SIDE) ? get(k - i - i * SIDE) : EMPTY);
            /* 135° diagonal, from top-left to bottom-right. */
            score[3][4] = get(k);
            score[3][4 - i] = (validSquare(k - i + i * SIDE) &&
                    row(k) + i == row(k - i + i * SIDE) ? get(k - i + i * SIDE) : EMPTY);
            score[3][4 + i] = (validSquare(k + i - i * SIDE) &&
                    row(k) - i == row(k + i - i * SIDE) ? get(k + i - i * SIDE) : EMPTY);
        }
        return score;
    }

    PieceColor[][] count2(int k) {
        PieceColor score[][] = new PieceColor[4][4];
        for (int i = 1; i < 5; i++) {
            /* Y axis, from top to bottom. */
            score[0][i - 1] = (validSquare(k + i * SIDE)? get(k + i * SIDE) : EMPTY);
            /* X axis, from right to left. */
            score[1][i - 1] = (validSquare(k + i) &&
                    row(k) == row(k + i) ? get(k + i) : EMPTY);
            /* 45° diagonal, from top-right to bottom-left. */
            score[2][i - 1] = (validSquare(k + i + i * SIDE) &&
                    row(k) + i == row(k + i + i * SIDE) ? get(k + i + i * SIDE) : EMPTY);
            /* 135° diagonal, from top-left to bottom-right. */
            score[3][i - 1] = (validSquare(k - i + i * SIDE) &&
                    row(k) + i == row(k - i + i * SIDE) ? get(k - i + i * SIDE) : EMPTY);
        }
        return score;
    }

    /**
     * To record the number of chain of one, two, three, four and five pieces
     * from all the pieces of the color COLOR on the board.
     */
    int[] chainOfPieces(PieceColor color) {
        int[] stat = new int[5];
        int num = 0;
        for (int i = 0; i <= MAX_INDEX; i++) {
            if (get(i).equals(color)) {
                num++;
                int row = row(i);
                for (int cs = 1; cs <= 4; cs++) {
                    int t = 1;
                    if (cs == 1) {
                        for (; t < 5; t++) {
                            if (!validSquare(i + t) || !color.equals(get(i + t))
                                    || !(row == row(i + t))) {
                                break;
                            }
                        }
                    } else if (cs == 2) {
                        for (; t < 5; t++) {
                            if (!validSquare(i + t * SIDE) ||
                                    !color.equals(get(i + t * SIDE))) {
                                break;
                            }
                        }
                    } else if (cs == 3) {
                        for (; t < 5; t++) {
                            if (!validSquare(i + t + t * SIDE) ||
                                    !color.equals(get(i + t + t * SIDE)) ||
                                    !((row + t) == row(i + t + t * SIDE))) {
                                break;
                            }
                        }
                    } else {
                        for (; t < 5; t++) {
                            if (!validSquare(i - t + t * SIDE) ||
                                    !color.equals(get(i - t + t * SIDE)) ||
                                    !((row + t) == row(i - t + t * SIDE))) {
                                break;
                            }
                        }
                    }
                    stat[t - 1]++;
                }
            }
        }
        stat[0] = num;
        return stat;
    }

    /**
     * Get available and potential pieces on the board, always add pieces
     * with two relative distance away from one existed piece on the
     * board to the arraylist. Only add the central point if the board is
     * empty.
     */
    Set<Piece> getPotentialPieces(Boolean advance) {
        Set<Piece> potent = new HashSet<>();
        if (numberOfPieces() == 0) {
            int cent = 1 + SIDE / 2;
            potent.add(Piece.create(whoseMove(), cent, cent));
            return potent;
        }
        for (int i = 0; i <= MAX_INDEX; i++) {
            if (get(i).isPiece()) {
                for (int index : getAdjacentIndex(i, advance)) {
                    if (!get(index).isPiece()) {
                        potent.add(Piece.create(whoseMove(),
                                row(index) + 1, col(index) + 1));
                    }
                }
            }
        }
        return potent;
    }

    /**
     * Get the index of adjacent positions with two relative distance away.
     */
    Set<Integer> getAdjacentIndex(int k, boolean advance) {
        Set<Integer> adjc = new HashSet<>();
        Boolean ad = true;
        for (int i = 1; i <= 2 && ad; i++) {
            if (validSquare(k + i * SIDE)) {
                adjc.add(k + i * SIDE);
            }
            if (validSquare(k - i * SIDE)) {
                adjc.add(k - i * SIDE);
            }
            if (validSquare(k + i) && row(k) == row(k + i)) {
                adjc.add(k + i);
            }
            if (validSquare(k - i) && row(k) == row(k - i)) {
                adjc.add(k - i);
            }
            for (int t = 1; t <= 2 && ad; t++) {
                if (validSquare(k + t + i * SIDE) &&
                        row(k) + i == row(k + t + i * SIDE)) {
                    adjc.add(k + t + i * SIDE);
                }
                if (validSquare(k - t + i * SIDE) &&
                        row(k) + i == row(k - t + i * SIDE)) {
                    adjc.add(k - t + i * SIDE);
                }
                if (validSquare(k + t - i * SIDE) &&
                        row(k) - i == row(k + t - i * SIDE)) {
                    adjc.add(k + t - i * SIDE);
                }
                if (validSquare(k - t - i * SIDE) &&
                        row(k) - i == row(k - t - i * SIDE)) {
                    adjc.add(k - t - i * SIDE);
                }
                if (!advance) {
                    ad = false;
                }
            }
        }
        return adjc;
    }

    /**
     * Cancel last two pieces (one white and one black).
     */
    void undo() {
        if (_log.size() < 2) {
            System.err.println("Illegal Undo.");
        }
        Piece one = _log.pop();
        this.set(one.row(), one.col(), EMPTY);
        Piece two = _log.pop();
        this.set(two.row(), two.col(), EMPTY);
        setChanged();
        notifyObservers();
    }

    /** Return the number of pieces. */
    int numberOfPieces() {
        int number = 0;
        for (int i = 0; i <= MAX_INDEX; i++) {
            if (!get(i).equals(EMPTY)) {
                number++;
            }
        }
        return number;
    }

    /** Return true iff K is a valid linearized index. */
    private boolean validSquare(int k) {
        return 0 <= k && k <= MAX_INDEX;
    }

    /** Return the all the pieces after SETUP state. */
    Stack<Piece> log() {
        return _log;
    }

    /**
     * Return the color of the player who has the next move.  The
     * value is arbitrary if gameOver().
     */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Get column from index. */
    static int col(int k) {
        return k % SIDE;
    }

    /** Get row from index. */
    static int row(int k) {
        return k / SIDE;
    }

    /** Get index from row and column. */
    static int index(int row, int col) {
        return SIDE * (row - 1) + (col - 1);
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
    static int MAX_INDEX = SIDE * SIDE - 1;

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** True Iff game is over. */
    private boolean _gameover;

    /** Represent the whole table. */
    private String[][] _table;

    /** Used to record the each step. */
    private Stack<Piece> _log;
}
