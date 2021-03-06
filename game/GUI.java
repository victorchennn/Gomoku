package game;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Observer;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import static game.Paint.*;
import static game.Board.*;

public class GUI extends Top implements Observer{

    GUI(String title, Board board, boolean start) {
        super(title, true);
        _board = board;
        _paint = new Paint(board);
        _start = start;
        _signature = "      Author: Victor Chen";
        addButton();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        add(_paint, constraints);

        GridBagConstraints labeline = new GridBagConstraints();
        labeline.gridy = 1;
        addLabel("Time", labeline);

        setLabel(getTime() + _signature);
        Timer timer = new Timer();
        timer.schedule(new StopWatch(), 0, 1000);

        _paint.setMouseHandler(this::click);
        _paint.addObserver(this);
        _board.addObserver(this);


    }

    private void addButton() {
        addMenuButton("Gobang->Start",  this::start);
        addMenuButton("Gobang->Quit",  this::quit);
        addMenuButton("Game->New", this::clear);
        addMenuButton("Game->Load", this::load);
        addMenuButton("Game->Save", this::save);
        addMenuButton("Game->Status",  this::status);
        addMenuButton("Edit->Undo", this::undo);
        addMenuButton("Edit->Auto->Black", this::auto1);
        addMenuButton("Edit->Auto->White", this::auto2);
        addMenuButton("Edit->Manual->Black", this::manual1);
        addMenuButton("Edit->Manual->White", this::manual2);
        addMenuButton("Help", this::help);
    }

    /** Return the key, take it from the queue. */
    String readkey() {
        try {
            return _pendingKeys.take();
        } catch (InterruptedException e) {
            throw new Error("unexpected interrupt");
        }
    }

    /** Respond to the mouse-clicking event. */
    private void click(String s, MouseEvent e) {
        if (_start) {
            int x = e.getX() / SQDIM;
            int y = e.getY() / SQDIM;
            String row = String.valueOf(SIDE - y);
            String col = String.valueOf(1 + x);
            _pendingKeys.offer(row + "," + col);
            _paint.requestFocusInWindow();
        }
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

    /** Response to "Help" button click. */
    private synchronized void help(String unused) {
        _pendingKeys.offer("help");
        _paint.requestFocusInWindow();
    }

    /** Response to "Load" button click. */
    private synchronized void load(String unused) {
        _pendingKeys.offer("load");
        _paint.requestFocusInWindow();
    }

    /** Response to "Save" button click. */
    private synchronized void save(String unused) {
        _pendingKeys.offer("save");
        _paint.requestFocusInWindow();
    }

    /** Response to "Status" button click. */
    private synchronized void status(String unused) {
        _pendingKeys.offer("status");
        _paint.requestFocusInWindow();
    }

    /** Get current time. */
    String getTime() {
        Calendar calendar = Calendar.getInstance();
        Date date = (Date) calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(date);
        return time;
    }

    void start(boolean ifstart) {
        _start = ifstart;
    }

    @Override
    public void update(Observable obs, Object arg) {
    }

    /** True iff game starts. */
    private boolean _start;

    /** Decorations of the board. */
    private Paint _paint;

    /** Current playing board. */
    private Board _board;

    /** Queue of pending key presses. */
    private ArrayBlockingQueue<String> _pendingKeys =
            new ArrayBlockingQueue<>(10);

    /** Used to show the time. */
    private class StopWatch extends TimerTask {
        public void run() {
            setLabel(getTime() + _signature);
        }
    }

    /** Show the author. */
    private String _signature;
}
