package game;

import java.util.Formatter;

/** Piece represent the each 'dot' on the board, it can be
 *  white or black. Use coordinate to record the position of
 *  the piece on the board.
 *  @author Victor
 */
public class Piece {

    /** A new piece with its color and position. */
    private Piece(PieceColor color, int row, int col) {
        _color = color;
        _col = col;
        _row = row;
    }

    /** Return a new piece at (COL, ROW) with color COLOR. */
    static Piece create(PieceColor color, int row, int col) {
        return new Piece(color, row, col);
    }

    @Override
    public String toString() {
        Formatter f = new Formatter();
        f.format("row = %s, col = %s, color = %s", _row, _col, _color);
        return f.toString();
    }

    /** Return the column of the current piece. */
    int col() {
        return _col;
    }

    /** Return the row of the current piece. */
    int row() {
        return _row;
    }

    /** Return the color of the current piece. */
    PieceColor color() {
        return _color;
    }

    /** Position. */
    private final int _col, _row;

    /** Color. */
    private final PieceColor _color;
}
