package topdownshooter;

import javax.swing.*;
import java.awt.*;

import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.WindowProperties;
import topdownshooter.Panels.GameAreaPanel;
import topdownshooter.Panels.GameInfoPanel;
import topdownshooter.Panels.GameOverPanel;
import topdownshooter.Core.ConfigHandler;

public class GamePanel extends JPanel {
    private GameAreaPanel gameAreaPanel;
    private GameInfoPanel gameInfoPanel;
    private GameOverPanel gameOverPanel;

    public GamePanel(JFrame frame, ConfigHandler config) {
        setLayout(new BorderLayout());

        setBackground(Color.ORANGE);
        
        WindowProperties windowProperties = config.getWindowProperties();

        this.gameInfoPanel = new GameInfoPanel(config);
        add(this.gameInfoPanel, BorderLayout.NORTH);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowProperties.windowWidth(), windowProperties.windowHeight()));

        this.gameAreaPanel = new GameAreaPanel(config);
        this.gameAreaPanel.setBounds(0, 0, windowProperties.windowWidth(), windowProperties.windowHeight());
        layeredPane.add(this.gameAreaPanel, JLayeredPane.DEFAULT_LAYER);

        this.gameOverPanel = new GameOverPanel();
        this.gameOverPanel.setBounds(0, 0, windowProperties.windowWidth(), windowProperties.windowHeight());
        this.gameOverPanel.setVisible(false);

        layeredPane.add(this.gameOverPanel, JLayeredPane.PALETTE_LAYER);

        add(layeredPane, BorderLayout.CENTER);

        setVisible(true);
    }
}
