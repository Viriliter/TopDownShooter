package topdownshooter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JFrame frame;

    public MenuPanel(JFrame frame) {
        this.frame = frame;
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
        frame.add(new GamePanel(frame));
        frame.revalidate();
        frame.repaint();
    }
}
