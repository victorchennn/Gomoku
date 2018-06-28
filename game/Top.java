package game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.function.Consumer;
import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Top extends Observable implements ActionListener {
    private final HashMap<String, Top.ButtonHandler> buttonMap = new HashMap();
    private final JFrame frame;

    void display() {
        this.frame.pack();
        this.frame.setVisible(true);
    }

    Top(String title, boolean exitOnClose) {
        this.frame = new JFrame(title);
        this.frame.setUndecorated(true);
        this.frame.getRootPane().setWindowDecorationStyle(1);
        this.frame.setLayout(new GridBagLayout());
        if (exitOnClose) {
            this.frame.setDefaultCloseOperation(3);
        }
    }

    void addMenuButton(String label, Consumer<String> func) {
        String[] names = label.split("->");
        JMenu menu = this.getMenu(names, names.length - 2);
        JMenuItem item = new JMenuItem(names[names.length - 1]);
        item.setActionCommand(label);
        item.addActionListener(this);
        menu.add(item);
        this.buttonMap.put(label, new Top.ButtonHandler(func, label, item));
    }

    void add(Decorate widget, GridBagConstraints constraints) {
        this.frame.add(widget.me, constraints);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof AbstractButton) {
            String key = e.getActionCommand();
            Top.ButtonHandler h = (Top.ButtonHandler)this.buttonMap.get(key);
            if (h == null) {
                return;
            }
            h.doAction();
        }
    }

    private JMenu getMenu(String[] label, int last) {
        if (this.frame.getJMenuBar() == null) {
            this.frame.setJMenuBar(new JMenuBar());
        }
        JMenuBar bar = this.frame.getJMenuBar();
        JMenu menu = null;
        int k;
        for(k = 0; k < bar.getMenuCount(); ++k) {
            menu = bar.getMenu(k);
            if (menu.getText().equals(label[0])) {
                break;
            }
            menu = null;
        }
        if (menu == null) {
            menu = new JMenu(label[0]);
            bar.add(menu);
        }
        for(k = 1; k <= last; ++k) {
            JMenu menu0 = menu;
            menu = null;
            for(int i = 0; i < menu0.getItemCount(); ++i) {
                JMenuItem item = menu0.getItem(i);
                if (item != null) {
                    if (item.getText().equals(label[k])) {
                        if (!(item instanceof JMenu)) {
                            throw new IllegalStateException("inconsistent menu label");
                        }
                        menu = (JMenu)item;
                        break;
                    }
                    menu = null;
                }
            }
            if (menu == null) {
                menu = new JMenu(label[k]);
                menu0.add(menu);
            }
        }
        return menu;
    }

    private static class ButtonHandler {
        private Consumer<String> _func;
        private String _id;
        private AbstractButton _src;
        ButtonHandler(Consumer<String> func, String id, AbstractButton src) {
            this._src = src;
            this._id = id;
            this._func = func;
        }
        void doAction() {
            if (this._func != null) {
                this._func.accept(this._id);
            }
        }
    }
}