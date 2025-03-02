package topdownshooter.Panels;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class AbstractActionPanel extends JPanel {

    public AbstractActionPanel() {
        setOpaque(false);
        setVisible(false);
    }

    public void show() {
        setVisible(true);
    
        new Timer(50, new ActionListener() {
            private int alpha = 0;
    
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha < 150) {
                    alpha += 10;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();

        repaint(); // Redraw panel when game over
    }
}
