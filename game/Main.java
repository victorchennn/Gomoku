package game;

import java.io.InputStreamReader;

/** The main program for Gomoku.
 *  @author Victor
 */
public class Main {

    /** Run the gomoku game. */
    public static void main(String[] args) {
        Board board = new Board();
        GUI gui = new GUI("Gobang", board);
        gui.display(true);
        Game game = new Game(board, new ReaderSource(new InputStreamReader(System.in), true), gui);
        game.process();
    }
}
