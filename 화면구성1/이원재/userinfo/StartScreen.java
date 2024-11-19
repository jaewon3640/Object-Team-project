package userinfo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartScreen {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Retro Slot Machine");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    setBackground(Color.BLACK);

                    g.setColor(Color.YELLOW);
                    g.setFont(new Font("Monospaced", Font.BOLD, 32));
                    g.drawString("SLOT_MACHINE", 75, 100);

                    g.setFont(new Font("Monospaced", Font.BOLD, 18));
                    g.drawString("객체지향프로그래밍 7조", 85, 150);

                    g.setFont(new Font("Monospaced", Font.BOLD, 16));
                    g.drawString("HAVE A FUN 😊😊", 115, 240);
                }
            };

            panel.setLayout(null);

            JButton startButton = new JButton("[INSERT COIN]");
            startButton.setBounds(115, 280, 150, 40);
            startButton.setBackground(Color.BLACK);
            startButton.setForeground(Color.YELLOW);
            startButton.setFont(new Font("Monospaced", Font.BOLD, 14));
            startButton.setFocusPainted(false);

            startButton.addActionListener(e -> {
                frame.dispose();
                new LoginGUI(); 
            });
            
            Timer blinkTimer = new Timer(500, new ActionListener() {
                private boolean isGreen = true;

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isGreen) {
                        startButton.setForeground(Color.BLACK); 
                    } else {
                        startButton.setForeground(Color.YELLOW); 
                    }
                    isGreen = !isGreen; 
                }
            });

            blinkTimer.start();
            panel.add(startButton);
            frame.add(panel);
            frame.setVisible(true);
        });
    }
}