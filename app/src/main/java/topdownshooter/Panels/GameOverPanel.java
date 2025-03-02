package topdownshooter.Panels;

import java.awt.*;

public class GameOverPanel extends AbstractActionPanel {
    public GameOverPanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Darken the background with transparency
        g.setColor(new Color(0, 0, 0, 150)); // Black with 150 alpha (out of 255)
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw "Game Over" text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String message = "GAME OVER";
        int x = (getWidth() - g.getFontMetrics().stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);
    }
}
