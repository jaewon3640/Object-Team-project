package SlotGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JLabel timeLabel;
    private Map<String, User> userDatabase = new HashMap<>();

    public LoginGUI() {
        setTitle("Slot Machine Game - Login");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);
        layeredPane.setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel("image.jpg");
        backgroundPanel.setLayout(new BorderLayout());

        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        // 메시지 레이블 설정
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        messageLabel.setPreferredSize(new Dimension(200, 25)); // 최대 너비 설정
        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton loginButton = new JButton("로그인");
        JButton signUpButton = new JButton("회원가입");

        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(new JLabel("아이디 :"), gbc);

        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        loginPanel.add(new JLabel("비밀번호 :"), gbc);

        gbc.gridy = 3;
        loginPanel.add(passwordField, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        loginPanel.add(messageLabel, gbc); // 메시지 레이블 위치 고정

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        loginPanel.add(loginButton, gbc);

        gbc.gridx = 1;
        loginPanel.add(signUpButton, gbc);

        backgroundPanel.add(loginPanel, BorderLayout.CENTER);

        timeLabel = new JLabel();
        timeLabel.setForeground(Color.black);
        timeLabel.setFont(new Font("Serif", Font.BOLD, 30));
        backgroundPanel.add(timeLabel, BorderLayout.NORTH);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
        timer.start();

        layeredPane.add(backgroundPanel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                handleLogin(username, password);
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

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String time = sdf.format(new Date());
        timeLabel.setText(time);
    }

    private void handleLogin(String username, String password) {
        messageLabel.setText("");
        User user = userDatabase.get(username);

        if (user == null) {
            messageLabel.setText("아이디를 다시 입력해주세요.");
        } else if (!user.getPassword().equals(password)) {
            messageLabel.setText("비밀번호를 다시 입력해주세요.");
        } else {
            JOptionPane.showMessageDialog(this, "로그인 성공! 게임 메인 페이지로 이동합니다.");
        }
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
                userDatabase.put(username, new User(username, password, name, birthdate));
                JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!");
            } else {
                JOptionPane.showMessageDialog(this, "아이디와 비밀번호는 필수 입력 사항입니다.");
            }
        }
    }

    private static class User {
        private String username;
        private String password;
        private String name;
        private String birthdate;

        public User(String username, String password, String name, String birthdate) {
            this.username = username;
            this.password = password;
            this.name = name;
            this.birthdate = birthdate;
        }

        public String getPassword() {
            return password;
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

    public static void main(String[] args) {
        new LoginGUI();
    }
}
