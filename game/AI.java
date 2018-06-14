package game;

public class AI extends Player {

    /** A new AI for GAME that will play MYCOLOR. */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Piece next() {
        return null;
    }

}
