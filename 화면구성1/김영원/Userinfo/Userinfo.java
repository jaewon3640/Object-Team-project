package Userinfo;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Userinfo extends JPanel {
	private User user;
	private CardLayout cardLayout;
	private JLabel chipsLabel; // 칩 레이블

	public Userinfo(User user) {
		this.user = user;
		cardLayout = new CardLayout();
		setLayout(cardLayout);

		// 칩 레이블 초기화
		chipsLabel = createInfoLabel("Chips: " + user.getChipNum());

		// 정보 화면과 수정 화면 추가
		add(createInfoPanel(), "InfoPanel");
		add(createEditPanel(), "EditPanel");
		cardLayout.show(this, "InfoPanel");
	}

	// 칩 레이블 업데이트 메서드
	public void updateChipLabel(int newChipCount) {
		user.setChipNum(newChipCount);
		chipsLabel.setText("Chips: " + newChipCount);
	}

	private JPanel createInfoPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.BLACK);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 타이틀
		JLabel titleLabel = new JLabel("USER INFORMATION", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
		titleLabel.setForeground(Color.YELLOW);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(titleLabel, gbc);

		// 사용자 정보
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		panel.add(createInfoLabel("ID: " + user.getId()), gbc);

		gbc.gridy = 2;
		panel.add(createInfoLabel("Name: " + user.getName()), gbc);

		gbc.gridy = 3;
		panel.add(createInfoLabel("Birthdate: " + user.getBirthdate()), gbc);

		gbc.gridy = 4;
		panel.add(chipsLabel, gbc); // 칩 레이블을 추가

		// 버튼
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridx = 0;

		JButton editButton = createRetroButton("Edit");
		editButton.addActionListener(e -> cardLayout.show(this, "EditPanel"));
		panel.add(editButton, gbc);

		gbc.gridx = 1;
		JButton backButton = createRetroButton("Back");
		backButton.addActionListener(e -> {
			CardLayout layout = (CardLayout) getParent().getLayout();
			layout.show(getParent(), "MainPage");
		});
		panel.add(backButton, gbc);

		return panel;
	}

	// 수정 화면
	private JPanel createEditPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBackground(Color.BLACK);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 타이틀
		JLabel titleLabel = new JLabel("EDIT INFORMATION", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 30));
		titleLabel.setForeground(Color.YELLOW);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(titleLabel, gbc);

		// 수정 입력 필드
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		gbc.gridx = 0;
		panel.add(createEditLabel("ID:"), gbc);

		gbc.gridx = 1;
		JTextField idField = createEditField(user.getId());
		panel.add(idField, gbc);

		gbc.gridy = 2;
		gbc.gridx = 0;
		panel.add(createEditLabel("Name:"), gbc);

		gbc.gridx = 1;
		JTextField nameField = createEditField(user.getName());
		panel.add(nameField, gbc);

		gbc.gridy = 3;
		gbc.gridx = 0;
		panel.add(createEditLabel("Birthdate:"), gbc);

		gbc.gridx = 1;
		JTextField birthField = createEditField(user.getBirthdate());
		panel.add(birthField, gbc);

		// 칩 수는 수정 불가능
		gbc.gridy = 4;
		gbc.gridx = 0;
		panel.add(createEditLabel("Chips:"), gbc);

		gbc.gridx = 1;
		JLabel chipsLabel = createInfoLabel(String.valueOf(user.getChipNum()));
		panel.add(chipsLabel, gbc);

		// 버튼
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.gridx = 0;

		JButton saveButton = createRetroButton("Save");
		// 수정된 saveButton 액션 리스너
		saveButton.addActionListener(e -> {
			// 사용자 객체 업데이트
			user.setId(idField.getText());
			user.setName(nameField.getText());
			user.setBirthdate(birthField.getText());

			// 파일에 저장
			saveUserToFile();

			// 정보 패널 갱신
			removeAll(); // 기존 패널 제거
			add(createInfoPanel(), "InfoPanel");
			add(createEditPanel(), "EditPanel");
			cardLayout.show(this, "InfoPanel");

			JOptionPane.showMessageDialog(this, "Information successfully updated!");
		});

		panel.add(saveButton, gbc);

		gbc.gridx = 1;
		JButton cancelButton = createRetroButton("Cancel");
		cancelButton.addActionListener(e -> cardLayout.show(this, "InfoPanel"));
		panel.add(cancelButton, gbc);

		return panel;
	}

	private JLabel createInfoLabel(String text) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setFont(new Font("Monospaced", Font.BOLD, 20));
		label.setForeground(Color.YELLOW);
		return label;
	}

	private JLabel createEditLabel(String text) {
		JLabel label = new JLabel(text, SwingConstants.RIGHT);
		label.setFont(new Font("Monospaced", Font.BOLD, 20));
		label.setForeground(Color.YELLOW);
		return label;
	}

	public void refreshInfoPanel() {
		// 정보 화면에서 사용자 데이터를 갱신
		for (Component comp : getComponents()) {
			if (comp instanceof JPanel && "InfoPanel".equals(((JPanel) comp).getName())) {
				JPanel infoPanel = (JPanel) comp;

				// ID, Name, Birthdate, Chips 레이블 갱신
				for (Component child : infoPanel.getComponents()) {
					if (child instanceof JLabel) {
						JLabel label = (JLabel) child;
						String text = label.getText();

						if (text.startsWith("ID:")) {
							label.setText("ID: " + user.getId());
						} else if (text.startsWith("Name:")) {
							label.setText("Name: " + user.getName());
						} else if (text.startsWith("Birthdate:")) {
							label.setText("Birthdate: " + user.getBirthdate());
						} else if (text.startsWith("Chips:")) {
							label.setText("Chips: " + user.getChipNum());
						}
					}
				}
			}
		}
	}

	public void saveUserToFile() {
		File inputFile = new File("user.txt"); // 파일 경로를 확인하세요
		StringBuilder updatedContent = new StringBuilder();
		boolean isUpdated = false;

		// 파일을 읽어 기존 내용을 수정
		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
			String line;

			// 파일의 각 줄을 확인하여 수정할 내용 찾기
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\s+");

				if (fields.length >= 5 && fields[0].trim().equals(user.getId())) {
					// ID가 동일하면 사용자 정보를 갱신
					updatedContent.append(user.getId()).append(" ").append(user.getPassword()).append(" ") // 비밀번호 갱신
							.append(user.getName()).append(" ").append(user.getBirthdate()).append(" ")
							.append(fields[4].trim()) // 기존 점수 유지
							.append(System.lineSeparator());
					isUpdated = true;
				} else {
					// 수정 대상이 아니면 기존 데이터를 그대로 유지
					updatedContent.append(line).append(System.lineSeparator());
				}
			}

			if (!isUpdated) {
				JOptionPane.showMessageDialog(this, "ID not found. Update failed.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to read user data from file.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// 수정된 내용을 user.txt에 덮어쓰기
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFile))) {
			writer.write(updatedContent.toString());
			writer.flush(); // 강제로 파일에 반영
			System.out.println("File updated successfully!"); // 디버깅 로그 추가
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to save user data to file.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private JTextField createEditField(String text) {
		JTextField field = new JTextField(text);
		field.setFont(new Font("Monospaced", Font.PLAIN, 18));
		field.setBackground(Color.BLACK);
		field.setForeground(Color.WHITE);
		field.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
		return field;
	}

	private JButton createRetroButton(String text) {
		JButton button = new JButton(text);
		button.setFont(new Font("Monospaced", Font.BOLD, 16));
		button.setBackground(Color.BLACK);
		button.setForeground(Color.YELLOW);
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
}