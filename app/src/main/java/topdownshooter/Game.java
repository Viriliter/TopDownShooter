/*
 * @file Game.java
 * @brief This file defines the `Game` class which is entry point of the game.
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

package topdownshooter;

import javax.swing.*;

import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.WindowProperties;
import topdownshooter.Panels.MenuPanel;
import topdownshooter.Core.ConfigHandler;

/**
 * @class Game
 * @brief Main entry point of the game.
 *
 * This class initializes the main window and loads configurations.
 */
public class Game {
    /**
     * Main method to start the game.
     *
     * Loads game configurations, sets up the game window, 
     * and initializes the menu panel.
     *
     * @param args Command-line arguments (unused).
     */
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
