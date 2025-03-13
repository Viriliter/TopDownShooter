package topdownshooter.Panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;
import javax.swing.Timer;

public class NotificationPanel extends JPanel {
    private String message;
    private JPanel parentPanel = null;

    public NotificationPanel(JPanel parentPanel, String message) {
        this.parentPanel = parentPanel;
        this.message = message;

        setOpaque(false);
        setVisible(false);
        setPreferredSize(new Dimension(300, 60));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable Anti-aliasing for smoother corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Semi-transparent dark background with rounded corners
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));

        // Draw Text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(message)) / 2;
        int textY = (getHeight() + fm.getAscent()) / 2 - 5;
        g2.drawString(message, textX, textY);

        g2.dispose();
    }

    public void show(int displayTimeMs) {
        int panelX = (parentPanel.getWidth() - getWidth()) / 2; // Horizontal center
        setBounds(panelX, 20, getWidth(), getHeight()); // Position at the top center

        // Add to parent panel
        parentPanel.setLayout(null);
        parentPanel.add(this);
        parentPanel.revalidate();
        parentPanel.repaint();

        // Timer to remove the panel after displayTime milliseconds
        Timer timer = new Timer(displayTimeMs, e -> {
            parentPanel.remove(NotificationPanel.this);
            parentPanel.revalidate();
            parentPanel.repaint();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
