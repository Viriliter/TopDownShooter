package topdownshooter.Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;

import topdownshooter.Core.ConfigHandler;
import topdownshooter.Core.Globals;
import topdownshooter.Player.InventoryInfo;
import topdownshooter.Weapon.WeaponType;

public class GameInfoPanel extends JPanel implements ActionListener, MouseListener{
    private class WeaponSlot extends JPanel {
        private final JLabel iconLabel;
        private final JLabel ammoLabel;
        private final JLabel magazineLabel;
        private final JLabel ammoIconLabel;
        private final JLabel magazineIconLabel;

        public static final int weaponIconSize = 50;
    
        public WeaponSlot(ImageIcon icon) {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(100, 50)); // Fixed size
            setBackground(Color.BLACK);

            // Create the icon label
            this.iconLabel = new JLabel(icon);

            // Create ammo & magazine labels
            this.ammoLabel = new JLabel("-", SwingConstants.LEFT);
            this.ammoLabel.setForeground(Color.WHITE);
            this.ammoLabel.setFont(new Font("Arial", Font.BOLD, 18));
            this.ammoLabel.setPreferredSize(new Dimension(25, 25));
            this.ammoLabel.setHorizontalAlignment(SwingConstants.LEFT);

            this.magazineLabel = new JLabel("-", SwingConstants.LEFT);
            this.magazineLabel.setForeground(Color.WHITE);
            this.magazineLabel.setFont(new Font("Arial", Font.BOLD, 18));
            this.magazineLabel.setPreferredSize(new Dimension(25, 25));
            this.magazineLabel.setHorizontalAlignment(SwingConstants.LEFT);
           
            // Scale ammo & magazine icons to 30x30
            this.ammoIconLabel = new JLabel(Globals.loadPNGIcon(Globals.ICON_PATH_BULLET, 15, 15));
            this.magazineIconLabel = new JLabel(Globals.loadPNGIcon(Globals.ICON_PATH_MAGAZINE, 15, 15));

            // Create a panel for the ammo & magazine info
            JPanel infoPanel = new JPanel(new GridLayout(2, 2));
            infoPanel.setPreferredSize(new Dimension(100, 100)); // Fixed size for alignment
            infoPanel.setBackground(Color.BLACK);

            infoPanel.add(this.ammoIconLabel);
            infoPanel.add(this.ammoLabel);
            infoPanel.add(this.magazineIconLabel);
            infoPanel.add(this.magazineLabel);

            // Add components
            add(this.iconLabel, BorderLayout.WEST);
            add(infoPanel, BorderLayout.CENTER);
        }

        public void updateAmmo(int ammo, int magazine) {
            this.ammoLabel.setText(ammo < 0 ? "-": String.valueOf(ammo));
            this.magazineLabel.setText(magazine < 0 ? "-": String.valueOf(magazine));
        }

