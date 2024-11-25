package united;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainPage extends JPanel {
    private User user;
    private ArrayList<Coin> coins = new ArrayList<>();
    private Timer coinTimer;
    private Image coinImage;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ArrayList<JButton> buttons; // 버튼 리스트 추가

    public MainPage(User user, CardLayout cardLayout, JPanel mainPanel) {
        this.user = user;
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;
        this.buttons = new ArrayList<>(); // 버튼 리스트 초기화

        // 이미지 로드
        coinImage = new ImageIcon(getClass().getClassLoader().getResource("imgs/coin.png")).getImage();

        // 메인 화면 구성
        setLayout(null); // 절대 레이아웃
        setBackground(Color.BLACK);

        createButtons(); // 버튼 생성
        startCoinAnimation(); // 코인 애니메이션 시작

        // 화면 크기 변경 리스너 추가
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateButtonPositions(); // 창 크기 변경 시 버튼 위치 업데이트
            }
        });

        // 다른 패널 추가
        Ranking rankingPanel = new Ranking(cardLayout, mainPanel);
        mainPanel.add(rankingPanel, "Ranking"); // "Ranking" 이름으로 Ranking 패널을 mainPanel에 추가
        chipExchangePage chipExchangePanel = new chipExchangePage(user, cardLayout, mainPanel);
        mainPanel.add(chipExchangePanel, "ChipExchangePage"); // ChipExchangePage 추가
        Userinfo userInfoPage = new Userinfo(user); // Userinfo 객체 생성
        mainPanel.add(userInfoPage, "UserInfoPage"); // UserInfoPage를 mainPanel에 추가
        HowToPlay howToPlayPanel = new HowToPlay(cardLayout, mainPanel);
        mainPanel.add(howToPlayPanel, "HowToPlay");
        testpage gamePanel = new testpage(user.getId(), cardLayout, mainPanel);
        mainPanel.add(gamePanel, "testpage"); // "testpage"라는 이름으로 gamePanel을 추가
    }

    // MainPage 클래스
    private void createButtons() {
        // SlotGame 버튼
        JButton slotGameButton = createRetroButton("Slot Game");
        slotGameButton.addActionListener(e -> cardLayout.show(mainPanel, "testpage"));

        // ChipExchange 버튼
        JButton chipExchangeButton = createRetroButton("Chip Exchange");
        chipExchangeButton.addActionListener(e -> cardLayout.show(mainPanel, "ChipExchangePage"));

        // Ranking 버튼
        JButton rankingButton = createRetroButton("Ranking");
        rankingButton.addActionListener(e -> cardLayout.show(mainPanel, "Ranking"));

        // UserInfo 버튼
        JButton userInfoButton = createRetroButton("User Info");
        userInfoButton.addActionListener(e -> cardLayout.show(mainPanel, "UserInfoPage"));

        // How to Play 버튼
        JButton howToPlayButton = createRetroButton("How to Play");
        howToPlayButton.addActionListener(e -> cardLayout.show(mainPanel, "HowToPlay"));

        // Exit 버튼
        JButton exitButton = createRetroButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));

        // 버튼 리스트에 추가
        buttons.add(slotGameButton);
        buttons.add(chipExchangeButton);
        buttons.add(rankingButton);
        buttons.add(userInfoButton);
        buttons.add(howToPlayButton);
        buttons.add(exitButton);

        // 패널에 버튼 추가
        for (JButton button : buttons) {
            add(button);
        }

        // 초기 버튼 위치 설정
        updateButtonPositions();
    }

    // 버튼 스타일 설정
    private JButton createRetroButton(String text) {
        JButton button = new JButton(text);
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

    // 버튼 위치 업데이트
    private void updateButtonPositions() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int buttonWidth = 200;
        int buttonHeight = 50;
        int startY = panelHeight / 3; // 첫 번째 버튼 시작 Y 위치
        int spacing = 30; // 버튼 간 간격

        for (int i = 0; i < buttons.size(); i++) {
            JButton button = buttons.get(i);
            int x = (panelWidth - buttonWidth) / 2; // 중앙 정렬
            int y = startY + i * (buttonHeight + spacing);
            button.setBounds(x, y, buttonWidth, buttonHeight); // 버튼 위치와 크기 설정
        }
    }

    // 코인 애니메이션 시작
    private void startCoinAnimation() {
        Random random = new Random();
        coinTimer = new Timer(50, e -> {
            // 새 코인 생성
            if (random.nextInt(10) < 3) { // 30% 확률로 새 코인 추가
                int x = random.nextInt(getWidth());
                coins.add(new Coin(x, 0)); // y 좌표는 0에서 시작
            }

            // 코인 업데이트
            Iterator<Coin> iterator = coins.iterator();
            while (iterator.hasNext()) {
                Coin coin = iterator.next();
                if (!coin.update()) {
                    iterator.remove(); // 화면 밖으로 나간 코인 제거
                }
            }

            repaint(); // 화면 갱신
        });

        coinTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 배경 및 텍스트
        String title = "🎰 SLOT MACHINE GAME 🎰";
        g.setFont(new Font("Monospaced", Font.BOLD, 40));
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(title);
        int x = (getWidth() - textWidth) / 2;
        int y = 100;

        g.setColor(Color.YELLOW);
        g.drawString(title, x, y);

        // 웰컴 메세지
        String welcomeMessage = "Welcome, " + user.getName() + "!";
        g.setFont(new Font("Monospaced", Font.PLAIN, 20));
        int welcomeTextWidth = g.getFontMetrics().stringWidth(welcomeMessage);
        int welcomeX = (getWidth() - welcomeTextWidth) / 2;
        g.drawString(welcomeMessage, welcomeX, y + 50);

        //사용자의 현재 코인 수
        String coinMessage = "Your Chips: " + user.getChipNum();
        int coinMessageWidth = g.getFontMetrics().stringWidth(coinMessage);
        int coinMessageX = (getWidth() - coinMessageWidth) / 2;
        g.drawString(coinMessage, coinMessageX, y + 80);

        // 코인 그리기
        for (Coin coin : coins) {
            coin.draw(g);
        }
    }

    // 코인 클래스
    private class Coin {
        private int x, y, alpha;
        private final int size = 25; // 코인 크기

        public Coin(int x, int y) {
            this.x = x;
            this.y = y;
            this.alpha = 255; // 초기 투명도
        }

        public boolean update() {
            y += 4; // 코인 이동 속도
            alpha -= 1; // 코인 투명화 속도 감소
            return y < getHeight() && alpha > 0; // 화면 안에 있으면 true
        }

        public void draw(Graphics g) {
            if (alpha > 0) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
                g2d.drawImage(coinImage, x, y, size, size, null);
            }
        }
    }
}