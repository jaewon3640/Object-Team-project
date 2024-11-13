package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;

public class chipExchangePage extends JFrame {

	public void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				setUpPanel();
			}
		});
	}

	static chipExchangePage main = null;
	private JPanel panel;
	private JButton excbutton, insertbutton;
	private JLabel label;
	protected JTextField txtmoney = new JTextField(30);
	int chips;
	GridLayoutEx keypad = new GridLayoutEx();

	public chipExchangePage() {
		setTitle("chipExchange");
		setSize(840, 640);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

	}

	@SuppressWarnings("removal")
	private void setUpPanel() {
		panel = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				ImageIcon imageIcon = new ImageIcon("./image/background03.png");
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

		insertbutton = new JButton() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				ImageIcon imageIcon = new ImageIcon("./image/moneybutton.png");
				Image image = imageIcon.getImage();
				g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

			}
		};
		insertbutton.setSize(300, 500);
		insertbutton.setBounds(300, 150, 100, 40);

		insertbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				keypad.run();
			}
		});

		excbutton = new JButton() {

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				ImageIcon imageIcon = new ImageIcon("./image/exchangebutton.png");
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
						try {
							JOptionPane.showMessageDialog(null, getChip() + " chips aquired!");
							txtmoney.setText("");
						} catch (NumberFormatException n) {
							JLabel label = new JLabel("Exceeded maximum money amount");
							label.setFont(new Font("굴림", Font.BOLD, 15));
							label.setForeground(Color.red);
							JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
							txtmoney.setText("");
						}
					} else
						JOptionPane.showMessageDialog(null, "exchange cancelled");

				} else {
					JLabel label = new JLabel("Enter more than 10,000krd");
					label.setFont(new Font("굴림", Font.BOLD, 15));
					label.setForeground(Color.red);
					JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
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
		panel.add(insertbutton);

		add(panel);
	}

	int getChip() {

		int money = Integer.parseInt(txtmoney.getText());
		// System.out.println("입력값:" + (txtmoney.getText()) + "원");
		chips = money / 1000;
		return chips;
	}

	class GridLayoutEx extends JFrame implements ActionListener {

		static final int rowSize = 4;
		static final int colSize = 3;
		JTextField T1;
		JButton[] Button = new JButton[12];
		String tempinput = "";
		int enteredNum;

		void run() {
			setVisible(true);

		}

		GridLayoutEx() {
			setTitle("keypad");
			setLayout(new BorderLayout());
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLocationRelativeTo(null);
			setResizable(false);
			setPreferredSize(new Dimension(300, 300));
			setSize(300, 300);
			T1 = new JTextField(15);
			JPanel p1 = new JPanel();
			JPanel p2 = new JPanel();
			p1.add(T1);
			p2.setLayout(new GridLayout(rowSize, colSize, 5, 5));
			String[] btnValue = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "Enter", "0", "C" };

			for (int i = 0; i < 12; i++) {
				Button[i] = new JButton(btnValue[i]);
				p2.add(Button[i]);
				Button[i].addActionListener(this);
			}

			add(p1, BorderLayout.NORTH);
			add(p2, BorderLayout.CENTER);
			// setVisible(true);

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String input = e.getActionCommand();

			if (input.equals("C")) {
				tempinput = "";
				T1.setText("");
				txtmoney.setText("");
			} else if (input.equals("Enter")) {
				try {
					enteredNum = Integer.parseInt(T1.getText());
					JOptionPane.showMessageDialog(null, enteredNum + "krw 입력했습니다");
					tempinput = "";
					T1.setText("");
					dispose();
				} catch (NumberFormatException n) {
					JLabel label = new JLabel("Exceeded maximum money amount");
					label.setFont(new Font("굴림", Font.BOLD, 15));
					label.setForeground(Color.red);
					JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
					tempinput = "";
					T1.setText("");
					txtmoney.setText("");

				}

			} else {
				tempinput += e.getActionCommand();
				T1.setText(tempinput);
				txtmoney.setText(tempinput);

			}
		}

	}

	public static void main(String[] args) {
		main = new chipExchangePage();
		main.startGUI();
		main.setVisible(true);
	}

}