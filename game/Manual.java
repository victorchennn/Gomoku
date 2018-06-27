package game;

import static game.Command.Type.*;

/**
 * Manually operates a player, which means player have to type
 * position of the piece at each step to play with another
 * player, who can be also Manual or AI.
 * @author Victor
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking its pieces from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
        _prompt = myColor + ": ";
    }

    @Override
    Piece next() {
        Command command = this.game().GetPieceCommand(_prompt);
        if (command.commandType().equals(CLEAR)) {
            game().doClear(null);
            return null;
        }
        int col =  Integer.parseInt(command.operands()[0]);
        int row =  Integer.parseInt(command.operands()[2]);
        return Piece.create(myColor(), col, row);
    }

    /** Identifies the player serving as a source of input commands. */
    private String _prompt;
}
