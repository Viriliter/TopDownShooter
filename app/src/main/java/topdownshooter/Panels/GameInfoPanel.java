package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Player.InventoryInfo;

public class GameInfoPanel extends JPanel implements ActionListener, MouseListener{
    private class WeaponSlot extends JPanel {
        private final JLabel iconLabel;
        private final JLabel ammoLabel;
        private final JLabel magazineLabel;
    
        public WeaponSlot(ImageIcon icon, int ammo, int magazine) {
            // Set up the layout to arrange icon and labels vertically
            setLayout(new BorderLayout());
    
            // Create the icon label
            iconLabel = new JLabel(icon);
            iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
            // Create the ammo and magazine labels
            ammoLabel = new JLabel("-");
            magazineLabel = new JLabel("-");
            
            // Set font style for ammo/magazine counts
            ammoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            magazineLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    
            // Create a panel for ammo and magazine count and add labels to it
            JPanel countPanel = new JPanel();
            countPanel.setLayout(new BoxLayout(countPanel, BoxLayout.Y_AXIS));
            countPanel.add(ammoLabel);
            countPanel.add(magazineLabel);
    
            // Add components to the main panel
            add(iconLabel, BorderLayout.CENTER);
            add(countPanel, BorderLayout.SOUTH);
        }
    
        public void updateAmmo(int ammo, int magazine) {
            ammoLabel.setText(ammo < 0 ? "-": String.valueOf(ammo));
            magazineLabel.setText(magazine < 0 ? "-": String.valueOf(magazine));
        }

        public void setIcon(ImageIcon icon) {

        }
    }

    private class WeaponInventoryPanel extends JPanel {
        private final List<WeaponSlot> weaponSlots;
        private int lastWeaponIndex = -1;
        WeaponSlot slot1 = null, slot2 = null, slot3 = null, slot4 = null, slot5 = null;
    
        public WeaponInventoryPanel() {
            weaponSlots = new ArrayList<>();
    
            setLayout(new GridLayout(2, 5, 10, 10));
    
            this.slot1 = new WeaponSlot(Globals.loadPNGIcon(Globals.ICON_PATH_PISTOL_INACTIVE, 30, 30), -1, -1);
            weaponSlots.add(this.slot1);
            add(this.slot1);  // Add the slot to the panel

            this.slot2 = new WeaponSlot(Globals.loadPNGIcon(Globals.ICON_PATH_ASSAULT_RIFLE_INACTIVE, 30, 30), -1, -1);
            weaponSlots.add(this.slot2);
            add(this.slot2);  // Add the slot to the panel

            this.slot3 = new WeaponSlot(Globals.loadPNGIcon(Globals.ICON_PATH_SHOTGUN_INACTIVE, 30, 30), -1, -1);
            weaponSlots.add(this.slot3);
            add(this.slot3);  // Add the slot to the panel

            this.slot4 = new WeaponSlot(Globals.loadPNGIcon(Globals.ICON_PATH_SNIPER_RIFLE_INACTIVE, 30, 30), -1, -1);
            weaponSlots.add(this.slot4);
            add(this.slot4);  // Add the slot to the panel

            this.slot5 = new WeaponSlot(Globals.loadPNGIcon(Globals.ICON_PATH_ROCKET_LAUNCHER_INACTIVE, 30, 30), -1, -1);
            weaponSlots.add(this.slot5);
            add(this.slot5);  // Add the slot to the panel

        }
        
        public void setCurrentWeapon(int index) {
            if (this.lastWeaponIndex != index) {
                this.lastWeaponIndex = index;

                switch(this.lastWeaponIndex) {
                    case 0:
                        this.slot1.setIcon(new ImageIcon(Globals.ICON_PATH_PISTOL_SELECTED));
                        break;
                    case 1:
                        this.slot2.setIcon(new ImageIcon(Globals.ICON_PATH_ASSAULT_RIFLE_SELECTED));
                        break;
                    case 2:
                        this.slot3.setIcon(new ImageIcon(Globals.ICON_PATH_SHOTGUN_SELECTED));
                        break;
                    case 3:
                        this.slot4.setIcon(new ImageIcon(Globals.ICON_PATH_SNIPER_RIFLE_SELECTED));
                        break;
                    case 4:
                        this.slot5.setIcon(new ImageIcon(Globals.ICON_PATH_ROCKET_LAUNCHER_SELECTED));
                        break;
                    default:
                    break;
                }
            }
        }

        public void updateWeaponSlot(int index, int ammo, int magazine) {
            if (index >= 0 && index < weaponSlots.size()) {
                weaponSlots.get(index).updateAmmo(ammo, magazine);
                if (ammo==-1 && magazine ==-1) {

                }
            }
        }
    }

    private GamePanel parentPanel = null;

    WeaponInventoryPanel weaponPanel = null;
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
        JPanel leftPanel = new JPanel();

        ImageIcon pngIcon = Globals.loadPNGIcon("menu-icon.png", 16, 16); // Provide the path to your PNG file
        JButton menuButton = new JButton(pngIcon);
        menuButton.setBackground(Color.GRAY);
        menuButton.setForeground(Color.WHITE);  // Set text color to white
        menuButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        menuButton.setFocusPainted(false); // Disable the focus paint (no border on focu
        menuButton.setPreferredSize(new Dimension(24, 24));
        menuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                menuButton.setBackground(Color.DARK_GRAY); // Darker gray on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                menuButton.setBackground(Color.GRAY); // Back to original gray when the mouse leaves
            }

            @Override
            public void mousePressed(MouseEvent e) {
                menuButton.setBackground(Color.LIGHT_GRAY); // Light gray when clicked
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                menuButton.setBackground(Color.DARK_GRAY); // Darker gray when the mouse button is released
            }
        });
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInGameMenu(); // Call the click handler function
            }
        });

        leftPanel.add(menuButton);

        topPanel.add(leftPanel, BorderLayout.WEST);

        this.weaponPanel = new WeaponInventoryPanel();
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

    public void updatePlayerHealth(double health) {
        this.healthLabel.setText("Health: " + (int) health);
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

    public void updatePlayerInventory(final InventoryInfo inventoryInfo) {
        this.weaponPanel.setCurrentWeapon(inventoryInfo.selectedWeaponID);
        this.weaponPanel.updateWeaponSlot(0, inventoryInfo.pistolAmmo, inventoryInfo.pistolMagazine);
        this.weaponPanel.updateWeaponSlot(1, inventoryInfo.assaultRifleAmmo, inventoryInfo.assaultRifleMagazine);
        this.weaponPanel.updateWeaponSlot(2, inventoryInfo.shotgunAmmo, inventoryInfo.shotgunMagazine);
        this.weaponPanel.updateWeaponSlot(3, inventoryInfo.sniperRifleAmmo, inventoryInfo.sniperRifleMagazine);
        this.weaponPanel.updateWeaponSlot(4, inventoryInfo.rocketLauncherAmmo, inventoryInfo.rocketLauncherMagazine);
    }

    private void showInGameMenu() {
        this.parentPanel.getGameAreaPanel().pauseGame();

        InGameMenuPanel inGameMenuPanel = this.parentPanel.getInGameMenuPanel();
        inGameMenuPanel.fadeIn();
    }
}
