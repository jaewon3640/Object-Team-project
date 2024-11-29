package Userinfo;

import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

// 생성자 하나로 통합
public class chipExchangePage extends JPanel {
	private JTextField txtmoney = new JTextField(15);
	private JButton excbutton;
	private JButton backbutton;
	private JLabel label;
	private JLabel confirm;
	private int chips;
	private User user;
	private CardLayout cardLayout;
	private JPanel mainContainer;
	private Timer timer;
	private ArrayList<Chip> chip = new ArrayList<>();
	private Timer chipTimer;
	private Image chipImage;
	private Userinfo userInfoPage;

	// 생성자 하나로 통합
	public chipExchangePage(User user, CardLayout cardLayout, JPanel mainContainer, Userinfo userInfoPage) {
		this.user = user;
		this.cardLayout = cardLayout;
		this.mainContainer = mainContainer;
		this.userInfoPage = userInfoPage;
		chipImage = new ImageIcon("imgs/coin.png").getImage();
		// 레이아웃 및 패널 설정
		setLayout(null);
		setBackground(Color.BLACK); // 기본 배경은 검정색으로 설정 (이미지가 그려질 부분)

		Random rand = new Random();
		chipTimer = new Timer(50, e -> {
			if (rand.nextInt(10) < 4) {
				int x = rand.nextInt(this.getWidth());
				int y = rand.nextInt(this.getHeight());
				chip.add(new Chip(x, y, chipImage));
			}
			Iterator<Chip> iterator = chip.iterator();
			while (iterator.hasNext()) {
				Chip chip = iterator.next();
				if (!chip.update()) {
					iterator.remove();
				}
			}
			this.repaint();
		});
		// 라벨 추가
		label = new JLabel("How much?:");
		label.setBounds(300, 330, 100, 40);
		label.setForeground(Color.yellow);
		label.setFont(new Font("Monospaced", Font.BOLD, 15));

		// 텍스트 필드 설정
		txtmoney.setBounds(390, 342, 100, 40);
		txtmoney.setSize(80, 20);
		txtmoney.setBackground(Color.BLACK);
		txtmoney.setForeground(Color.CYAN);
		txtmoney.setFont(new Font("Monospaced", Font.BOLD, 12));
		txtmoney.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar(); // 숫자 아니면 입력 막음
				if (!Character.isDigit(c)) {
					e.consume();
				}
			}
		});
		confirm = new JLabel(" ", SwingConstants.CENTER);
		confirm.setFont(new Font("Monospaced", Font.BOLD, 45));
		confirm.setForeground(Color.CYAN);
		confirm.setVisible(false);
		confirm.setBounds(100, 130, 640, 100);

		timer = new Timer(300, e -> {
			if (confirm.getForeground().equals(Color.CYAN)) {
				confirm.setForeground(Color.BLACK);
			} else {
				confirm.setForeground(Color.CYAN);
			}
		});

		// 칩 교환 버튼 설정
		excbutton = new JButton();
		excbutton = createRetroButton("Exchange", 305, 400, 200, 50);
		excbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleExchange();
			}
		});

		// 뒤로가기 버튼 설정
		backbutton = createRetroButton("back", 700, 700, 80, 30);
		backbutton.addActionListener(e -> {
			confirm.setVisible(false);
			chip.clear();
			chipTimer.stop();
			this.repaint();
			cardLayout.show(mainContainer, "MainPage");
			Component[] components = mainContainer.getComponents();
			for (Component component : components) {
				if (component instanceof testpage) {
					testpage testPage = (testpage) component;
					testPage.updateChipLabel(); // 칩 개수 업데이트
				}
			}
		});

		// 구성 요소 추가
		add(label);
		add(confirm);
		add(txtmoney);
		add(excbutton);
		add(backbutton);
	}

	// paintComponent 메서드에서 배경 이미지를 그리도록 수정
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.BLACK);

		g.setColor(Color.YELLOW);
		g.setFont(new Font("Monospaced", Font.BOLD, 60));
		g.drawString("Insert Money," + user.getName() + "!", 150, 100);
		for (Chip c : chip) {
			c.draw(g);
		}

	}

	private void handleExchange() {
	    if (txtmoney.getText().length() >= 5) {
	        int result = JOptionPane.showConfirmDialog(null, "Confirm exchange?", "Exchange Confirm",
	                JOptionPane.YES_NO_OPTION);

	        if (result == JOptionPane.YES_OPTION) {
	            try {
	                int acquiredChips = getChip();
	                confirm.setText(acquiredChips + " Chips Acquired!");
	                chipTimer.start();
	                confirm.setVisible(true);
	                timer.start();

	                // User 객체의 칩 수 업데이트
	                user.setChipNum(user.getChipNum() + acquiredChips);
	                userInfoPage.updateChipLabel(user.getChipNum()); // Userinfo 페이지의 칩 레이블 업데이트
	                txtmoney.setText("");

	                // testpage의 칩 개수 레이블 업데이트
	                Component[] components = mainContainer.getComponents();
	                for (Component component : components) {
	                    if (component instanceof testpage) {
	                        testpage testPage = (testpage) component;
	                        testPage.updateChipLabel(); // 최신 칩 수 반영
	                    }
	                }
	            } catch (NumberFormatException n) {
	                JLabel label = new JLabel("Exceeded maximum money amount");
	                label.setFont(new Font("Monospaced", Font.BOLD, 15));
	                label.setForeground(Color.red);
	                JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
	                txtmoney.setText("");
	            }
	        } else {
	            JLabel label = new JLabel("Exchange cancelled");
	            label.setFont(new Font("Monospaced", Font.BOLD, 15));
	            label.setForeground(Color.red);
	            JOptionPane.showMessageDialog(null, label, "CANCELLATION", JOptionPane.WARNING_MESSAGE);
	        }
	    } else {
	        JLabel label = new JLabel("Enter at least 10000 KRW.");
	        label.setFont(new Font("Monospaced", Font.BOLD, 15));
	        label.setForeground(Color.red);
	        JOptionPane.showMessageDialog(null, label, "WARNING", JOptionPane.WARNING_MESSAGE);
	    }
	}

	private int getChip() {
		int money = Integer.parseInt(txtmoney.getText());
		chips = money / 1000;
		return chips;
	}

	private JButton createRetroButton(String text, int x, int y, int z, int u) {
		JButton button = new JButton(text);
		button.setBounds(x, y, z, u);
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

	private static class Chip {
		private int x, y, alpha;
		private Image image;

		public Chip(int x, int y, Image image) {
			this.x = x;
			this.y = y;
			this.image = image;
			this.alpha = 255;
		}

		public boolean update() {
			y += 2;
			alpha -= 10;
			return alpha > 0;
		}

		public void draw(Graphics g) {
			if (alpha > 0) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
				g2d.drawImage(image, x, y, 80, 80, null);
			}
		}

	}

}