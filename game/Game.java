package game;

import java.io.*;
import java.util.HashMap;
import java.util.function.Consumer;

import static game.PieceColor.*;
import static game.Game.State.*;
import static game.Command.Type.*;
import static game.Board.*;

/** Controls the play of the game.
 *  @author Victor
 */
public class Game {

    /** States of play. */
    enum State {
        SETUP, PLAYING;
    }

    /** A new Game, using BOARD to play on, reading initially from SOURCE. */
    Game(Board board, Source source, GUI gui) {
        _board = board;
        _inputs.addSource(source);
        _gui = gui;
    }

    /** Run a session of Gomoku gaming. */
    void process() {
        Player white, black;
        doClear(null);
        _blackIsManual = true;
        _whiteIsManual = true;
        while (true) {
            while (_state == SETUP) {
                if (_gui != null) {
                    _board.notifyObservers();
                    doCommand( _gui.readkey());
                } else {
                    doCommand(null);
                }
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
                    System.out.println(_board);
                }
            }
            if (_state == PLAYING) {
                reportWinner();
            }
        }
    }

    /** Perform the next command from our input source. */
    private void doCommand(String command) {
        Command c;
        if (command == null) {
            c = Command.parseCommand(_inputs.getline("Gomoku: "));
        } else {
            c = Command.parseCommand(command);
        }
        _commands.get(c.commandType()).accept(c.operands());
    }

    /** Get the new pieces in the board and continue the game. */
    Command GetPieceCommand(String prompt) {
        while (_state == PLAYING) {
            Command command = Command.parseCommand(_inputs.getline(prompt));
            switch (command.commandType()) {
                case PIECE:
                    return command;
                case CLEAR:
                    return command;
                default:
                    _commands.get(command.commandType()).accept(command.operands());
            }
        }
        return null;
    }

    /** Perform the command 'auto OPERANDS[0]'. */
    private void doAuto(String[] operands) {
        _state = SETUP;
        if (operands[0].toLowerCase().equals("white")) {
            _whiteIsManual = false;
        } else if (operands[0].toLowerCase().equals("black")) {
            _blackIsManual = false;
        }
    }

    /** Perform the command 'manual OPERANDS[0]'. */
    private void doManual(String[] operands) {
        _state = SETUP;
        if (operands[0].toLowerCase().equals("white")) {
            _whiteIsManual = true;
        } else if (operands[0].toLowerCase().equals("black")) {
            _blackIsManual = true;
        }
    }

    /** Perform the command 'set OPERANDS[0] OPERANDS[1]'. */
    private void doSet(String[] operands) {
        if (operands[0].equals("white")) {
            _board.clear();
            _board.setPieces(operands[1], WHITE);
        } else {
            _board.clear();
            _board.setPieces(operands[1], BLACK);
        }
        System.out.println(_board);
    }

    /** Perform the command 'save OPERANDS[0]'. */
    private void doSave(String[] operands) {
        try {
            InputStream helpIn =
                    Game.class.getClassLoader().getResourceAsStream(operands[0] + ".game");
            if (helpIn != null) {
                System.err.println("The name \"" + operands[0] +
                        "\" is already taken." +
                        " Please choose a different name.");
            } else {
                File file = new File(operands[0] + ".game");
                FileWriter fw = new FileWriter(file, true);
                String board = "";
                for (int i = 0; i < SIDE * SIDE; i++) {
                    board += _board.get(i).shortName();
                }
                fw.write(board + "\n");
                fw.write(_board.whoseMove().shortName());
                fw.flush();
                fw.close();
            }
        } catch (IOException e) {
            System.err.println("Wrong writing.");
        }
    }

    /** Perform the command 'Load OPERANDS[0]'. */
    private void doLoad(String[] operands) {
        try {
            InputStream helpIn =
                    Game.class.getClassLoader().getResourceAsStream(operands[0] + ".game");
            if (helpIn == null) {
                System.err.println("The game \"" + operands[0] +
                        "\" does not exist." );
            } else {
               FileReader fr = new FileReader(operands[0] + ".game");
               BufferedReader br = new BufferedReader(fr);
               String board = br.readLine();
               String whosemove = br.readLine();
               if (whosemove.equals("b")) {
                   _board.setPieces(board, BLACK);
               } else {
                   _board.setPieces(board, WHITE);
               }
               br.close();
            }
        } catch (IOException e) {
            System.err.println("Wrong reading.");
        }
    }

    /** Perform a 'help' command. */
    private void doHelp(String[] unused) {
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

    /** Perform a 'undo' command. */
    private void doUndo(String[] unused) {
        _board.undo();
    }

    /** Perform the command 'start'. */
    private void doStart(String[] unused) {
        _state = PLAYING;
    }

    /** Exit the program. */
    private void doQuit(String[] unused) {
        System.exit(0);
    }

    /** Perform the command 'clear'. */
    void doClear(String[] unused) {
        _board.clear();
        _whiteIsManual = true;
        _blackIsManual = true;
        _state = SETUP;
    }

    /** Perform the command 'print'. */
    private void doPrint(String[] unused) {
        System.out.println("===");
        System.out.println(_board.toString());
        System.out.println("===");
    }

    /** Execute the artificial 'error' command. */
    private void doError(String[] unused) {
        System.err.println("Command not understood");
    }

    /** Perform the command 'Status'. */
    private void doStatus(String[] unused) {
        System.out.println("===");
        System.out.println("Black: " + (_blackIsManual? "Manual":"AI"));
        System.out.println("White: " + (_whiteIsManual? "Manual":"AI"));
        System.out.println("===");
    }

    /** Report the outcome of the current game. */
    private void reportWinner() {
        if (_board.gameOver()) {
            _state = SETUP;
            if (_board.whoseMove() == BLACK) {
                System.out.println("White wins.");
            } else {
                System.out.println("Black wins.");
            }
        }
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
        _commands.put(SETBOARD, this::doSet);
        _commands.put(START, this::doStart);
        _commands.put(SAVE, this::doSave);
        _commands.put(LOAD, this::doLoad);
        _commands.put(QUIT, this::doQuit);
        _commands.put(UNDO, this::doUndo);
        _commands.put(ERROR, this::doError);
    }

    private GUI _gui;

    /** My board. */
    private Board _board;

    /** Indicate whether players are manual players (as opposed to AIs). */
    private boolean _whiteIsManual, _blackIsManual;

    /** Current game state. */
    private State _state;

    /** Input source. */
    private final CommandSource _inputs = new CommandSource();
}
