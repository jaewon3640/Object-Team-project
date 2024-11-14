package Userinfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends JFrame {

    public MainPage(String username) {
        setTitle("Slot Machine Game - Main Page");
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("메인 페이지에 오신 것을 환영합니다, " + username + "님!");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 20));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // 칩교환하기 버튼 만들기

        JButton ChipExchangeButton = new JButton("칩 교환하기");
        ChipExchangeButton.setFont(new Font("Serif", Font.BOLD, 18));

        // 버튼 클릭 시 chipExchangePage로 이동
        ChipExchangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 메인 페이지를 닫고 chipExchangePage를 엽니다
                dispose();
                chipExchangePage exchangePage = new chipExchangePage();
                exchangePage.startGUI();
                exchangePage.setVisible(true);
            }
        });

        // 레이아웃 설정
        setLayout(new BorderLayout());
        add(welcomeLabel, BorderLayout.CENTER);
        add(ChipExchangeButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        // Test MainPage independently
        new MainPage("테스트 사용자");
    }
}
