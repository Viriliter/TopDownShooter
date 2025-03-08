package topdownshooter.Panels;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class GameOverPanel extends AbstractActionPanel {
    private int playerScore = 0;
    private int gameLevel = 1;
    private JButton backButton;
    private GamePanel parentPanel;

    public GameOverPanel() {
        super();
        setLayout(null); // Use absolute positions

        // Create Back to Menu button
        backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 20));
        backButton.setBounds( // Centering the button
                200, 300, 200, 50
        );
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMenu();
            }
        });

        add(backButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Darken the background with transparency
        g.setColor(new Color(0, 0, 0, 150)); // Black with 150 alpha (out of 255)
        g.fillRect(0, 0, getWidth(), getHeight());

        // "Game Over" text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        String message = "GAME OVER";
        int x = (getWidth() - g.getFontMetrics().stringWidth(message)) / 2;
        int y = getHeight() / 2;
        g.drawString(message, x, y);

        // Player score
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.WHITE);
        String scoreText = "Final Score: " + playerScore;
        x = (getWidth() - g.getFontMetrics().stringWidth(scoreText)) / 2;
        g.drawString(scoreText, x, y + 50);

        // Game level
        String levelText = "Reached Level: " + gameLevel;
        x = (getWidth() - g.getFontMetrics().stringWidth(levelText)) / 2;
        g.drawString(levelText, x, y + 100);

        backButton.setBounds((getWidth() - 200) / 2, y + 150, 200, 50);
    }

    void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }

    void setPlayerScore(int score) {
        this.playerScore = score;
        repaint();
    }

    void setGameLevel(int level) {
        this.gameLevel = level;
        repaint();
    }

    void returnToMenu() {
        this.parentPanel.switchToMenu();
        this.setVisible(false);
    }
}
