package game;

/** Piece represent the each 'dot' on the board, it can be
 *  white or black. Use coordinate to record the position of
 *  the piece on the board.
 *  @author Victor
 */
public class Piece {

    /** A new piece with its color and position. */
    private Piece(PieceColor color, int col, int row) {
        _color = color;
        _col = col;
        _row = row;
    }

    /** Return a new piece at (COL, ROW) with color COLOR. */
    static Piece create(PieceColor color, int col, int row) {
        return new Piece(color, col, row);
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