        public void setIcon(ImageIcon icon) {
            this.iconLabel.setIcon(icon);
        }
    }

    private class WeaponInventoryPanel extends JPanel {
        private enum WeaponStatus {
            EMPTY,      // The weapon has not been earned yet 
            INACTIVE,   // The weapon is not using
            ACTIVE      // Current weapon that is used by the player
        }

        private WeaponType lastWeaponType = WeaponType.UNDEFINED;

        private LinkedHashMap<WeaponType, WeaponSlot> weaponSlots = null;
        
        private static final ImageIcon iconPistolEmpty = Globals.loadPNGIcon(Globals.ICON_PATH_PISTOL_EMPTY, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconPistolUnselected = Globals.loadPNGIcon(Globals.ICON_PATH_PISTOL_UNSELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconPistolSelected = Globals.loadPNGIcon(Globals.ICON_PATH_PISTOL_SELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconAssaultRifleEmpty = Globals.loadPNGIcon(Globals.ICON_PATH_ASSAULT_RIFLE_EMPTY, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconAssaultRifleUnselected = Globals.loadPNGIcon(Globals.ICON_PATH_ASSAULT_RIFLE_UNSELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconAssaultRifleSelected = Globals.loadPNGIcon(Globals.ICON_PATH_ASSAULT_RIFLE_SELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconShotgunEmpty = Globals.loadPNGIcon(Globals.ICON_PATH_SHOTGUN_EMPTY, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconShotgunUnselected = Globals.loadPNGIcon(Globals.ICON_PATH_SHOTGUN_UNSELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconShotgunSelected = Globals.loadPNGIcon(Globals.ICON_PATH_SHOTGUN_SELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconSniperRifleEmpty = Globals.loadPNGIcon(Globals.ICON_PATH_SNIPER_RIFLE_EMPTY, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconSniperRifleUnselected = Globals.loadPNGIcon(Globals.ICON_PATH_SNIPER_RIFLE_UNSELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconSniperRifleSelected = Globals.loadPNGIcon(Globals.ICON_PATH_SNIPER_RIFLE_SELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconRocketLauncherEmpty = Globals.loadPNGIcon(Globals.ICON_PATH_ROCKET_LAUNCHER_EMPTY, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconRocketLauncherUnselected = Globals.loadPNGIcon(Globals.ICON_PATH_ROCKET_LAUNCHER_UNSELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);
        private static final ImageIcon iconRocketLauncherSelected = Globals.loadPNGIcon(Globals.ICON_PATH_ROCKET_LAUNCHER_SELECTED, WeaponSlot.weaponIconSize, WeaponSlot.weaponIconSize);

        public WeaponInventoryPanel() {   
            setLayout(new GridLayout(1, 5, 10, 10));
            setBackground(Color.BLACK);

            weaponSlots = new LinkedHashMap<>();
            weaponSlots.put(WeaponType.PISTOL, new WeaponSlot(iconPistolEmpty));
            weaponSlots.put(WeaponType.ASSAULTRIFLE, new WeaponSlot(iconAssaultRifleEmpty));
            weaponSlots.put(WeaponType.SHOTGUN, new WeaponSlot(iconShotgunEmpty));
            weaponSlots.put(WeaponType.SNIPERRIFLE, new WeaponSlot(iconSniperRifleEmpty));
            weaponSlots.put(WeaponType.ROCKETLAUNCHER, new WeaponSlot(iconRocketLauncherEmpty));

            for (WeaponType wt : weaponSlots.keySet()) {
                System.out.println(wt);
                add(this.weaponSlots.get(wt));  // Add the slot to the panel
            }

        }

        public void updateInventory(final InventoryInfo inventoryInfo) {
            for (WeaponType weaponType : WeaponType.values()) {
                WeaponType selectedWeaponType = inventoryInfo.selectedWeaponType;
                boolean isWeaponSelected = selectedWeaponType == weaponType;
        
                // If ammo and magazine are -1, set empty icon
                boolean isWeaponEmpty = false;
                switch (weaponType) {
                    case PISTOL:
                        isWeaponEmpty = (inventoryInfo.pistolAmmo == -1 && inventoryInfo.pistolMagazine == -1);
                        break;
                    case ASSAULTRIFLE:
                        isWeaponEmpty = (inventoryInfo.assaultRifleAmmo == -1 && inventoryInfo.assaultRifleMagazine == -1);
                        break;
                    case SHOTGUN:
                        isWeaponEmpty = (inventoryInfo.shotgunAmmo == -1 && inventoryInfo.shotgunMagazine == -1);
                        break;
                    case SNIPERRIFLE:
                        isWeaponEmpty = (inventoryInfo.sniperRifleAmmo == -1 && inventoryInfo.sniperRifleMagazine == -1);
                        break;
                    case ROCKETLAUNCHER:
                        isWeaponEmpty = (inventoryInfo.rocketLauncherAmmo == -1 && inventoryInfo.rocketLauncherMagazine == -1);
                        break;
                    default:
                        break;
                }

                if (weaponType==WeaponType.UNDEFINED) continue;

                // Update icon based on whether weapon is selected or empty
                if (isWeaponSelected) {
                    // Set the selected icon
                    this.weaponSlots.get(weaponType).setIcon(getSelectedIcon(weaponType));
                    this.lastWeaponType = selectedWeaponType;
                } else {
                    // Set the unselected or empty icon
                    this.weaponSlots.get(weaponType).setIcon(isWeaponEmpty ? getEmptyIcon(weaponType) : getUnselectedIcon(weaponType));
                }
                
                // Update ammo for each weapon type
                updateAmmoForWeapon(weaponType, inventoryInfo);
            }
        }
        
        private ImageIcon getSelectedIcon(WeaponType weaponType) {
            switch (weaponType) {
                case PISTOL: return iconPistolSelected;
                case ASSAULTRIFLE: return iconAssaultRifleSelected;
                case SHOTGUN: return iconShotgunSelected;
                case SNIPERRIFLE: return iconSniperRifleSelected;
                case ROCKETLAUNCHER: return iconRocketLauncherSelected;
                default: return null;
            }
        }
        
        private ImageIcon getUnselectedIcon(WeaponType weaponType) {
            switch (weaponType) {
                case PISTOL: return iconPistolUnselected;
                case ASSAULTRIFLE: return iconAssaultRifleUnselected;
                case SHOTGUN: return iconShotgunUnselected;
                case SNIPERRIFLE: return iconSniperRifleUnselected;
                case ROCKETLAUNCHER: return iconRocketLauncherUnselected;
                default: return null;
            }
        }
        
        private ImageIcon getEmptyIcon(WeaponType weaponType) {
            switch (weaponType) {
                case PISTOL: return iconPistolEmpty;
                case ASSAULTRIFLE: return iconAssaultRifleEmpty;
                case SHOTGUN: return iconShotgunEmpty;
                case SNIPERRIFLE: return iconSniperRifleEmpty;
                case ROCKETLAUNCHER: return iconRocketLauncherEmpty;
                default: return null;
            }
        }
        
        private void updateAmmoForWeapon(WeaponType weaponType, InventoryInfo inventoryInfo) {
            switch (weaponType) {
                case PISTOL:
                    this.weaponSlots.get(WeaponType.PISTOL).updateAmmo(inventoryInfo.pistolAmmo, inventoryInfo.pistolMagazine);
                    break;
                case ASSAULTRIFLE:
                    this.weaponSlots.get(WeaponType.ASSAULTRIFLE).updateAmmo(inventoryInfo.assaultRifleAmmo, inventoryInfo.assaultRifleMagazine);
                    break;
                case SHOTGUN:
                    this.weaponSlots.get(WeaponType.SHOTGUN).updateAmmo(inventoryInfo.shotgunAmmo, inventoryInfo.shotgunMagazine);
                    break;
                case SNIPERRIFLE:
                    this.weaponSlots.get(WeaponType.SNIPERRIFLE).updateAmmo(inventoryInfo.sniperRifleAmmo, inventoryInfo.sniperRifleMagazine);
                    break;
                case ROCKETLAUNCHER:
                    this.weaponSlots.get(WeaponType.ROCKETLAUNCHER).updateAmmo(inventoryInfo.rocketLauncherAmmo, inventoryInfo.rocketLauncherMagazine);
                    break;
                default:
                    break;
            }
        }
    }

    private GamePanel parentPanel = null;

    WeaponInventoryPanel weaponInventoryPanel = null;
    private JLabel healthLabel;
    private JLabel scoreLabel;
    private JLabel gameLevelLabel;
    private JLabel remainingZombiesLabel;
    private JLabel remainingTimeLabel;

    public GameInfoPanel(ConfigHandler config) {
        // Set the layout of the panel to BorderLayout for the overall structure
        setLayout(new BorderLayout());
        setBackground(Color.ORANGE);

        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Align left with spacing
        leftPanel.setBackground(Color.BLACK);

        // Menu button
        ImageIcon pngIcon = Globals.loadPNGIcon("menu-icon.png", 32, 32); // Provide the path to your PNG file
        JButton menuButton = new JButton(pngIcon);
        menuButton.setBackground(Color.GRAY);
        menuButton.setForeground(Color.WHITE);  // Set text color to white
        menuButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        menuButton.setFocusPainted(false); // Disable the focus paint (no border on focu
        menuButton.setPreferredSize(new Dimension(50, 50));
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

        this.weaponInventoryPanel = new WeaponInventoryPanel();
        this.weaponInventoryPanel.setPreferredSize(new Dimension(800, 50)); // Adjust the width/height as needed
        
        leftPanel.add(menuButton);
        leftPanel.add(weaponInventoryPanel);

        // CENTER PANEL

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // Set vertical layout
        centerPanel.setBackground(Color.BLACK);

        gameLevelLabel = new JLabel("");
        gameLevelLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gameLevelLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gameLevelLabel.setForeground(Color.WHITE);
        remainingZombiesLabel = new JLabel("");
        remainingZombiesLabel.setHorizontalAlignment(SwingConstants.CENTER);
        remainingZombiesLabel.setFont(new Font("Arial", Font.BOLD, 18));
        remainingZombiesLabel.setForeground(Color.WHITE);
        
        centerPanel.add(gameLevelLabel);
        centerPanel.add(remainingZombiesLabel);

        // Player Health and Score
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.BLACK);

        healthLabel = new JLabel("");
        healthLabel.setFont(new Font("Arial", Font.BOLD, 18));
        healthLabel.setForeground(Color.WHITE);
        scoreLabel = new JLabel("");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);
        remainingTimeLabel = new JLabel("");
        remainingTimeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        remainingTimeLabel.setForeground(Color.WHITE);

        rightPanel.add(healthLabel);
        rightPanel.add(scoreLabel);
        rightPanel.add(remainingTimeLabel);
        
        // Add panels to the main panel
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
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
        this.remainingZombiesLabel.setText("Zombies Left: " + zombieCount);
    }

    public void updatePlayerInventory(final InventoryInfo inventoryInfo) {
        this.weaponInventoryPanel.updateInventory(inventoryInfo);
    }

    private void showInGameMenu() {
        this.parentPanel.getGameAreaPanel().pauseGame();

        InGameMenuPanel inGameMenuPanel = this.parentPanel.getInGameMenuPanel();
        inGameMenuPanel.fadeIn();
    }
}
