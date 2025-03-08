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

public abstract class AbstractActionPanel extends JPanel {
    private int alpha = 0;

    public AbstractActionPanel() {
        setOpaque(false);
        setVisible(false);
    }

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


    public void close() {
        setVisible(false);
    }
}
