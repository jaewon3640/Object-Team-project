package united;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainPage extends JFrame {
    private User user;
    private ArrayList<Dot> dots = new ArrayList<>();
    private Timer fireworksTimer;
    private Image coinImage;

    public MainPage(User user) {
        this.user = user;

        setTitle("Retro Slot Machine - Main Page");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 코인 이미지 로드
        coinImage = new ImageIcon("imgs/coin.png").getImage();

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.BLACK);

                g.setColor(Color.YELLOW);
                g.setFont(new Font("Monospaced", Font.BOLD, 40));
                g.drawString("🎰 SLOT MACHINE GAME 🎰", 100, 100);

                g.setFont(new Font("Monospaced", Font.PLAIN, 20));
                g.setColor(Color.YELLOW);
                g.drawString("Welcome, " + user.getName() + "!", 300, 150);

                // 불꽃놀이 그리기
                for (Dot dot : dots) {
                    dot.draw(g);
                }
            }
        };

        // 불꽃놀이 애니메이션 설정
        Random random = new Random();
        fireworksTimer = new Timer(50, e -> {
            // 새 도트 생성
            if (random.nextInt(10) < 3) { // 30% 확률로 도트 생성
                int x = random.nextInt(mainPanel.getWidth());
                int y = random.nextInt(mainPanel.getHeight());
                dots.add(new Dot(x, y, coinImage)); // Dot에 코인 이미지 전달
            }

            // 도트 업데이트
            Iterator<Dot> iterator = dots.iterator();
            while (iterator.hasNext()) {
                Dot dot = iterator.next();
                if (!dot.update()) {
                    iterator.remove(); // 수명이 다한 도트 제거
                }
            }

            mainPanel.repaint();
        });
        fireworksTimer.start();

        mainPanel.setLayout(null);

        // 레트로 버튼 생성
        JButton startGameButton = createRetroButton("Start Game", 300, 200);
        JButton chipExchangeButton = createRetroButton("Chip Exchange", 300, 280);
        JButton rankingButton = createRetroButton("Ranking", 300, 360); // Ranking 버튼
        JButton userInfoButton = createRetroButton("User Info", 300, 440);
        JButton exitButton = createRetroButton("Exit Game", 300, 520);

        // 버튼 동작 추가
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // testpage로 이동, 로그인한 사용자 ID를 전달
                testpage slotGamePage = new testpage(user.getId()); // 로그인된 사용자 ID 전달
                slotGamePage.setVisible(true); // 슬롯 머신 게임 화면 보이기
                setVisible(true);
            }
        });
        chipExchangeButton.addActionListener(e -> new chipExchangePage(user).startGUI());
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ranking 페이지로 이동
            	new Ranking(); // User 객체를 전달하여 Ranking 페이지로 이동
                setVisible(true);  // RankingPage 화면 보이기
            }
        });
        userInfoButton.addActionListener(e -> new Userinfo(user).startGUI());
        exitButton.addActionListener(e -> System.exit(0));

        // 패널에 버튼 추가
        mainPanel.add(startGameButton);
        mainPanel.add(chipExchangeButton);
        mainPanel.add(rankingButton);
        mainPanel.add(userInfoButton);
        mainPanel.add(exitButton);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createRetroButton(String text, int x, int y) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 200, 50);
        button.setFont(new Font("Monospaced", Font.BOLD, 16));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.YELLOW);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.YELLOW);
                button.setForeground(Color.BLACK);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.BLACK);
                button.setForeground(Color.YELLOW);
            }
        });
        return button;
    }

    private static class Dot {
        private int x, y, alpha;
        private Image image;

        public Dot(int x, int y, Image image) {
            this.x = x;
            this.y = y;
            this.image = image;
            this.alpha = 255;
        }

        public boolean update() {
            y += 2;
            alpha -= 10; // 투명도 감소
            return alpha > 0; // 투명도가 0보다 크면 유지
        }

        public void draw(Graphics g) {
            if (alpha > 0) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f)); // 투명도 설정
                g2d.drawImage(image, x, y, 20, 20, null); // 16x16 크기로 이미지 그리기
            }
        }
    }

    public static void main(String[] args) {
        User sampleUser = new User("testUser", "password", "Player1", "1990-01-01");
        new MainPage(sampleUser);
    }
}
