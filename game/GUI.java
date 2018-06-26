package game;

import ucb.gui2.LayoutSpec;
import ucb.gui2.TopLevel;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;

public class GUI extends TopLevel implements Observer{

    GUI(String title, Board board) {
        super(title, true);
        _board = board;
        _paint = new Paint(board);
        addButton();
        add(_paint, new LayoutSpec("ileft", 5, "itop", 5, "iright", 5, "ibottom", 5));
        _paint.setKeyHandler("keypress", this::keyPressed);
        _paint.setMouseHandler("click", this::mouseClicked);
        _paint.addObserver(this);
        _board.addObserver(this);
    }

    private void addButton() {
        addMenuButton("Game->Quit",  this::quit);
        addMenuButton("Game->New", this::clear);
        addMenuButton("Game->Start",  this::quit);
        addMenuButton("Edit->Undo", this::undo);
        addMenuButton("Edit->Auto->Black", this::auto1);
        addMenuButton("Edit->Auto->White", this::auto2);
        addMenuButton("Edit->Manual->Black", this::manual1);
        addMenuButton("Edit->Manual->White", this::manual2);
    }

    /** Return the key, take it from the queue. */
    String readkey() {
        try {
            return _pendingKeys.take();
        } catch (InterruptedException e) {
            throw new Error("unexpected interrupt");
        }
    }

    /** Respond to the key press by queuing it to the queue. */
    private void keyPressed(String s, KeyEvent e) {
        _pendingKeys.offer(e.getKeyText(e.getKeyCode()));
    }

    /** Respond to the mouse-clicking event. */
    private void mouseClicked(String s, MouseEvent e) {
        int x = e.getX(), y = e.getY();
        System.out.println(x);
        System.out.println(y);
    }

    /** Response to "Quit" button click. */
    private synchronized void quit(String unused) {
        _pendingKeys.offer("quit");
        _paint.requestFocusInWindow();
    }

    /** Response to "Start" button click. */
    private synchronized void start(String unused) {
        _pendingKeys.offer("start");
        _paint.requestFocusInWindow();
    }

    /** Response to "New" button click. */
    private synchronized void clear(String unused) {
        _pendingKeys.offer("clear");
        _paint.requestFocusInWindow();
    }

    /** Response to "Undo" button click. */
    private synchronized void undo(String unused) {
        _pendingKeys.offer("undo");
        _paint.requestFocusInWindow();
    }

    private synchronized void auto1(String unused) {
        _pendingKeys.offer("auto black");
        _paint.requestFocusInWindow();
    }

    private synchronized void auto2(String unused) {
        _pendingKeys.offer("auto white");
        _paint.requestFocusInWindow();
    }

    private synchronized void manual1(String unused) {
        _pendingKeys.offer("manual black");
        _paint.requestFocusInWindow();
    }

    private synchronized void manual2(String unused) {
        _pendingKeys.offer("manual white");
        _paint.requestFocusInWindow();
    }

    @Override
    public void update(Observable obs, Object arg) {
    }

    /** Decorations of the board. */
    private Paint _paint;

    /** Current playing board. */
    private Board _board;

    /** Queue of pending key presses. */
    private ArrayBlockingQueue<String> _pendingKeys =
            new ArrayBlockingQueue<>(5);
}
