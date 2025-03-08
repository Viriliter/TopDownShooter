package topdownshooter.Panels;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public abstract class AbstractActionPanel extends JPanel {
    private int alpha = 0;

    public AbstractActionPanel() {
        setOpaque(false);
        setVisible(false);
    }

    public void fadeIn() {
        setVisible(true);
    
        new Timer(50, new ActionListener() {
    
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));

        // Paint the background with transparency
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g2d); // Let the panel draw its components
        g2d.dispose();
    }
}
