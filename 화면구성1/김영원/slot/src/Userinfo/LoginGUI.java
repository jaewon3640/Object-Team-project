package Userinfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginGUI extends JFrame {
	private static JTextField usernameField;
	private static JPasswordField passwordField;
	private JLabel messageLabel;
	

	public LoginGUI() {
		setTitle("Slot Machine Game - Login");
		setSize(850, 800);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JLayeredPane layeredPane = new JLayeredPane();
		setContentPane(layeredPane);
		layeredPane.setLayout(new BorderLayout());

		BackgroundPanel backgroundPanel = new BackgroundPanel("imgs/imageSlot.png");
		backgroundPanel.setLayout(new BorderLayout());

		JLabel titleLabel = new JLabel("   Slot           Game", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Serif", Font.BOLD, 45));
		titleLabel.setForeground(Color.cyan);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));
		backgroundPanel.add(titleLabel, BorderLayout.NORTH);

		JPanel loginPanel = new JPanel(new GridBagLayout());
		loginPanel.setOpaque(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		usernameField = new JTextField(15);
		passwordField = new JPasswordField(15);

		messageLabel = new JLabel();
		messageLabel.setForeground(Color.RED);
		messageLabel.setPreferredSize(new Dimension(200, 25));
		messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

		JButton loginButton = new JButton("로그인");
		JButton signUpButton = new JButton("회원가입");

		JLabel usernameLabel = new JLabel("아이디 :");
		usernameLabel.setFont(new Font("Serif", Font.PLAIN, 15));
		usernameLabel.setForeground(Color.WHITE);

		JLabel passwordLabel = new JLabel("비밀번호 :");
		passwordLabel.setFont(new Font("Serif", Font.PLAIN, 15));
		passwordLabel.setForeground(Color.WHITE);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.WEST;
		loginPanel.add(usernameLabel, gbc);

		gbc.gridy = 1;
		loginPanel.add(usernameField, gbc);

		gbc.gridy = 2;
		loginPanel.add(passwordLabel, gbc);

		gbc.gridy = 3;
		gbc.gridwidth = 2;
		loginPanel.add(passwordField, gbc);

		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		loginPanel.add(messageLabel, gbc);

		gbc.gridy = 5;
		gbc.gridx = 0;
		gbc.gridwidth = 1;
		loginPanel.add(loginButton, gbc);

		gbc.gridx = 1;
		loginPanel.add(signUpButton, gbc);

		backgroundPanel.add(loginPanel, BorderLayout.CENTER);
		layeredPane.add(backgroundPanel, BorderLayout.CENTER);

		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User user=readUserFromFile("user.txt");
				if (user!=null) {
					JOptionPane.showMessageDialog(null, "로그인 성공! 게임 메인 페이지로 이동합니다.");
					dispose();
					// MainPage 창 열기
					new MainPage();
				}
				else {
					JOptionPane.showMessageDialog(null, "로그인 실패, 다시 시도하세요.");
				}
			}
		});
		signUpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleSignUp();
			}
		});

		setVisible(true);
	}

	

	private void handleSignUp() {
		JTextField usernameField = new JTextField();
		JPasswordField passwordField = new JPasswordField();
		JTextField nameField = new JTextField();
		JTextField birthdateField = new JTextField();

		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.add(new JLabel("아이디 :"));
		panel.add(usernameField);
		panel.add(new JLabel("비밀번호 :"));
		panel.add(passwordField);
		panel.add(new JLabel("이름 :"));
		panel.add(nameField);
		panel.add(new JLabel("생년월일 (YYYY-MM-DD):"));
		panel.add(birthdateField);

		int result = JOptionPane.showConfirmDialog(this, panel, "회원가입", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			String name = nameField.getText();
			String birthdate = birthdateField.getText();

			if (!username.isEmpty() && !password.isEmpty()) {
				
				// Save user information to "user.txt" using space as a separator
				try (BufferedWriter writer = new BufferedWriter(new FileWriter("user.txt", true))) {
					writer.write(username + " " + password + " " + name + " " + birthdate);
					writer.newLine();
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "회원 정보를 저장하는 중 오류가 발생했습니다.");
					ex.printStackTrace();
				}

				JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!");
			} else {
				JOptionPane.showMessageDialog(this, "아이디와 비밀번호는 필수 입력 사항입니다.");
			}
		}
	}


	private class BackgroundPanel extends JPanel {
		private ImageIcon backgroundImage;

		public BackgroundPanel(String imagePath) {
			backgroundImage = new ImageIcon(imagePath);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Image image = backgroundImage.getImage();
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		}
	}

	static protected User readUserFromFile(String filePath) {
		User user=null;
		try {
			String line = "";

			FileReader reader = new FileReader(new File(filePath));
			BufferedReader bufferReader = new BufferedReader(reader);
			List<String> Lines = new ArrayList<String>();

			while ((line = bufferReader.readLine()) != null) {
				Lines.add(line);
			}
			bufferReader.close();
			for (int i = 0; i < Lines.size(); i++) {

				String[] userData = Lines.get(i).split(" ");
				String username = usernameField.getText();
				String password = new String(passwordField.getPassword());
				if (username.equals(userData[0]) && password.equals(userData[1])) {
					user=new User(userData[0], userData[1], userData[2], userData[3]);
				}
					
			}
		} catch (Exception e) {
			
		}
		return user;

	}

	public static void main(String[] args) {
		new LoginGUI();

	}
}