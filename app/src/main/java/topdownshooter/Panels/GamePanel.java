/*
 * @file GamePanel.java
 * @brief This file defines the `GamePanel` class.
 *
 * Created on Wed Mar 19 2025
 *
 * @copyright MIT License
 *
 * Copyright (c) 2025 Mert LIMONCUOGLU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

import topdownshooter.Core.ConfigHandler.WindowProperties;
import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;

/**
 * @class GamePanel
 * @brief A panel that manages the main gameplay interface, and other overlays.
 * 
 * The `GamePanel` class is responsible for managing and displaying components in the game, 
 * such as the game area, game information, in-game menu, notifications, and the game over screen. It is the core panel for the gameplay interface.
 * 
 * The panel contains a `GameAreaPanel`, which is where the game is played, a `GameInfoPanel` that shows player statistics,
 * a `NotificationPanel` for displaying notifications, and `GameOverPanel` for handling the end of the game.
 */
public class GamePanel extends JPanel {
    private MenuPanel parentPanel = null;               //*< The parent MenuPanel that holds the main menu. */
    private JFrame frame;                               //*< The parent JFrame that holds this panel. */
    private GameAreaPanel gameAreaPanel = null;         //*< The panel where the game is played. */
    private GameInfoPanel gameInfoPanel = null;         //*< The panel displaying player health, inventory, and level info. */
    private GameOverPanel gameOverPanel = null;         //*< The panel displayed when the game is over. */
    private InGameMenuPanel inGameMenuPanel = null;     //*< The panel displayed when the game is paused. */
    private NotificationPanel notificationPanel = null; //*< The panel displaying notifications about new waves. */

    /**
     * Constructs the GamePanel and sets up its layout with various sub-panels.
     * 
     * @param frame The parent JFrame that holds the game panel.
     * @param config The configuration handler that provides window properties.
     */
    public GamePanel(JFrame frame, ConfigHandler config) {
        this.frame = frame;
        setLayout(new BorderLayout());

        setBackground(Color.ORANGE);
        
        WindowProperties windowProperties = config.getWindowProperties();

        // Create Game Info Panel which shows player health inventory, level info, etc. 
        this.gameInfoPanel = new GameInfoPanel(config);
        this.gameInfoPanel.setParentPanel(this);
        //this.gameInfoPanel.setPreferredSize(new Dimension(windowProperties.windowWidth(), 100));

        add(this.gameInfoPanel, BorderLayout.NORTH);

        // Create Layered Pane for Game Area and GameOver Panel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(windowProperties.windowWidth(), windowProperties.windowHeight()-(int)this.gameInfoPanel.getPreferredSize().getHeight()));

        // Create Game Area Panel which where game is played. 
        this.gameAreaPanel = new GameAreaPanel(config);
        this.gameAreaPanel.setBounds(0, 0, (int) layeredPane.getPreferredSize().getWidth(), (int) layeredPane.getPreferredSize().getHeight());
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

        this.gameAreaPanel.startGame();
    }

    /**
     * Gets the GameAreaPanel for managing the game area.
     * 
     * @return The GameAreaPanel used to play the game.
     */
    public GameAreaPanel getGameAreaPanel() {
        return this.gameAreaPanel;
    }

    /**
     * Gets the GameInfoPanel for managing the player's information.
     * 
     * @return The GameInfoPanel displaying player health, inventory, and level info.
     */
    public GameInfoPanel getGameInfoPanel() {
        return this.gameInfoPanel;
    }

    /**
     * Gets the GameOverPanel for managing the game over screen.
     * 
     * @return The GameOverPanel displayed when the game ends.
     */
    public GameOverPanel getGameOverPanel() {
        return this.gameOverPanel;
    }

    /**
     * Gets the InGameMenuPanel for managing the pause menu.
     * 
     * @return The InGameMenuPanel displayed when the game is paused.
     */
    public InGameMenuPanel getInGameMenuPanel() {
        return this.inGameMenuPanel;
    }

    /**
     * Gets the NotificationPanel for managing in-game notifications.
     * 
     * @return The NotificationPanel displayed for new wave notifications.
     */
    public NotificationPanel getNotificationPanel() {
        return this.notificationPanel;
    }

    /**
     * Sets the parent MenuPanel for the GamePanel to handle transitions.
     * 
     * @param panel The MenuPanel to set as the parent.
     */
    public void setParentPanel(MenuPanel panel) {
        this.parentPanel = panel;
    }

    /**
     * Switches to the main menu by exiting the game and displaying the menu.
     */
    public void switchToMenu() {
        exitGame();
        this.parentPanel.showMenu();
    }

    /**
     * Starts the game by initializing the game area.
     */
    public void startGame() {
        this.gameAreaPanel.startGame();
    }

        /**
     * Loads the game state from a file input stream.
     * 
     * @param inputStream The input stream from which to load the saved game state.
     * @throws IOException If there is an error reading the input stream.
     * @throws ClassNotFoundException If the class required for deserialization cannot be found.
     */
    public void loadGame(FileInputStream inputStream) throws IOException, ClassNotFoundException {
        this.gameAreaPanel.loadGame(inputStream);
        this.gameAreaPanel.startGame();
    }

    /**
     * Exits the game by calling the game area's exit method.
     */
    public void exitGame() {
        this.gameAreaPanel.exit();
    }
}
