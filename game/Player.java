package game;

/** A generic Gomoku Player.
 *  @author Victor
 */
abstract public class Player {

    /** A Player that will play MYCOLOR in GAME. */
    Player(Game game, PieceColor myColor) {
        _game = game;
        _myColor = myColor;
    }

    /** Return my pieces' color. */
    PieceColor myColor() {
        return _myColor;
    }

    /** Return the game I am playing in. */
    Game game() {
        return _game;
    }

    /** Return a view of the board I am playing on. */
    Board board() {
        return _game.board();
    }

    /** Return a legal piece for me. Assumes that
     *  board.whoseMove() == myColor and that !board.gameOver(). */
    abstract Piece next();

    /** The game I am playing in. */
    private final Game _game;
    /** The color of my pieces. */
    private final PieceColor _myColor;

}
