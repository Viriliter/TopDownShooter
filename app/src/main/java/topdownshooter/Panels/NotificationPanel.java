/*
 * @file NotificationPanel.java
 * @brief This file defines the `NotificationPanel` class.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * @class NotificationPanel
 * @brief A panel that displays a notification message for a specified duration.
 * 
 * @note The notification panel uses absolute positioning and should be added to a JFrame for proper display.
 */
public class NotificationPanel extends JPanel {
    private String message = "";
    private Frame frame = null;
    private Timer timer;

    /**
     * Constructs a NotificationPanel with the specified parent JFrame.
     * 
     * @param frame The parent JFrame that will be used to repaint the notification panel.
     */
    public NotificationPanel(JFrame frame) {
        super();
        this.frame = frame;
        setLayout(null); // Use absolute positions

        setOpaque(false);
        //setVisible(false);
        setPreferredSize(new Dimension(300, 60));
    }

    /**
     * Paints the notification panel by drawing a rounded rectangle background 
     * and the message text in the center of the panel.
     * 
     * @param g The Graphics object used to paint the component.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw semi-transparent background
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));

        // Draw text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        FontMetrics fm = g2.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(this.message)) / 2;
        int textY = (getHeight() + fm.getAscent()) / 2 - 5;
        g2.drawString(this.message, textX, textY);

        g2.dispose();

        this.frame.repaint();
    }

    /**
     * Displays the notification message for a specified duration.
     * 
     * @param message The message to be displayed in the notification.
     * @param displayTimeMs The duration (in milliseconds) for which the message should be displayed.
     */
    public void show(String message, int displayTimeMs) {
        this.message = message;
        setVisible(true);

        SwingUtilities.invokeLater(() -> {
            // Ensure only one timer at a time
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }

            timer = new Timer(displayTimeMs, e -> {
                setVisible(false);
            });
            timer.setRepeats(false);
            timer.start(); 
        });
    }
}
