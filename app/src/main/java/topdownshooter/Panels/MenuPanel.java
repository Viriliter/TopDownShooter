package topdownshooter.Panels;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;

import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Core.ConfigHandler.WindowProperties;

public class MenuPanel extends JPanel {
    private JFrame frame;
    private ConfigHandler config = null;
    private Image bgImage;

    public MenuPanel(JFrame frame, ConfigHandler config) {
        this.frame = frame;
        this.config = config;

        initPanel();
    }

    private void initPanel() {
        setLayout(new BorderLayout());

        // Get window properties from config
        WindowProperties windowProperties = this.config.getWindowProperties();

        // Load and resize background image
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("wallpaper.jpg");
            if (input != null) {
                Image image = ImageIO.read(input);  // Load the image
                this.bgImage = image.getScaledInstance(windowProperties.windowWidth(), windowProperties.windowHeight(), Image.SCALE_SMOOTH);  // Resize the image
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
        exitButton.addActionListener(e -> exitGame());
        
        // Add buttons to the panel
        buttonPanel.add(Box.createVerticalGlue()); // Push buttons to center
        buttonPanel.add(startButton);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (this.bgImage != null) {
            g.drawImage(this.bgImage, 0, 0, this);
        }

        frame.repaint();
    }

    public void showMenu() {
        this.frame.getContentPane().removeAll();  // It will clear out if there is any GamePanel

        MenuPanel menuPanel = new MenuPanel(this.frame, config);
        this.frame.add(menuPanel);
        this.frame.setVisible(true);
    }

    private void startGame() {
        frame.getContentPane().removeAll();
        GamePanel gamePanel = new GamePanel(frame, config);
        gamePanel.setParentPanel(this);
        frame.add(gamePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void loadGame() {
    
    }

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
            + "<li>Collect ammunation and medicals to get ready to next waves.</li>"
            + "</ul>"
            + "</html>";

        JEditorPane helpTextArea = new JEditorPane("text/html", helpText);
        helpTextArea.setEditable(false);  // Make sure the text area is not editable
        helpTextArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JDialog helpDialog = new JDialog(frame, "Game Help", true);
        helpDialog.setSize(500, 400);  // Set dialog size
        helpDialog.setLocationRelativeTo(frame);  // Center dialog on the main frame

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

    private void credits() {
        // Create and display a dialog with instructions on how to play the game
        String helpText = "<html><h2>Credits</h2>"
            + "<p> This top down view zombie shooter game created for BIL015 course project.</p>"
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

        JDialog helpDialog = new JDialog(frame, "Game Help", true);
        helpDialog.setSize(500, 400);  // Set dialog size
        helpDialog.setLocationRelativeTo(frame);  // Center dialog on the main frame

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

    private void exitGame() {
        // Confirm the exit with the user
        int response = JOptionPane.showConfirmDialog(frame, 
            "Are you sure you want to exit the game?", "Exit Game", 
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Perform any cleanup if necessary (e.g., saving game state, etc.)
            System.exit(0); // Close the game
        }
    }
}
