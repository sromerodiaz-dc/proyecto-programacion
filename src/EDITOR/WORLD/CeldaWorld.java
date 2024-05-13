package EDITOR.WORLD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CeldaWorld extends JPanel {
    public CeldaWorld() {
        setBackground(Color.DARK_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }
        });
    }
}
