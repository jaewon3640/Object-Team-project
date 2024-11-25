package united;

import javax.swing.*;
import java.awt.*;

public class Userinfo extends JPanel {
    private User user;
    private CardLayout cardLayout;

    public Userinfo(User user) {
        this.user = user;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        // 정보 화면과 수정 화면 추가
        add(createInfoPanel(), "InfoPanel");
        add(createEditPanel(), "EditPanel");
        cardLayout.show(this, "InfoPanel");
    }

    // 사용자 정보 화면
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
        panel.add(createInfoLabel("Chips: " + user.getChipNum()), gbc);

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
        saveButton.addActionListener(e -> {
            user.setId(idField.getText());
            user.setName(nameField.getText());
            user.setBirthdate(birthField.getText());
            JOptionPane.showMessageDialog(this, "Information successfully updated!");
            cardLayout.show(this, "InfoPanel");
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