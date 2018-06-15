package game;

/** Piece represent the each 'dot' on the board, it can be
 *  white or black. Use coordinate to record the position of
 *  the piece on the board.
 *  @author Victor
 */
public class Piece {

    /** A new piece with its color and position. */
    private Piece(PieceColor color, int row, int col) {
        _color = color;
        _row = row;
        _col = col;
    }

    /** Return a new piece at (ROW, COL) with color COLOR. */
    static Piece create(PieceColor color, int row, int col) {
        return new Piece(color, row, col);
    }

    /** Return the column of the current piece. */
    int col() {
        return _col;
    }

    /** Return the row of the current piece. */
    int row() {
        return _row;
    }

    /** Position. */
    private final int _col, _row;

    /** Color. */
    private final PieceColor _color;
}
