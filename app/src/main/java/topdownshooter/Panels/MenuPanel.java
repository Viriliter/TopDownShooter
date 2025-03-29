/*
 * @file MenuPanel.java
 * @brief This file defines the `MenuPanel` class.
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

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Core.SoundFX;

/**
 * @class MenuPanel
 * @brief A panel that displays the main menu for the game.
 * 
 * This class represents the main menu of the game, where users can interact with various options, including starting a new game, loading a saved game, viewing help, checking credits, and exiting the game. The panel features a background image, music, and several styled buttons for navigation.
 * 
 * @note This panel should be added to a JFrame to function as part of the game interface.
 */
public class MenuPanel extends JPanel {
    private JFrame frame;                   //*< The parent JFrame that holds this panel. */
    private ConfigHandler config = null;    //*< The configuration handler for game settings. */
    private Image bgImage = null;           //*< The background image for the menu. */
    private SoundFX menuMusic = null;       //*< The sound effects or music for the menu. */
    private GamePanel gamePanel = null;     //*< The GamePanel for transitioning to the game view. */

    /**
     * Constructs a MenuPanel with the specified parent JFrame and configuration handler.
     * 
     * @param frame The parent JFrame that will hold the menu panel.
     * @param config The configuration handler that manages game settings.
     */
    public MenuPanel(JFrame frame, ConfigHandler config) {
        this.frame = frame;
        this.config = config;

        initPanel();
    }

    /**
     * Initializes the components of the menu panel, including setting up buttons,
     * loading the background image, and setting up the music.
     */
    private void initPanel() {
        // Initialize menu music
        this.menuMusic = new SoundFX(Globals.MENU_MUSIC_PATH);
        this.menuMusic.play(true, 5000);

        setLayout(new BorderLayout());

        // Load and resize background image
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(Globals.WALLPAPER_PATH);
            if (input != null) {
                Image image = ImageIO.read(input);  // Load the image
                this.bgImage = image.getScaledInstance(this.frame.getWidth(), this.frame.getHeight(), Image.SCALE_SMOOTH);  // Resize the image
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false); 
        buttonPanel.setPreferredSize(new Dimension(200, 300));

        // Start button
        JButton startButton = createStyledButton("Start Game");
        startButton.addActionListener(e -> startGame());

        // Start button (Brutal Game mode)
        JButton startBrutalButton = createStyledButton("Brutal Game");
        startBrutalButton.addActionListener(e -> startBrutalGame());
        
        // Load button
        JButton loadButton = createStyledButton("Load Game");
        loadButton.addActionListener(e -> loadGame());

        // Help button
        JButton helpButton = createStyledButton("Help");
        helpButton.addActionListener(e -> help());

        // Credits button
        JButton creditButton = createStyledButton("Credits");
        creditButton.addActionListener(e -> credits());
        
        // Exit button
        JButton exitButton = createStyledButton("Exit");
        exitButton.addActionListener(e -> exit());
        
        // Add buttons to the panel
        buttonPanel.add(Box.createVerticalGlue()); // Push buttons to center
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(startBrutalButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(loadButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(helpButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(creditButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createVerticalGlue()); // Push buttons to center

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);

        // Add the button panel to the center of the menu
        add(centerPanel, BorderLayout.CENTER);
    }
    
    /**
     * Creates a styled button with the specified text for the menu.
     * 
     * @param text The text to be displayed on the button.
     * @return A styled JButton for the menu.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 50));
        button.setMaximumSize(new Dimension(200, 50));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(0, 0, 0, 150));  // Semi-transparent background
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);  // Make the button flat
        button.setFocusPainted(false);   // Remove focus painting
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));  // White border with thickness 2

        final Color hoverColor = new Color(0, 0, 0, 200); // Darker hover color

        // Add mouse listener for mouse-over effect
        button.addMouseListener(new MouseAdapter() {
            private Color originalColor;

            @Override
            public void mouseEntered(MouseEvent e) {
                originalColor = button.getBackground();
                button.setBackground(hoverColor);  // Darker on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);  // Original color
            }
        });
        return button;
    }

    /**
     * Paints the components of the menu panel, including the background image.
     * 
     * @param g The Graphics object used to paint the component.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.bgImage != null) {
            g.drawImage(this.bgImage, 0, 0, this);
        }

        this.frame.repaint();
    }

    /**
     * Displays the main menu.
     */
    public void showMenu() {
        // Clear resources
        this.bgImage = null;
        this.gamePanel = null;
        clearMusic();

        // Refresh the panel
        this.removeAll();
        this.revalidate();
        this.repaint();

        // Read configuration file, it may changed on previous session
        this.config = new ConfigHandler(Globals.CONFIGURATION_FILE);

        initPanel();  // Initialize the panel

        // Add MenuPanel to the frame
        this.frame.getContentPane().removeAll();
        this.frame.add(this);

        this.frame.setVisible(true);
        this.frame.repaint();
    }

    /**
     * Starts a new game when the user clicks "Start Game" button.
     */
    private void startGame() {
        clearMusic();

        this.frame.getContentPane().removeAll();
        this.gamePanel = new GamePanel(this.frame, this.config);
        this.gamePanel.setParentPanel(this);

        this.frame.add(this.gamePanel);
        this.frame.revalidate();
        this.frame.repaint();

        this.gamePanel.startGame();
    }

    /**
     * Starts a new brutal game when the user clicks "Start Game" button.
     * 
     * It reads configuration from config-brutal.ini to demonstrate the performance of the game engine.
     */
    private void startBrutalGame() {
        clearMusic();

        ConfigHandler brutal_config = new ConfigHandler(Globals.CONFIGURATION_FILE_BRUTAL);  // Read configuration file again for brutal game mode

        this.frame.getContentPane().removeAll();
        this.gamePanel = new GamePanel(this.frame, brutal_config);
        this.gamePanel.setParentPanel(this);

        this.frame.add(this.gamePanel);
        this.frame.revalidate();
        this.frame.repaint();

        this.gamePanel.startGame();
    }

    /**
     * Loads a previously saved game when the user clicks "Load Game" button.
     */
    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Open File");

        int userSelection = fileChooser.showOpenDialog(null); // Open "Open File" dialog

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToOpen = fileChooser.getSelectedFile();

            // Read content from the selected file
            boolean isExceptionOccured = true;
            try {
                FileInputStream inputStream = new FileInputStream(fileToOpen);

                this.menuMusic.pause();
                this.gamePanel = new GamePanel(this.frame, config);
                this.gamePanel.setParentPanel(this);
                this.gamePanel.loadGame(inputStream);

                this.frame.getContentPane().removeAll();
                this.frame.add(this.gamePanel);
                this.frame.revalidate();
                this.frame.repaint();

                clearMusic();
                this.gamePanel.startGame();

                isExceptionOccured = false;                    
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "No file given!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
    
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Corrupted file!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
    
                e.printStackTrace();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Cannot read file!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
    
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    null,
                    "Undefined error occured reading the file!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
    
                e.printStackTrace();
            } finally {
                // In case of exception, return to menu window
                if (isExceptionOccured) {
                    this.gamePanel.exitGame();

                    showMenu();
                }
            }
        }   
    }

