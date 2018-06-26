package game;

import ucb.gui2.Pad;

import java.awt.Graphics2D;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import static game.Board.*;
import static game.PieceColor.*;

class Paint extends Pad implements Observer {

    Paint(Board board) {
        _board = board;
        _board.addObserver(this);
        setPreferredSize(DIM, DIM);
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setColor(BLANK_COLOR);
        g.fillRect(0, 0, DIM, DIM);
        g.setColor(BAR_COLOR);
        for (int i = SQDIM / 2; i <= DIM; i += SEP) {
            g.fillRect(SQDIM / 2, i, LENGTH, BAR);
            g.fillRect(i, SQDIM / 2, BAR, LENGTH);
        }
        for (int i = 0; i <= MAX_INDEX; i++) {
            if (_board.get(i).isPiece()) {
                if (_board.get(i) == BLACK) {
                    g.setColor(BLACK_COLOR);
                } else {
                    g.setColor(WHITE_COLOR);
                }
                g.fillOval(col(i) * SEP,LENGTH - row(i) * SEP, DIAMETER, DIAMETER);
            }
        }
    }

    @Override
    public synchronized void update(Observable model, Object arg) {
        repaint();
    }

    /** Colors. */
    private static final Color
            BLANK_COLOR = new Color(253, 245, 230),
            BAR_COLOR = new Color(140, 140, 140),
            WHITE_COLOR = Color.WHITE,
            BLACK_COLOR = Color.BLACK;

    /** Length of sides and lines. */
    private static final int
            SQDIM = 50,
            BAR = 1,
            DIAMETER = SQDIM - BAR,
            SEP = SQDIM + BAR,
            DIM = SEP * SIDE + BAR,
            LENGTH = DIM - SEP;

    /** Board being displayed. */
    private Board _board;

}
