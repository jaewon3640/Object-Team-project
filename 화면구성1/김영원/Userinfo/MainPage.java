package Userinfo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class MainPage extends JPanel {
	private User user;
	private ArrayList<Dot> dots = new ArrayList<>();
	private Timer fireworksTimer;
	private Image coinImage;
	private CardLayout cardLayout;
	private JPanel mainPanel;

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
		startFireworks(); // 불꽃놀이 애니메이션 시작

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
		chipExchangeButton.addActionListener(e -> cardLayout.show(mainPanel, "ChipExchangePage"));

		// Ranking 버튼
		JButton rankingButton = createRetroButton("Ranking", 320, 360);
		rankingButton.addActionListener(e -> cardLayout.show(mainPanel, "Ranking"));

		// UserInfo 버튼
		JButton userInfoButton = createRetroButton("User Info", 320, 440);
		userInfoButton.addActionListener(e -> cardLayout.show(mainPanel, "UserInfoPage"));

		// HowToPlay 버튼
		JButton howToPlayButton = createRetroButton("How to Play", 320, 520);
		howToPlayButton.addActionListener(e -> cardLayout.show(mainPanel, "HowToPlay")); // "HowToPlay"로 화면 전환

		// Exit 버튼
		JButton exitButton = createRetroButton("Exit", 320, 600);
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

	private void startFireworks() {
		Random random = new Random();
		fireworksTimer = new Timer(50, e -> {
			if (random.nextInt(10) < 3) { // 30% 확률로 도트 생성
				int x = random.nextInt(getWidth());
				int y = random.nextInt(getHeight());
				dots.add(new Dot(x, y, coinImage)); // 이미지 추가
			}

			// 도트 업데이트
			Iterator<Dot> iterator = dots.iterator();
			while (iterator.hasNext()) {
				Dot dot = iterator.next();
				if (!dot.update()) {
					iterator.remove(); // 수명이 다한 도트 제거
				}
			}

			repaint();
		});
		fireworksTimer.start();
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

		String welcomeMessage = "Welcome, " + user.getName() + "!";
		g.setFont(new Font("Monospaced", Font.PLAIN, 20));
		int welcomeTextWidth = g.getFontMetrics().stringWidth(welcomeMessage);
		int welcomeX = (getWidth() - welcomeTextWidth) / 2;
		g.drawString(welcomeMessage, welcomeX, y + 50);

		// 불꽃놀이 도트 그리기
		for (Dot dot : dots) {
			dot.draw(g);
		}
	}

	// 불꽃놀이 도트 클래스
	private class Dot {
		private int x, y, alpha;
		private Image image;

		public Dot(int x, int y, Image image) {
			this.x = x;
			this.y = y;
			this.image = image;
			this.alpha = 255;
		}

		public boolean update() {
			y += 2; // 아래로 이동
			alpha -= 10; // 투명도 감소
			return alpha > 0;
		}

		public void draw(Graphics g) {
			if (alpha > 0) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f)); // 투명도 적용
				g2d.drawImage(image, x, y, 20, 20, null); // 코인 이미지 크기 설정
			}
		}

	}

}