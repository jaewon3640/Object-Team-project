package united;

import java.awt.CardLayout;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

// 생성자 하나로 통합
public class chipExchangePage extends JPanel {
	private JTextField txtmoney = new JTextField(15);
	private JButton excbutton;
	private JButton backbutton;
	private JLabel label;
	private int chips;
	private User user;
	private CardLayout cardLayout;
	private JPanel mainContainer;

	// 생성자 하나로 통합
	public chipExchangePage(User user, CardLayout cardLayout, JPanel mainContainer) {
		this.user = user;
		this.cardLayout = cardLayout;
		this.mainContainer = mainContainer;

		// 레이아웃 및 패널 설정
		setLayout(null);
		setBackground(Color.BLACK); // 기본 배경은 검정색으로 설정 (이미지가 그려질 부분)

		// 라벨 추가
		label = new JLabel("How much?:");
		label.setBounds(100, 150, 100, 40);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("", Font.BOLD, 15));

		// 텍스트 필드 설정
		txtmoney.setBounds(200, 160, 80, 20);
		txtmoney.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) {
					e.consume();
				}
			}
		});

		// 칩 교환 버튼 설정
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
		excbutton.setBounds(160, 300, 150, 50);
		excbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleExchange();
			}
		});

		// 뒤로가기 버튼 설정
		backbutton = new JButton("Back");
		backbutton.setBounds(700, 550, 80, 30);
		backbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(mainContainer, "MainPage"); // "MainPage"로 돌아가기
			}
		});

		// 구성 요소 추가
		add(label);
		add(txtmoney);
		add(excbutton);
		add(backbutton);
	}

	// paintComponent 메서드에서 배경 이미지를 그리도록 수정
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		// 이미지 불러오기
		ImageIcon imageIcon = new ImageIcon("imgs/background03.png");
		Image image = imageIcon.getImage();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this); // 패널 크기에 맞게 이미지를 그립니다.
	}

	private void handleExchange() {
		if (txtmoney.getText().length() >= 5) {
			int result = JOptionPane.showConfirmDialog(null, "Confirm exchange?", "Exchange Confirm",
					JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION) {
				int acquiredChips = getChip();
				user.setChipNum(user.getChipNum() + acquiredChips);
				JOptionPane.showMessageDialog(this, acquiredChips + " chips acquired!");
			} else {
				JOptionPane.showMessageDialog(this, "Exchange cancelled.");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Enter at least 10000 KRW.");
		}
	}

	private int getChip() {
		int money = Integer.parseInt(txtmoney.getText());
		chips = money / 1000;
		return chips;
	}
}
