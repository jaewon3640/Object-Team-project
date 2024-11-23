package united;

import java.awt.AlphaComposite;
import java.awt.Color;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class chipExchangePage extends JFrame {

	public void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setUpPanel();
				setVisible(true);
			}
		});
	}

	private JPanel panel;
	private JButton excbutton;
	private JButton backbutton;
	private JLabel label;
	private JLabel confirm;
	private JTextField txtmoney = new JTextField(15);
	private int chips;
	private User user;
	private ArrayList<Chip> chip = new ArrayList<>();
	private Timer chipTimer;
	private Image chipImage;

	public chipExchangePage(User user) {
		setTitle("chipExchange");
		setSize(840, 640);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		this.user = user;

	}

	@SuppressWarnings("removal")
	private void setUpPanel() {
		chipImage = new ImageIcon("imgs/coin.png").getImage();
		panel = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(Color.BLACK);

				g.setColor(Color.YELLOW);
				g.setFont(new Font("Monospaced", Font.BOLD, 40));
				g.drawString("Insert Money," + user.getName() + "!", 130, 100);
				for (Chip c : chip) {
					c.draw(g);
				}
			}
		};
		Random rand = new Random();
		chipTimer = new Timer(50, e -> {
			if (rand.nextInt(10) < 4) { 
				int x = rand.nextInt(panel.getWidth());
				int y = rand.nextInt(panel.getHeight());
				chip.add(new Chip(x, y, chipImage)); 
			}
			Iterator<Chip> iterator = chip.iterator();
			while (iterator.hasNext()) {
				Chip chip = iterator.next();
				if (!chip.update()) {
					iterator.remove();
				}
			}
			panel.repaint();
		});

		txtmoney.setBounds(380, 242, 100, 40);
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
		panel.add(confirm);

		excbutton = createRetroButton("Exchange", 305, 300, 200, 50);
		excbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (txtmoney.getText().length() >= 5) {
					int result = JOptionPane.showConfirmDialog(null, "confirm exchange?", "exchange confirm",
							JOptionPane.YES_NO_OPTION);

					if (result == JOptionPane.YES_OPTION) {
						try {
							confirm.setText(getChip() +" Chips Aquired!");
							confirm.setVisible(true);
							chipTimer.start();

							user.setChipNum(user.getChipNum() + getChip());
							txtmoney.setText("");
						} catch (NumberFormatException n) {
							JLabel label = new JLabel("Exceeded maximum money amount");
							label.setFont(new Font("Monospaced", Font.BOLD, 15));
							label.setForeground(Color.red);
							JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
							txtmoney.setText("");
						}

					} else {
						JOptionPane.showMessageDialog(null, "exchange cancelled");
					}
				} else
					JOptionPane.showMessageDialog(null, "enter more than 10000 krw");
			}
		});

		backbutton = createRetroButton("back", 700, 550, 80, 30);
		backbutton.addActionListener(e->dispose());

		panel.setLayout(null);
		label = new JLabel("How Much:");
		label.setBounds(300, 230, 100, 40);
		label.setForeground(Color.yellow);
		label.setFont(new Font("Monospaced", Font.BOLD, 15));
		panel.add(label);
		panel.add(txtmoney);
		panel.add(excbutton);
		panel.add(backbutton);

		add(panel);
	}

	int getChip() {
		int money = Integer.parseInt(txtmoney.getText());
		// System.out.println("입력값:" + (txtmoney.getText()) + "원");
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

	public static void main(String[] args) {
		User sampleuser = new User("testUser", "password", "Player1", "1990-01-01");
		chipExchangePage c = new chipExchangePage(sampleuser);
		c.startGUI();
	}

}
