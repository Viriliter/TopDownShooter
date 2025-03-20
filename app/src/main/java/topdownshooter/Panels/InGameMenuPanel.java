/*
 * @file InGameMenuPanel.java
 * @brief This file defines the `InGameMenuPanel` class.
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
        try {
            System.out.println("Saving game...");
            this.parentPanel.getGameAreaPanel().saveGame();
            System.out.println("Game Saved...");    
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "Game cannot be saved!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void returnToMenu() {
        System.out.println("Returning to menu...");
        this.parentPanel.getGameAreaPanel().exit();
        this.parentPanel.switchToMenu();
        this.close();
    }

    public void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }
}
