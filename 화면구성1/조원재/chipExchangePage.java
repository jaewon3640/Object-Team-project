package Userinfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class chipExchangePage extends JFrame {
    public static int chips; // 교환된 칩 개수를 저장하는 변수
    private JPanel panel;
    private JButton excbutton;
    private JLabel label;
    private JTextField txtmoney = new JTextField(15);

    public void startGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setUpPanel();
            }
        });
    }

    static chipExchangePage main = null;

    public chipExchangePage() {
        setTitle("chipExchange");
        setSize(840, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void setUpPanel() {
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("imgs/background03.png");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        txtmoney.setBounds(200, 160, 100, 40);
        txtmoney.setSize(80, 20);
        txtmoney.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar(); // 숫자 아니면 입력 막음
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        excbutton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imageIcon = new ImageIcon("imgs/moneybutton.png");
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
            }
        };

        excbutton.setSize(150, 150);
        excbutton.setBounds(160, 300, 100, 40);

        excbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtmoney.getText().length() >= 5) {
                    int result = JOptionPane.showConfirmDialog(null, "confirm exchange?", "exchange confirm",
                            JOptionPane.YES_NO_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        JOptionPane.showMessageDialog(null, "exchange confirmed");
                        JOptionPane.showMessageDialog(null, getChip() + " chips acquired!");
                        testpage slotGame = new testpage(chips); // testpage 인스턴스 생성
                        slotGame.createSlotGame(); // createSlotGame 메서드 호출
                        dispose(); // 현재 창 닫기
                    } else {
                        JOptionPane.showMessageDialog(null, "exchange cancelled");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "enter more than 10000 krw");
                }
            }
        });

        panel.setLayout(null);

        label = new JLabel("How much?:");
        label.setBounds(100, 150, 100, 40);
        label.setForeground(Color.white);
        label.setFont(new Font("", Font.BOLD, 15));

        panel.add(label);
        panel.add(txtmoney);
        panel.add(excbutton);

        add(panel);
    }

    int getChip() {
        int money = Integer.parseInt(txtmoney.getText());
        chips = money / 1000;
        return chips;
    }

    public static void main(String[] args) {
        main = new chipExchangePage();
        main.startGUI();
        main.setVisible(true);
    }
}
