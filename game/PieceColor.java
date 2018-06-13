package game;

/** Two different piece colors with an empty type on the board.
 * @author Victor
 */
public enum PieceColor {

    /** EMPTY: no piece.
     *  WHITE, BLACK: piece colors. */
    EMPTY,
    WHITE {
        @Override
        PieceColor opposite() {
            return BLACK;
        }

        @Override
        boolean isPiece() {
            return true;
        }
    },
    BLACK {
        @Override
        PieceColor opposite() {
            return WHITE;
        }

        @Override
        boolean isPiece() {
            return true;
        }
    };

    /** Return the opposite color, if defined. */
    PieceColor opposite() {
        throw new UnsupportedOperationException();
    }

    /** Return true iff this is a piece rather than an empty type. */
    boolean isPiece() {
        return false;
    }
}
