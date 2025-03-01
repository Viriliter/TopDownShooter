package topdownshooter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import topdownshooter.Core.ConfigHandler;

public class MenuPanel extends JPanel {
    private JFrame frame;
    private ConfigHandler config = null;

    public MenuPanel(JFrame frame, ConfigHandler config) {
        this.frame = frame;
        this.config = config;
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Zombie Shooter", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        add(title, BorderLayout.NORTH);

        JButton startButton = new JButton("Start Game");
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });

        add(startButton, BorderLayout.CENTER);
    }

    private void startGame() {
        frame.getContentPane().removeAll();
        frame.add(new GamePanel(frame, config));
        frame.revalidate();
        frame.repaint();
    }
}
