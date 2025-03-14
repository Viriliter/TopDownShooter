/*
 * This source file was generated by the Gradle 'init' task
 */
package topdownshooter;

import javax.swing.*;

import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.WindowProperties;
import topdownshooter.Panels.MenuPanel;
import topdownshooter.Core.ConfigHandler;

public class Game {
    public static void main(String[] args) {
        ConfigHandler config = new ConfigHandler(Globals.CONFIGURATION_FILE);

        WindowProperties windowProperties = config.getWindowProperties();

        SwingUtilities.invokeLater(() -> {
           
            JFrame frame = new JFrame(Globals.GAME_TITLE);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(windowProperties.windowWidth(), windowProperties.windowHeight());
            frame.setResizable(false);

            // Set full-screen mode
            //GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //GraphicsDevice device = env.getDefaultScreenDevice();
            //device.setFullScreenWindow(frame);
            //
            //config.setProperty("Window", "Width", frame.getWidth());
            //config.setProperty("Window", "Height", frame.getHeight());

            MenuPanel menuPanel = new MenuPanel(frame, config);
            frame.add(menuPanel);
            frame.setVisible(true);
        });
    }
}
