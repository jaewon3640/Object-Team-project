package united;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;  // Correct List import from java.util

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class StartScreen extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public StartScreen() {
        setTitle("Retro Slot Machine");
        setSize(850, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 카드 레이아웃 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // StartScreen 패널 생성
        JPanel startPanel = createStartPanel();
        mainPanel.add(startPanel, "StartScreen");

        // LoginPage 패널 생성
        JPanel loginPanel = createLoginPanel();
        mainPanel.add(loginPanel, "LoginPage");

        // 메인 패널 설정
        getContentPane().add(mainPanel);
        setVisible(true);
    }

    private JPanel createStartPanel() {
        JPanel startPanel = new JPanel(new GridBagLayout());
        startPanel.setBackground(Color.BLACK); // 배경색 설정

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(30, 0, 20, 0); // 간격 줄이기 (위아래 각각 10 픽셀)
        gbc.gridx = 0; // 모든 컴포넌트를 중앙에 배치
        gbc.anchor = GridBagConstraints.CENTER;

        // "SLOT_MACHINE" 제목
        JLabel titleLabel = new JLabel("SLOT_MACHINE");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 48)); // 글자 크기 키움
        titleLabel.setForeground(Color.YELLOW);
        gbc.gridy = 0; // 첫 번째 행
        startPanel.add(titleLabel, gbc);

        // 팀 정보
        JLabel teamLabel = new JLabel("객체지향프로그래밍 7조");
        teamLabel.setFont(new Font("Monospaced", Font.BOLD, 24)); // 글자 크기 키움
        teamLabel.setForeground(Color.YELLOW);
        gbc.gridy = 1; // 두 번째 행
        startPanel.add(teamLabel, gbc);

        // "HAVE A FUN 😊😊" 메시지
        JLabel funLabel = new JLabel("HAVE A FUN 😊😊");
        funLabel.setFont(new Font("Monospaced", Font.BOLD, 20)); // 글자 크기 키움
        funLabel.setForeground(Color.YELLOW);
        gbc.gridy = 2; // 세 번째 행
        startPanel.add(funLabel, gbc);

        // "INSERT COIN" 버튼
        JButton startButton = new JButton("[INSERT COIN]");
        startButton.setBackground(Color.BLACK);
        startButton.setForeground(Color.YELLOW);
        startButton.setFont(new Font("Monospaced", Font.BOLD, 20)); // 버튼 글자 크기 키움
        startButton.setFocusPainted(false);

        // 버튼 깜빡임 효과
        Timer blinkTimer = new Timer(500, e -> {
            if (startButton.getForeground().equals(Color.YELLOW)) {
                startButton.setForeground(Color.BLACK);
            } else {
                startButton.setForeground(Color.YELLOW);
            }
        });
        blinkTimer.start();

        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "LoginPage");
        });

        gbc.gridy = 3; // 네 번째 행
        startPanel.add(startButton, gbc);

        return startPanel;
    }



    // LoginPage 생성
    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(null); // 절대 배치 사용
        loginPanel.setBackground(Color.BLACK);

        // "LOGIN" 제목
        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 40));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setBounds(275, 100, 300, 50); // 위치와 크기 설정
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(titleLabel);

        // 아이디 레이블
        JLabel usernameLabel = new JLabel("아이디:");
        usernameLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        usernameLabel.setForeground(Color.YELLOW);
        usernameLabel.setBounds(250, 200, 100, 30);
        loginPanel.add(usernameLabel);

        // 아이디 입력 필드
        JTextField usernameField = new JTextField();
        usernameField.setFont(new Font("Monospaced", Font.PLAIN, 20));
        usernameField.setBounds(350, 200, 200, 30);
        loginPanel.add(usernameField);

        // 비밀번호 레이블
        JLabel passwordLabel = new JLabel("비밀번호:");
        passwordLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        passwordLabel.setForeground(Color.YELLOW);
        passwordLabel.setBounds(250, 260, 100, 30);
        loginPanel.add(passwordLabel);

        // 비밀번호 입력 필드
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Monospaced", Font.PLAIN, 20));
        passwordField.setBounds(350, 260, 200, 30);
        loginPanel.add(passwordField);

        // 메시지 레이블 (로그인 실패 시 출력)
        JLabel messageLabel = new JLabel("");
        messageLabel.setForeground(Color.RED);
        messageLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        messageLabel.setBounds(250, 320, 300, 30);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginPanel.add(messageLabel);

        // 로그인 버튼
        JButton loginButton = createRetroButton("로그인", 250, 380);
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            User user = readUserFromFile("user.txt", username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(null, "로그인 성공! 게임 메인 페이지로 이동합니다.");
                MainPage mainPage = new MainPage(user, cardLayout, mainPanel);
                mainPanel.add(mainPage, "MainPage");
                cardLayout.show(mainPanel, "MainPage");
            } else {
                messageLabel.setText("로그인 실패, 다시 시도하세요.");
            }
        });
        loginPanel.add(loginButton);

        // 회원가입 버튼
        JButton signUpButton = createRetroButton("회원가입", 470, 380);
        signUpButton.addActionListener(e -> handleSignUp());
        loginPanel.add(signUpButton);

        return loginPanel;
    }

    // 레트로 버튼 스타일 메서드 재사용
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

    static protected User readUserFromFile(String filePath, String username, String password) {
        User user = null;
        try {
            String line;
            BufferedReader bufferReader = new BufferedReader(new FileReader(new File(filePath)));
            List<String> lines = new ArrayList<>();

            while ((line = bufferReader.readLine()) != null) {
                lines.add(line);
            }
            bufferReader.close();

            for (String str : lines) {
                String[] userData = str.split(" ");
                if (userData[0].equals(username) && userData[1].equals(password)) {
                    user = new User(userData[0], userData[1], userData[2], userData[3]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static void main(String[] args) {
        new StartScreen(); 
    }
}
