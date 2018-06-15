package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.function.Consumer;

import static game.PieceColor.*;
import static game.Game.State.*;
import static game.Command.Type.*;


public class Game {

    /** States of play. */
    static enum State {
        SETUP, PLAYING;
    }

    Game(Board board, Source source) {
        _board = board;
        _inputs.addSource(source);
    }

    /** Run a session of Qirkat gaming. */
    void process() {
        Player white, black;

        doClear(null);
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

    /** Perform the next command from our input source. */
    void doCommand() {
        Command c = Command.parseCommand(_inputs.getline("Gomoku: "));
        _commands.get(c.commandType()).accept(c.operands());
    }

    /** Perform the command 'auto OPERANDS[0]'. */
    void doAuto(String[] operands) {
        _state = SETUP;
        if (operands[0].toLowerCase().equals("white")) {
            _whiteIsManual = false;
        } else if (operands[0].toLowerCase().equals("black")) {
            _blackIsManual = false;
        }
    }

    /** Perform the command 'manual OPERANDS[0]'. */
    void doManual(String[] operands) {
        _state = SETUP;
        if (operands[0].toLowerCase().equals("white")) {
            _whiteIsManual = true;
        } else if (operands[0].toLowerCase().equals("black")) {
            _blackIsManual = true;
        }
    }

    /** Perform a 'help' command. */
    void doHelp(String[] unused) {
        InputStream helpin =
            Game.class.getClassLoader().getResourceAsStream("game/help.txt");
        if (helpin == null) {
            System.err.println("No help available.");
        } else {
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(helpin));
                while (true) {
                    String line = r.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
                r.close();
            } catch (IOException e) {
                /* Ignore IOException */
            }

        }
    }

    /** Perform the command 'start'. */
    void doStart(String[] unused) {
        _state = PLAYING;
    }

    /** Exit the program. */
    void doQuit(String[] unused) {
        System.exit(0);
    }

    /** Perform the command 'clear'. */
    void doClear(String[] unused) {
        _board.clear();
        _state = SETUP;
    }

    /** Perform the command 'print'. */
    void doPrint(String[] unused) {
        System.out.println("===");
        System.out.println(_board.toString());
        System.out.println("===");
    }

    /** Execute the artificial 'error' command. */
    void doError(String[] unused) {
        System.err.println("Command not understood");
    }

    /** Perform the command 'Status'. */
    void doStatus(String[] unused) {
        System.out.println("===");


        System.out.println("===");
    }

    /** Report the outcome of the current game. */
    void reportWinner() {

    }

    /** Return a read-only view of my game board. */
    Board board() {
        return _board;
    }

    /** Mapping of command types to methods that process them. */
    private final HashMap<Command.Type, Consumer<String[]>> _commands =
            new HashMap<>();
    {
        _commands.put(AUTO, this::doAuto);
        _commands.put(MANUAL, this::doManual);
        _commands.put(CLEAR, this::doClear);
        _commands.put(PRINT, this::doPrint);
        _commands.put(HELP, this::doHelp);
        _commands.put(STATUS, this::doStatus);
        _commands.put(START, this::doStart);
        _commands.put(QUIT, this::doQuit);
        _commands.put(ERROR, this::doError);

    }

    /** My board. */
    private Board _board;

    /** Indicate whether players are manual players (as opposed to AIs). */
    private boolean _whiteIsManual, _blackIsManual;

    /** Current game state. */
    private State _state;

    /** Input source. */
    private final CommandSource _inputs = new CommandSource();
}
