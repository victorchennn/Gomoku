package game;

import java.util.Observable;
import java.util.Stack;

public class Board extends Observable {

    Board() {

    }

    Board(Board board) {

    }

    private PieceColor _whoseMove;

    private boolean _gameover;

    private String[][] _table;

    private Stack<Piece> _log;
}
