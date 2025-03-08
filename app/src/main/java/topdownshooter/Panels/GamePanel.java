package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;

import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.WindowProperties;
import topdownshooter.Core.ConfigHandler;

public class GamePanel extends JPanel {
    private MenuPanel parentPanel = null;
    private GameAreaPanel gameAreaPanel = null;
    private GameInfoPanel gameInfoPanel = null;
    private GameOverPanel gameOverPanel = null;

    public GamePanel(JFrame frame, ConfigHandler config) {
        setLayout(new BorderLayout());

        setBackground(Color.ORANGE);
        
        WindowProperties windowProperties = config.getWindowProperties();

        // Create Game Info Panel which shows player health inventory, level info, etc. 
        this.gameInfoPanel = new GameInfoPanel(config);
        this.gameInfoPanel.setParentPanel(this);

        add(this.gameInfoPanel, BorderLayout.NORTH);

        // Create Layered Pane for Game Area and GameOver Panel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowProperties.windowWidth(), windowProperties.windowHeight()));

        // Create Game Area Panel which where game is played. 
        this.gameAreaPanel = new GameAreaPanel(config);
        this.gameAreaPanel.setBounds(0, 0, windowProperties.windowWidth(), windowProperties.windowHeight());
        this.gameAreaPanel.setParentPanel(this);
        layeredPane.add(this.gameAreaPanel, JLayeredPane.DEFAULT_LAYER);

        // Create GameOver Panel which is visible if game is over
        this.gameOverPanel = new GameOverPanel();
        this.gameOverPanel.setBounds(0, 0, windowProperties.windowWidth(), windowProperties.windowHeight());
        this.gameOverPanel.setVisible(false);
        this.gameOverPanel.setParentPanel(this);
        layeredPane.add(this.gameOverPanel, JLayeredPane.PALETTE_LAYER);
        
        add(layeredPane, BorderLayout.CENTER);

        setVisible(true);
    }

    public GameAreaPanel getGameAreaPanel() {
        return this.gameAreaPanel;
    }

    public GameInfoPanel getGameInfoPanel() {
        return this.gameInfoPanel;
    }

    public GameOverPanel getGameOverPanel() {
        return this.gameOverPanel;
    }

    public void setParentPanel(MenuPanel panel) {
        this.parentPanel = panel;
    }

    public void switchToMenu() {
        this.parentPanel.showMenu();
    }

}