    /**
     * Opens the help dialog when the user clicks "Help" button.
     */
    private void help() {
        // Create and display a dialog with instructions on how to play the game
        String helpText = "<html><h2>How to Play the Game</h2>"
            + "<p><b>Objective:</b> The goal of the game is to shoot zombies and survive as long as possible.</p>"
            + "<p><b>Controls:</b></p>"
            + "<ul>"
            + "<li><b>Move:</b> Use WASD keys to move your character.</li>"
            + "<li><b>Shoot:</b> Aim using mouse and click to shoot your weapon.</li>"
            + "<li><b>Pause:</b> Press 'Q' to switch weapon.</li>"
            + "<li><b>Reload:</b> Press 'R' to reload your weapon.</li>"
            + "<li><b>Reload:</b> Press 'ESC' to pause the game.</li>"
            + "</ul>"
            + "<p><b>Tips:</b></p>"
            + "<ul>"
            + "<li>Collect ammunation and medicines to get ready to next waves.</li>"
            + "</ul>"
            + "</html>";

        JEditorPane helpTextArea = new JEditorPane("text/html", helpText);
        helpTextArea.setEditable(false);  // Make sure the text area is not editable
        helpTextArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JDialog helpDialog = new JDialog(this.frame, "Game Help", true);
        helpDialog.setSize(500, 400);  // Set dialog size
        helpDialog.setLocationRelativeTo(this.frame);  // Center dialog on the main frame

        helpDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpDialog.dispose(); // Close the dialog when clicked
            }
        });

        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout());
        closePanel.add(closeButton);

        helpDialog.add(closePanel, BorderLayout.SOUTH);
        helpDialog.setVisible(true);  // Show the dialog
    }

    /**
     * Opens the help dialog when the user clicks "Credit" button.
     */
    private void credits() {
        // Create and display a dialog with instructions on how to play the game
        String helpText = "<html><h2>Credits</h2>"
            + "<p> This top down view zombie shooter game created for BIL015 course project.</p>"
            + "<p> The wallpaper and intro soundtrack generated using AI tools. For more information, visit the websites: https://deepai.org/ and  https://www.mureka.ai .</p>"
            + "<p> </p>"
            + "<p> Some in game textures obtained from a ZombieV Github project (see https://github.com/johnBuffer/ZombieV).</p>"
            + "<p><b>Author:</b></p>"
            + "<ul>"
            + "<li><b>Mert LİMONCUOĞLU</b></li>"
            + "</ul>"
            + "</html>";

        JEditorPane helpTextArea = new JEditorPane("text/html", helpText);
        helpTextArea.setEditable(false);  // Make sure the text area is not editable
        helpTextArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JDialog helpDialog = new JDialog(this.frame, "Game Help", true);
        helpDialog.setSize(500, 400);  // Set dialog size
        helpDialog.setLocationRelativeTo(this.frame);  // Center dialog on the main frame

        helpDialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                helpDialog.dispose(); // Close the dialog when clicked
            }
        });

        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout());
        closePanel.add(closeButton);

        helpDialog.add(closePanel, BorderLayout.SOUTH);
        helpDialog.setVisible(true);  // Show the dialog
    }
    
    /**
     * Exits the game when the user clicks "Exit" button.
     */
    private void exit() {
        // Confirm the exit with the user
        int response = JOptionPane.showConfirmDialog(this.frame, 
            "Are you sure you want to exit the game?", "Exit Game", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Perform any cleanup if necessary (e.g., saving game state, etc.)
            System.exit(0); // Close the game
        }
    }

    /**
     * Stops the music and deallocates its resources.
     */
    private void clearMusic() {
        if (this.menuMusic != null) {
            this.menuMusic.stop();
            this.menuMusic = null;
        }
    }
}
