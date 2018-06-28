package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.function.BiConsumer;
import javax.swing.JComponent;

public class Decorate extends Observable{
    private final HashMap<String, BiConsumer<String, MouseEvent>> mouseEventMap = new HashMap();

    Decorate() {
        me = new Decorate.PadComponent();
        me.setFocusable(true);
    }

    void setPreferredSize(int width, int height) {
        preferredSize = new Dimension(width, height);
        me.invalidate();
    }

    /** Repaint. */
    void repaint() {
        me.repaint();
    }

    /** Paint all the decorations. */
    void paintComponent(Graphics2D g) {}

    void setMouseHandler(BiConsumer<String, MouseEvent> func) {
        mouseEventMap.put("click", func);
        if (me.getMouseListeners().length == 0) {
            me.addMouseListener((MouseListener)this.me);
        }
    }

    private class PadComponent extends JComponent implements MouseListener {
        private PadComponent() { }

        public void paintComponent(Graphics g) {
            Decorate.this.paintComponent((Graphics2D)g);
        }

        public void mouseClicked(MouseEvent e) {
            mouseEventMap.get("click").accept("click", e);
        }

        public void mouseReleased(MouseEvent e) { }

        public void mousePressed(MouseEvent e) { }

        public void mouseEntered(MouseEvent e) { }

        public void mouseExited(MouseEvent e) { }

        public Dimension getPreferredSize() {
            return preferredSize;
        }
    }

    void requestFocusInWindow() {
        me.requestFocusInWindow();
    }

    /** Preferred size of the board. */
    private Dimension preferredSize;

    /** All components of me. */
    JComponent me;
}