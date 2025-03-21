/*
 * @file AbstractActionPanel.java
 * @brief This file defines the `AbstractActionPanel` class.
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
/**
 * @abstractclass AbstractActionPanel
 * @brief An abstract JPanel that pops up on game area and It can b used as a notification dialog.
 * 
 */
public abstract class AbstractActionPanel extends JPanel {
    private int alpha = 0;  /**< The alpha value controlling the transparency of the panel during the fade-in effect. */
    
    /**
     * Constructs an AbstractActionPanel and sets its initial properties.
     * 
     * The panel is set to be non-opaque and initially invisible.
     * 
     */
    public AbstractActionPanel() {
        setOpaque(false);
        setVisible(false);
    }

    /**
     * Fades the panel in by gradually increasing its opacity from transparent to semi-transparent.
     * 
     * The fade-in effect takes place over time using a Timer.
     * 
     */
    public void fadeIn() {
        setVisible(true);
    
        new Timer(50, new ActionListener() {
    
            @Override
            public void actionPerformed(ActionEvent e) {
                if (alpha < 150) {
                    alpha += 10;
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    /**
     * Paints the component with a fading effect by applying an alpha channel.
     * 
     * This method is responsible for drawing the transparent background during the fade-in.
     * 
     * @param g The Graphics object used for painting the component.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));

        // Paint the background with transparency
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        super.paintComponent(g2d); // Let the panel draw its components
        g2d.dispose();
    }

    /**
     * Creates a stylized JButton for the panel.
     * 
     * @param text The text to display on the button.
     * @return The customized JButton.
     */
    protected JButton createButton(String text) {
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

        // Add mouse listener
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
     * Hides the panel by setting its visibility to false.
     * 
     */
    public void close() {
        setVisible(false);
    }
}
