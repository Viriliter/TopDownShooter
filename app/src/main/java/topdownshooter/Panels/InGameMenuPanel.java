package topdownshooter.Panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class InGameMenuPanel extends AbstractActionPanel{
    private GamePanel parentPanel = null;
    private JFrame frame; 
    private JButton resumeButton = null;
    private JButton saveButton = null;
    private JButton returnMenuButton = null;

    public InGameMenuPanel(JFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(200, 300));

        // Resume button
        resumeButton = createButton("Resume Game");
        resumeButton.addActionListener(e -> resumeGame());

        // Save button
        saveButton = createButton("Save Game");
        saveButton.addActionListener(e -> saveGame());

        // Return to menu button
        returnMenuButton = createButton("Return to Menu");
        returnMenuButton.addActionListener(e -> returnToMenu());

        // Add buttons to the panel
        buttonPanel.add(Box.createVerticalGlue()); // Push buttons to center
        buttonPanel.add(resumeButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(returnMenuButton);
        buttonPanel.add(Box.createVerticalGlue()); // Push buttons to center

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(buttonPanel);

        // Add the button panel to the center of the menu
        add(centerPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        this.frame.repaint();
    }

    private void resumeGame() {
        System.out.println("Resuming game...");
        this.close();
        this.parentPanel.getGameAreaPanel().resumeGame();
    }

    private void saveGame() {
        System.out.println("Saving game...");
        this.parentPanel.getGameAreaPanel().saveGame();
    }

    private void returnToMenu() {
        System.out.println("Returning to menu...");
        this.parentPanel.switchToMenu();
        this.close();
    }

    public void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }
}
