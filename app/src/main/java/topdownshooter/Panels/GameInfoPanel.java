package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import topdownshooter.Core.ConfigHandler;

public class GameInfoPanel extends JPanel implements ActionListener, MouseListener{
    private JLabel playerHealthLabel;
    private JLabel levelLabel;

    public GameInfoPanel(ConfigHandler config) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.WHITE);

        // Player's Health
        playerHealthLabel = new JLabel("Health: 100");
        playerHealthLabel.setForeground(Color.WHITE);
        add(playerHealthLabel);

        // Level Number
        levelLabel = new JLabel("Level: 1");
        levelLabel.setForeground(Color.WHITE);
        add(levelLabel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}
    
    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}
    
    @Override
    public void mouseExited(MouseEvent e) {}
}
