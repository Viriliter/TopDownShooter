package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;

import topdownshooter.Core.ConfigHandler.WindowProperties;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;

public class GamePanel extends JPanel {
    private MenuPanel parentPanel = null;
    private JFrame frame; 
    private GameAreaPanel gameAreaPanel = null;
    private GameInfoPanel gameInfoPanel = null;
    private GameOverPanel gameOverPanel = null;
    private InGameMenuPanel inGameMenuPanel = null;
    private NotificationPanel notificationPanel = null;

    public GamePanel(JFrame frame, ConfigHandler config) {
        this.frame = frame;
        setLayout(new BorderLayout());

        setBackground(Color.ORANGE);
        
        WindowProperties windowProperties = config.getWindowProperties();

        // Create Game Info Panel which shows player health inventory, level info, etc. 
        this.gameInfoPanel = new GameInfoPanel(config);
        this.gameInfoPanel.setParentPanel(this);
        this.gameInfoPanel.setPreferredSize(new Dimension(windowProperties.windowWidth(), 100));

        add(this.gameInfoPanel, BorderLayout.NORTH);

        // Create Layered Pane for Game Area and GameOver Panel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowProperties.windowWidth(), windowProperties.windowHeight()-this.gameInfoPanel.getHeight()));

        // Create Game Area Panel which where game is played. 
        this.gameAreaPanel = new GameAreaPanel(config);
        this.gameAreaPanel.setBounds(0, 0, windowProperties.windowWidth(), windowProperties.windowHeight());
        this.gameAreaPanel.setParentPanel(this);
        layeredPane.add(this.gameAreaPanel, JLayeredPane.DEFAULT_LAYER);

        // Create Notification Panel which is visible only if new wave is coming
        this.notificationPanel = new NotificationPanel(this.frame);
        this.notificationPanel.setBounds((windowProperties.windowWidth()-Globals.NOTIFICATION_WINDOW_WIDTH)/2, 20, Globals.NOTIFICATION_WINDOW_WIDTH, Globals.NOTIFICATION_WINDOW_HEIGHT);
        this.notificationPanel.setVisible(false);
        layeredPane.add(this.notificationPanel, JLayeredPane.PALETTE_LAYER);
        
        // Create GameOver Panel which is visible only if game is over
        this.gameOverPanel = new GameOverPanel(this.frame);
        this.gameOverPanel.setBounds(0, 0, windowProperties.windowWidth(), windowProperties.windowHeight());
        this.gameOverPanel.setVisible(false);
        this.gameOverPanel.setParentPanel(this);
        layeredPane.add(this.gameOverPanel, JLayeredPane.PALETTE_LAYER);

        // Create InGameMenu Panel which is visible only if game is paused
        this.inGameMenuPanel = new InGameMenuPanel(this.frame);
        this.inGameMenuPanel.setBounds(0, 0, windowProperties.windowWidth(), windowProperties.windowHeight());
        this.inGameMenuPanel.setVisible(false);
        this.inGameMenuPanel.setParentPanel(this);
        layeredPane.add(this.inGameMenuPanel, JLayeredPane.PALETTE_LAYER);

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

    public InGameMenuPanel getInGameMenuPanel() {
        return this.inGameMenuPanel;
    }

    public NotificationPanel getNotificationPanel() {
        return this.notificationPanel;
    }

    public void setParentPanel(MenuPanel panel) {
        this.parentPanel = panel;
    }

    public void switchToMenu() {
        this.parentPanel.showMenu();
    }

}
