package Userinfo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

public class LoginGUI extends JFrame {
    private JTextField usernameField;        // 아이디 입력 필드
    private JPasswordField passwordField;    // 비밀번호 입력 필드
    private JLabel messageLabel;             // 로그인 상태 메시지 레이블
    private JLabel timeLabel;                // 현재 시간 레이블
    private Map<String, User> userDatabase;  // 사용자 데이터베이스 저장

    public LoginGUI() {
        // 사용자 데이터베이스 초기화
        userDatabase = new HashMap<>();

        // 윈도우 창 설정
        setTitle("Slot Machine Game - Login");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // 레이어드 패인과 배경 패널 설정
        JLayeredPane layeredPane = new JLayeredPane();
        setContentPane(layeredPane);
        layeredPane.setLayout(new BorderLayout());

        BackgroundPanel backgroundPanel = new BackgroundPanel("image.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        layeredPane.add(backgroundPanel, BorderLayout.CENTER);

        // 로그인 패널 설정
        JPanel loginPanel = createLoginPanel();
        backgroundPanel.add(loginPanel, BorderLayout.CENTER);

        // 시간 표시 레이블 및 타이머 설정
        timeLabel = new JLabel();
        timeLabel.setForeground(Color.black);
        timeLabel.setFont(new Font("Serif", Font.BOLD, 30));
        backgroundPanel.add(timeLabel, BorderLayout.NORTH);
        startTimer();

        setVisible(true);
    }

    // 로그인 패널 생성 메서드
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);

        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);
        messageLabel.setPreferredSize(new Dimension(200, 25));
        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JButton loginButton = new JButton("로그인");
        JButton signUpButton = new JButton("회원가입");

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginPanel.add(new JLabel("아이디 :"), gbc);
        gbc.gridy = 1;
        loginPanel.add(usernameField, gbc);
        gbc.gridy = 2;
        loginPanel.add(new JLabel("비밀번호 :"), gbc);
        gbc.gridy = 3;
        loginPanel.add(passwordField, gbc);
        gbc.gridy = 4;
        loginPanel.add(messageLabel, gbc);
        
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 1;
        loginPanel.add(loginButton, gbc);
        gbc.gridx = 1;
        loginPanel.add(signUpButton, gbc);

        // 버튼 액션 설정
        loginButton.addActionListener(e -> handleLogin(usernameField.getText(), new String(passwordField.getPassword())));
        signUpButton.addActionListener(e -> handleSignUp());

        return loginPanel;
    }

    // 시간 업데이트 메서드
    private void startTimer() {
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        timeLabel.setText(sdf.format(new Date()));
    }

    // 로그인 처리 메서드
    private void handleLogin(String username, String password) {
        messageLabel.setText("");
        User user = userDatabase.get(username);

        if (user == null) {
            messageLabel.setText("아이디를 다시 입력해주세요.");
        } else if (!user.getPassword().equals(password)) {
            messageLabel.setText("비밀번호를 다시 입력해주세요.");
        } else {
            JOptionPane.showMessageDialog(this, "로그인 성공! 게임 메인 페이지로 이동합니다.");
            dispose();
            new MainPage(username).setVisible(true);
        }
    }

    // 회원가입 처리 메서드
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

    // 사용자 정보를 저장하는 User 클래스
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

    // 배경 이미지 설정을 위한 패널
    private class BackgroundPanel extends JPanel {
        private ImageIcon backgroundImage;

        public BackgroundPanel(String imagePath) {
            backgroundImage = new ImageIcon(imagePath);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
