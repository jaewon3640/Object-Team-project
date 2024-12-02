package united;

import javax.swing.*;



import java.awt.*;
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

		// 이미지 로드
		coinImage = new ImageIcon("imgs/coin.png").getImage();

		// 메인 화면 구성
		setLayout(null);
		setBackground(Color.BLACK);

		createButtons();
		startCoinAnimation(); // 불꽃놀이 애니메이션 시작

		// 다른 패널 추가
		JPanel rankingPanel = new Ranking(cardLayout, mainPanel);
		mainPanel.add(rankingPanel, "Ranking");

		Userinfo userInfoPage = new Userinfo(user); // Userinfo 객체 생성
		mainPanel.add(userInfoPage, "UserInfoPage");

		JPanel chipExchangePanel = new chipExchangePage(user, cardLayout, mainPanel, userInfoPage);
		mainPanel.add(chipExchangePanel, "ChipExchangePage");

		JPanel gamePanel = new testpage(user.getId(), user, cardLayout, mainPanel);
		mainPanel.add(gamePanel, "testpage");

		// HowToPlay 패널 추가
		JPanel howToPlayPanel = new HowToPlay(cardLayout, mainPanel);
		mainPanel.add(howToPlayPanel, "HowToPlay");
	}

	// MainPage 클래스
	private void createButtons() {
	    // SlotGame 버튼
	    JButton slotGameButton = createRetroButton("Slot Game", 320, 200);
	    slotGameButton.setLocation(320, 200);  // setLocation 사용
	    slotGameButton.setSize(200, 50);  // 크기 설정
	    slotGameButton.addActionListener(e -> {
	        cardLayout.show(mainPanel, "testpage");

	        // testpage로 이동할 때 칩 개수 업데이트
	        Component[] components = mainPanel.getComponents();
	        for (Component component : components) {
	            if (component instanceof testpage) {
	                testpage testPage = (testpage) component;
	                testPage.updateChipLabel(); // 최신 칩 수 반영
	            }
	        }
	    });

	    // ChipExchange 버튼
	    JButton chipExchangeButton = createRetroButton("Chip Exchange", 320, 280);
	    chipExchangeButton.setLocation(320, 280);  // setLocation 사용
	    chipExchangeButton.setSize(200, 50);  // 크기 설정
	    chipExchangeButton.addActionListener(e -> cardLayout.show(mainPanel, "ChipExchangePage"));

	    // Ranking 버튼
	    JButton rankingButton = createRetroButton("Ranking", 320, 360);
	    rankingButton.setLocation(320, 360);  // setLocation 사용
	    rankingButton.setSize(200, 50);  // 크기 설정
	    rankingButton.addActionListener(e -> cardLayout.show(mainPanel, "Ranking"));

	    // UserInfo 버튼
	    JButton userInfoButton = createRetroButton("User Info", 320, 440);
	    userInfoButton.setLocation(320, 440);  // setLocation 사용
	    userInfoButton.setSize(200, 50);  // 크기 설정
	    userInfoButton.addActionListener(e -> cardLayout.show(mainPanel, "UserInfoPage"));

	    // HowToPlay 버튼
	    JButton howToPlayButton = createRetroButton("How to Play", 320, 520);
	    howToPlayButton.setLocation(320, 520);  // setLocation 사용
	    howToPlayButton.setSize(200, 50);  // 크기 설정
	    howToPlayButton.addActionListener(e -> cardLayout.show(mainPanel, "HowToPlay")); // "HowToPlay"로 화면 전환

	    // Exit 버튼
	    JButton exitButton = createRetroButton("Exit", 320, 600);
	    exitButton.setLocation(320, 600);  // setLocation 사용
	    exitButton.setSize(200, 50);  // 크기 설정
	    exitButton.addActionListener(e -> System.exit(0));

	    // 패널에 추가
	    add(slotGameButton);
	    add(chipExchangeButton);
	    add(rankingButton);
	    add(userInfoButton);
	    add(howToPlayButton); // HowToPlay 버튼 추가
	    add(exitButton);
	}


	// 버튼 스타일 설정
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