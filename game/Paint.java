package game;

import ucb.gui2.Pad;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
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
        if (_end) {
            g.setFont(GAME_OVER);
            g.setColor(GAME_OVER_COLOR);
            FontMetrics m1 = g.getFontMetrics();
            g.drawString("GAME OVER", (DIM
                            - m1.stringWidth("GAME OVER")) / 2,
                    (2 * DIM + m1.getMaxAscent()) / 4 - m1.getHeight());
            g.setFont(WIN);
            g.setColor(RED_COLOR);
            FontMetrics m2 = g.getFontMetrics();
            String win = _board.whoseMove().opposite().toString() + " WIN";
            g.drawString(win, (DIM - m2.stringWidth(win)) / 2,
                    (2 * DIM + m2.getMaxAscent()) / 4 + m2.getHeight());
        }
    }

    @Override
    public synchronized void update(Observable model, Object arg) {
        _end = _board.gameOver();
        repaint();
    }

    /** Fonts. */
    private static final Font
            GAME_OVER = new Font("Game Over", 1, 64),
            WIN = new Font("WIN", 2, 50);

    /** Colors. */
    private static final Color
            BLANK_COLOR = new Color(253, 245, 230),
            BAR_COLOR = new Color(140, 140, 140),
            RED_COLOR = new Color(220, 0, 0, 150),
            WHITE_COLOR = Color.WHITE,
            BLACK_COLOR = new Color(0, 0, 0, 180),
            GAME_OVER_COLOR = new Color(200, 0, 0, 64);

    /** Lengths. */
    static final int
            SQDIM = 50,
            BAR = 1,
            DIAMETER = SQDIM - BAR,
            SEP = SQDIM + BAR,
            DIM = SEP * SIDE + BAR,
            LENGTH = DIM - SEP;

    /** Board being displayed. */
    private Board _board;

    /** True iff "GAME OVER" message is being displayed. */
    private boolean _end;
}
