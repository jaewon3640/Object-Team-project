package united;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;  // Import from java.util
import java.util.ArrayList;  // Import ArrayList

public class LoginGUI extends JFrame {
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private JLabel messageLabel;
    private JPanel mainContainer;
    private CardLayout cardLayout;

    public LoginGUI() {
        setTitle("Slot Machine Game - Login");
        setSize(850, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // CardLayout 초기화
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 로그인 화면 설정
        JPanel loginPanel = createLoginPanel();
        mainContainer.add(loginPanel, "Login");

        // 메인 페이지 설정
        JPanel mainPage = createMainPage();
        mainContainer.add(mainPage, "MainPage");

        getContentPane().add(mainContainer);

        // 기본 화면을 로그인 화면으로 설정
        cardLayout.show(mainContainer, "Login");

        setVisible(true);
    }

    // 로그인 패널 생성
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

        // 로그인 버튼 클릭 이벤트 처리
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                User user = readUserFromFile("user.txt", username, password);
                if (user != null) {
                    JOptionPane.showMessageDialog(null, "로그인 성공! 게임 메인 페이지로 이동합니다.");
                    cardLayout.show(mainContainer, "MainPage"); // MainPage로 전환
                } else {
                    JOptionPane.showMessageDialog(null, "로그인 실패, 다시 시도하세요.");
                }
            }
        });

        // 회원가입 버튼 클릭 이벤트
        signUpButton.addActionListener(e -> handleSignUp());

        return loginPanel;
    }

    // 메인 페이지 생성
    private JPanel createMainPage() {
        JPanel mainPage = new JPanel(new BorderLayout());
        JButton slotGameButton = new JButton("Slot Game");
        JButton chipExchangeButton = new JButton("Chip Exchange");

        slotGameButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Slot Game Clicked");
            // 슬롯 게임 페이지로 이동하는 코드를 추가할 수 있음
        });

        chipExchangeButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chip Exchange Clicked");
            // Chip Exchange 페이지로 이동하는 코드를 추가할 수 있음
        });

        mainPage.add(slotGameButton, BorderLayout.NORTH);
        mainPage.add(chipExchangeButton, BorderLayout.SOUTH);

        return mainPage;
    }

    // 회원가입 처리
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

    // 사용자 정보 읽기
    static protected User readUserFromFile(String filePath, String username, String password) {
        User user = null;
        try {
            String line = "";
            FileReader reader = new FileReader(new File(filePath));
            BufferedReader bufferReader = new BufferedReader(reader);
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

    // 사용자 정의 클래스
    static class User {
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

        // Getter와 Setter 추가 가능
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}
