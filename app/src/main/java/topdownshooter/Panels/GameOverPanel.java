package topdownshooter.Panels;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameOverPanel extends AbstractActionPanel {
    private JFrame frame;
    private JLabel gameOverLabel;
    private JLabel scoreLabel;
    private JLabel levelLabel;
    private JButton backButton;
    private GamePanel parentPanel;

    public GameOverPanel(JFrame frame) {
        super();
        this.frame = frame;
        setLayout(null); // Use absolute positions

        // Create and configure the labels
        gameOverLabel = createLabel("GAME OVER", 50, Color.RED);
        scoreLabel = createLabel("Final Score: " + 0, 30, Color.WHITE);
        levelLabel = createLabel("Reached Level: " + 1, 30, Color.WHITE);

        // Create Back to Menu button
        backButton = createButton("Back to Menu");
        backButton.addActionListener(e -> returnToMenu());

        // Add components to the panel
        add(gameOverLabel);
        add(scoreLabel);
        add(levelLabel);
        add(backButton);
    }

    private JLabel createLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setForeground(color);
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(0, 0, 0, 150)); // Black with 150 alpha (out of 255)
        g.fillRect(0, 0, getWidth(), getHeight());

        // Position the labels
        int x;

        x = (getWidth() - gameOverLabel.getPreferredSize().width) / 2;
        gameOverLabel.setBounds(x, getHeight() / 2 - 50, gameOverLabel.getPreferredSize().width, gameOverLabel.getPreferredSize().height);

        x = (getWidth() - scoreLabel.getPreferredSize().width) / 2;
        scoreLabel.setBounds(x, gameOverLabel.getY() + 60, scoreLabel.getPreferredSize().width, scoreLabel.getPreferredSize().height);

        x = (getWidth() - levelLabel.getPreferredSize().width) / 2;
        levelLabel.setBounds(x, scoreLabel.getY() + 60, levelLabel.getPreferredSize().width, levelLabel.getPreferredSize().height);
    
        // Position the button
        backButton.setBounds((getWidth() - 200) / 2, levelLabel.getY() + 70, 200, 50);

        this.frame.repaint();
    }

    void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }

    void setPlayerScore(int score) {
        this.scoreLabel.setText("Final Score: " + score);
        repaint();
    }

    void setGameLevel(int level) {
        this.levelLabel.setText("Reached Level: " + level);
        repaint();
    }

    void returnToMenu() {
        this.parentPanel.switchToMenu();
        this.close();
    }
}
