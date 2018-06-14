package game;

import static game.PieceColor.*;
import static game.Game.State.*;


public class Game {

    static enum State {
        SETUP, PLAYING;
    }

    Game(Board board) {
        _board = board;
    }

    void process() {
        Player white, black;

        doClear();
        _blackIsManual = true;
        _whiteIsManual = true;

        while (true) {
            while (_state == SETUP) {
                doCommand();
            }

            reportWinner();
            black = (_blackIsManual ?
                    new Manual(this, BLACK) : new AI(this, BLACK));
            white = (_whiteIsManual ?
                    new Manual(this, WHITE) : new AI(this, WHITE));

            while(_state != SETUP && !board().gameOver()) {
                Piece piece;
                if(_board.whoseMove() == BLACK) {
                    piece = black.next();
                } else {
                    piece = white.next();
                }

                if (_state == PLAYING) {
                    _board.play(piece);
                }
            }

            if (_state == PLAYING) {
                reportWinner();
            }
        }
    }

    void doCommand() {
//        Command c = Command.parseCommand();
    }

    void doAuto() {

    }

    void doManual() {

    }

    void doClear() {
        _board.clear();
        _state = SETUP;
    }

    void reportWinner() {

    }

    /** Return a read-only view of my game board. */
    Board board() {
        return _constBoard;
    }

    private Board _board, _constBoard;

    private boolean _whiteIsManual, _blackIsManual;

    private State _state;

}
