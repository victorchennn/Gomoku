package game;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Observable;
import java.util.function.Consumer;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.AbstractButton;

public class Top extends Observable implements ActionListener {

    void display() {
        frame.pack();
        frame.setVisible(true);
    }

    Top(String title, boolean exitOnClose) {
        buttonMap = new HashMap<>();
        frame = new JFrame(title);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(1);
        frame.setLayout(new GridBagLayout());
        if (exitOnClose) {
            frame.setDefaultCloseOperation(3);
        }
    }

    void addMenuButton(String label, Consumer<String> func) {
        String[] names = label.split("->");
        JMenu menu = getMenu(names, names.length - 2);
        JMenuItem item = new JMenuItem(names[names.length - 1]);
        item.setActionCommand(label);
        item.addActionListener(this);
        menu.add(item);
        buttonMap.put(label, func);
    }

    void add(Decorate decorate, GridBagConstraints constraints) {
        frame.add(decorate.me, constraints);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof AbstractButton) {
            String key = e.getActionCommand();
            Consumer h = buttonMap.get(key);
            h.accept(key);
        }
    }

    private JMenu getMenu(String[] label, int last) {
        if (frame.getJMenuBar() == null) {
            frame.setJMenuBar(new JMenuBar());
        }
        JMenuBar bar = frame.getJMenuBar();
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

    private final HashMap<String, Consumer<String>> buttonMap;
    private final JFrame frame;
}