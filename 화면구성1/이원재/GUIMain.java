package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class GUIMain {
    private static final int ROWS = 5;
    private static final int COLS = 5;
    private static final String[] SYMBOLS = {"2", "3", "4", "5", "6", "1"};
    private static final int SPIN_COST = 10;

    private JFrame mainFrame;
    private JPanel slotPanel;
    private JLabel[][] slotLabels;
    private JButton spinButton;
    private JButton scoreSummaryButton;
    private JButton resetButton;
    private JButton historyButton; // 게임 기록 보기 버튼
    private JLabel scoreLabel;
    private JLabel chipLabel;
    private int chips = 100;
    private int score = 0;
    private int gameCount = 0; // 게임 횟수 추적
    private Random random;
    private ArrayList<String> gameHistory; // 게임 기록 리스트

    public GUIMain() {
        mainFrame = new JFrame("슬롯 머신 게임");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 400);
        
        slotPanel = new JPanel(new GridLayout(ROWS, COLS));
        slotLabels = new JLabel[ROWS][COLS];
        random = new Random();
        gameHistory = new ArrayList<>(); // 기록 리스트 초기화

        // 슬롯 화면 초기화
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                slotLabels[i][j] = new JLabel("", SwingConstants.CENTER);
                slotLabels[i][j].setFont(new Font("Serif", Font.BOLD, 24));
                slotLabels[i][j].setOpaque(true);
                slotLabels[i][j].setBackground(Color.WHITE);
                slotPanel.add(slotLabels[i][j]);
            }
        }

        spinButton = new JButton("스핀");
        scoreSummaryButton = new JButton("점수 총합 보기");
        resetButton = new JButton("재시작");
        historyButton = new JButton("게임 기록 보기"); // 게임 기록 버튼

        // 점수 레이블 및 칩 레이블 설정
        scoreLabel = new JLabel("점수: " + score);
        chipLabel = new JLabel("칩: " + chips);

        JPanel controlPanel = new JPanel();
        controlPanel.add(spinButton);
        controlPanel.add(scoreSummaryButton);
        controlPanel.add(resetButton);
        controlPanel.add(historyButton); // 기록 버튼 추가
        controlPanel.add(scoreLabel);
        controlPanel.add(chipLabel);

        mainFrame.add(slotPanel, BorderLayout.CENTER);
        mainFrame.add(controlPanel, BorderLayout.SOUTH);

        mainFrame.setVisible(true);

        // 스핀 버튼 액션 리스너
        spinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spinSlots();
            }
        });

        // 점수 총합 버튼 액션 리스너
        scoreSummaryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showScoreSummary();
            }
        });

        // 재시작 버튼 액션 리스너
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        // 게임 기록 보기 버튼 액션 리스너
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showGameHistory();
            }
        });
    }

    // 슬롯 스핀 기능
    private void spinSlots() {
        if (chips >= SPIN_COST) {
            chips -= SPIN_COST;
            chipLabel.setText("칩: " + chips);
            int spinScore = random.nextInt(100); // 임의의 점수 생성
            score += spinScore;
            scoreLabel.setText("점수: " + score);
            updateSlots();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "칩이 부족합니다.");
        }
    }

    // 슬롯 화면 업데이트
    private void updateSlots() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int symbolIndex = random.nextInt(SYMBOLS.length);
                slotLabels[i][j].setText(SYMBOLS[symbolIndex]);
            }
        }
    }

    // 점수 총합 페이지 표시
    private void showScoreSummary() {
        JFrame scoreSummaryFrame = new JFrame("점수 총합");
        scoreSummaryFrame.setSize(300, 200);
        scoreSummaryFrame.setLayout(new GridLayout(2, 1));

        JLabel totalScoreLabel = new JLabel("현재 점수 총합: " + score, SwingConstants.CENTER);
        JLabel totalChipsLabel = new JLabel("현재 남은 칩: " + chips, SwingConstants.CENTER);

        scoreSummaryFrame.add(totalScoreLabel);
        scoreSummaryFrame.add(totalChipsLabel);
        scoreSummaryFrame.setVisible(true);
    }

    // 게임 재시작 기능
    private void resetGame() {
        // 이전 게임 기록 저장
        gameCount++;
        String record = gameCount + "번째 게임 점수 : " + score + " / 남은 코인 : " + chips + "개";
        gameHistory.add(record);

        // 게임 초기화
        chips = 100;
        score = 0;
        chipLabel.setText("칩: " + chips);
        scoreLabel.setText("점수: " + score);

        // 슬롯 화면 초기화
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                slotLabels[i][j].setText("");
            }
        }
    }

    // 게임 기록 보기
    private void showGameHistory() {
        JFrame historyFrame = new JFrame("게임 기록");
        historyFrame.setSize(300, 400);
        historyFrame.setLayout(new GridLayout(gameHistory.size(), 1));

        for (String record : gameHistory) {
            JLabel historyLabel = new JLabel(record, SwingConstants.CENTER);
            historyFrame.add(historyLabel);
        }

        historyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new GUIMain();
    }
}
