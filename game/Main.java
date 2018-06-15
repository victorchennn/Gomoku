package game;

import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        Board board = new Board();
        Game game = new Game(board,
                new ReaderSource(new InputStreamReader(System.in), true));
        game.process();
    }
}
