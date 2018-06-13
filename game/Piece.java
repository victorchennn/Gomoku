package game;

public class Piece {

    /** A new piece with its color and position. */
    private Piece(PieceColor color, int col, int row) {
        _color = color;
        _col = col;
        _row = row;
    }

    /** Return a new tile at (COL, ROW) with color COLOR. */
    static Piece create(PieceColor color, int col, int row) {
        return new Piece(color, col, row);
    }

    /** Position. */
    private final int _col, _row;

    /** Color. */
    private final PieceColor _color;
}
