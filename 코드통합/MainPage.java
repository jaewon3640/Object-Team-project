package Userinfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MainPage extends JFrame {

    public MainPage() {
        setTitle("슬롯 머신 게임 - 메인 페이지");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 사용자 정보를 읽어오기 위해 파일에서 데이터를 로드
        User user = readUserFromFile("user.txt");

        // 환영 메시지 라벨
        JLabel welcomeLabel = new JLabel("메인 페이지에 오신 것을 환영합니다, " + user.getName() + "님!");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 칩 교환 버튼
        JButton chipExchangeButton = new JButton("칩 교환하기");
        chipExchangeButton.setFont(new Font("Serif", Font.BOLD, 18));
        chipExchangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 메인 페이지를 닫고 chipExchangePage를 엽니다
                chipExchangePage exchangePage = new chipExchangePage();
                exchangePage.startGUI();
                exchangePage.setVisible(true);
            }
        });

        // Userinfo 페이지로 이동하는 버튼
        JButton userInfoButton = new JButton("Userinfo로 이동");
        userInfoButton.setFont(new Font("Serif", Font.BOLD, 18));
        userInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Userinfo 페이지로 이동
                Userinfo userInfoPage = new Userinfo(user); // User 객체 전달
                userInfoPage.startGUI();
            }
        });

        // Ranking 페이지로 이동하는 버튼
        JButton rankingButton = new JButton("Ranking으로 이동");
        rankingButton.setFont(new Font("Serif", Font.BOLD, 18));
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ranking 페이지로 이동
                Ranking rankingPage = new Ranking(); // RankingPage 객체 생성
                rankingPage.setVisible(true);  // RankingPage 화면 보이기
            }
        });

        // 레이아웃 설정
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.CENTER);

        // 버튼 패널에 칩 교환, Userinfo, Ranking 버튼 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(chipExchangeButton);
        buttonPanel.add(userInfoButton);
        buttonPanel.add(rankingButton); // Ranking 버튼 추가

        // 버튼 패널을 하단에 배치
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private User readUserFromFile(String filePath) {
        User user = null;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = null;
            String lastLine = null;
            
            // 파일을 한 줄씩 읽으며 마지막 줄을 찾음
            while ((line = reader.readLine()) != null) {
                lastLine = line;  // 마지막 줄을 계속 업데이트
            }
            
            // 마지막 줄이 존재하면 이를 파싱하여 User 객체 생성
            if (lastLine != null) {
                String[] userData = lastLine.split(" "); // 공백으로 구분된 데이터 파싱
                if (userData.length >= 4) {
                    // user.txt의 데이터는 "아이디 비밀번호 이름 생일" 순서
                    user = new User(userData[0], userData[1], userData[2], userData[3]);
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        // MainPage 독립적으로 테스트
        new MainPage();
    }
}
