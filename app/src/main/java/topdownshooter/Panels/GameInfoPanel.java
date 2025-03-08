package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import topdownshooter.Core.ConfigHandler;
import topdownshooter.Weapon.Weapon;

public class GameInfoPanel extends JPanel implements ActionListener, MouseListener{
    private GamePanel parentPanel = null;

    private JLabel healthLabel;
    private JLabel scoreLabel;
    private JLabel gameLevelLabel;
    private JLabel remainingZombiesLabel;
    private JLabel remainingTimeLabel;

    public GameInfoPanel(ConfigHandler config) {
        // Set the layout of the panel to BorderLayout for the overall structure
        setLayout(new BorderLayout());
        setBackground(Color.ORANGE);

        // Create top panel for the upper section of the layout (Menu + Weapon Slots + Game Info + Player Info)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Menu button on the upper left
        JButton menuButton = new JButton("Menu");
        JPanel leftPanel = new JPanel();
        leftPanel.add(menuButton);
        topPanel.add(leftPanel, BorderLayout.WEST);

        // Horizontal layout for Weapon Slots with ammo and magazine count
        JPanel weaponPanel = new JPanel();
        weaponPanel.setLayout(new GridLayout(1, 4)); // 1 row, 4 columns for weapon slots

        for (int i = 0; i < 4; i++) {
            JPanel weaponSlot = new JPanel();
            weaponSlot.setLayout(new BoxLayout(weaponSlot, BoxLayout.Y_AXIS)); // Vertical layout inside each weapon slot
            weaponSlot.add(new JLabel("Weapon " + (i + 1)));
            weaponSlot.add(new JLabel("Ammo: 100"));
            weaponSlot.add(new JLabel("Mag: 30"));
            weaponPanel.add(weaponSlot);
        }
        topPanel.add(weaponPanel, BorderLayout.CENTER);

        // Game level and remaining zombies at the upper middle
        JPanel gameInfoPanel = new JPanel();
        gameInfoPanel.setLayout(new BoxLayout(gameInfoPanel, BoxLayout.Y_AXIS));
        gameLevelLabel = new JLabel("Game Level: 1");
        remainingZombiesLabel = new JLabel("Remaining Zombies: 10");
        gameInfoPanel.add(gameLevelLabel);
        gameInfoPanel.add(remainingZombiesLabel);
        topPanel.add(gameInfoPanel, BorderLayout.NORTH);

        // Player health and score indicators on the upper right
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        healthLabel = new JLabel("Health: 100%");
        scoreLabel = new JLabel("Score: 1000");
        remainingTimeLabel = new JLabel("Time Remaining: 00:00");
        rightPanel.add(healthLabel);
        rightPanel.add(scoreLabel);
        rightPanel.add(remainingTimeLabel);
        topPanel.add(rightPanel, BorderLayout.EAST);

        // Add the top panel to the main panel (which is using BorderLayout)
        add(topPanel, BorderLayout.NORTH);
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

    public void setParentPanel(GamePanel panel) {
        this.parentPanel = panel;
    }

    public void updatePlayerHealth(int health) {
        this.healthLabel.setText("Health: " + health);
    }

    public void updatePlayerScore(int score) {
        this.scoreLabel.setText("Score: " + score);
    }

    public void updateGameLevel(int level) {
        this.gameLevelLabel.setText("Level: " + level);
    }

    public void updateGameRemainingTime(int remainingTimeMs) {
        int seconds = remainingTimeMs / 1000;
        this.remainingTimeLabel.setText("Time Left: " + seconds);
    }

    public void updateRemainingZombieCount(int zombieCount) {
        this.remainingZombiesLabel.setText("Zombies left: " + zombieCount);
    }

    public void updatePlayerInventory(final ArrayList<Weapon> weapon) {

    }


}
