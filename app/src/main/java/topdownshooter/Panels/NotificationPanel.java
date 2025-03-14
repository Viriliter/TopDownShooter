package topdownshooter.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class NotificationPanel extends JPanel {
    private String message = "";
    private Frame frame = null;
    private Timer timer;

    public NotificationPanel(JFrame frame) {
        super();
        this.frame = frame;
        setLayout(null); // Use absolute positions

        setOpaque(false);
        //setVisible(false);
        setPreferredSize(new Dimension(300, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));

        // Draw text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(this.message)) / 2;
        int textY = (getHeight() + fm.getAscent()) / 2 - 5;
        g2.drawString(this.message, textX, textY);

        g2.dispose();

        this.frame.repaint();
    }

    public void show(String message, int displayTimeMs) {
        this.message = message;
        setVisible(true);

        SwingUtilities.invokeLater(() -> {
            // Ensure only one timer at a time
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }

            timer = new Timer(displayTimeMs, e -> {
                setVisible(false);
            });
            timer.setRepeats(false);
            timer.start(); 
        });
    }
}
