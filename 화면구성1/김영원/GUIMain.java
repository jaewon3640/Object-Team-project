package swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
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
    private JLabel scoreLabel;
    private JLabel chipLabel;
    private int chips = 100;  // 초기 칩 수
    private int score = 0;
    private Random random;

    public GUIMain() {
        mainFrame = new JFrame("Slot Machine Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setPreferredSize(new Dimension(600, 700));

        // 슬롯 패널 초기화
        slotPanel = new JPanel(new GridLayout(ROWS, COLS, 5, 5));
        slotPanel.setBackground(Color.BLACK);
        slotLabels = new JLabel[ROWS][COLS];
        
        // 슬롯 레이블 초기화
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                slotLabels[row][col] = new JLabel("-", SwingConstants.CENTER);
                slotLabels[row][col].setFont(new Font("Arial", Font.BOLD, 24));
                slotLabels[row][col].setOpaque(true);
                slotLabels[row][col].setBackground(Color.WHITE);
                slotPanel.add(slotLabels[row][col]);
            }
        }

        // 스핀 버튼 초기화
        spinButton = new JButton("Spin");
        spinButton.setFont(new Font("Arial", Font.BOLD, 20));
        spinButton.setPreferredSize(new Dimension(100, 40));
        spinButton.addActionListener(new SpinAction());

        // 칩 및 점수 레이블 초기화
        chipLabel = new JLabel("Chips: " + chips, SwingConstants.CENTER);
        chipLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel = new JLabel("Score: " + score, SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        // 메인 프레임 구성
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.add(chipLabel);
        infoPanel.add(scoreLabel);

        mainFrame.setLayout(new BorderLayout());
        mainFrame.add(slotPanel, BorderLayout.CENTER);
        mainFrame.add(spinButton, BorderLayout.SOUTH);
        mainFrame.add(infoPanel, BorderLayout.NORTH);

        mainFrame.pack();
        mainFrame.setVisible(true);

        random = new Random();
    }

    // 스핀 버튼 클릭 액션
    private class SpinAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (chips < SPIN_COST) {
                JOptionPane.showMessageDialog(mainFrame, "칩이 부족합니다. 게임을 종료합니다.", "칩 부족", JOptionPane.WARNING_MESSAGE);
                spinButton.setEnabled(false);
                return;
            }

            chips -= SPIN_COST;
            chipLabel.setText("Chips: " + chips);

            spinSlots();
            calculateScore();
        }
    }

    // 슬롯을 랜덤 기호로 설정
    private void spinSlots() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int symbolIndex = random.nextInt(SYMBOLS.length);
                slotLabels[row][col].setText(SYMBOLS[symbolIndex]);
            }
        }
    }

    // 점수 계산
    private void calculateScore() {
        int newScore = 0;

        // 각 행에 대해 점수 산정
        for (int row = 0; row < ROWS; row++) {
            Map<String, Integer> symbolCount = new HashMap<>();

            // 각 기호의 빈도를 계산
            for (int col = 0; col < COLS; col++) {
                String symbol = slotLabels[row][col].getText();
                symbolCount.put(symbol, symbolCount.getOrDefault(symbol, 0) + 1);
            }

            // 빈도에 따라 점수를 산정
            int rowScore = 0;
            for (int count : symbolCount.values()) {
                if (count == 5) {
                    rowScore = 10000;  // 잭팟
                    break;  // 잭팟이면 다른 조건은 무시
                } else if (count == 4) {
                    rowScore = Math.max(rowScore, 100);  // 4개 일치
                } else if (count == 3) {
                    rowScore = Math.max(rowScore, 10);   // 3개 일치
                } else if (count == 2) {
                    rowScore = Math.max(rowScore, 1);    // 2개 일치
                }
            }
            newScore += rowScore;
        }

        // 총 점수 업데이트
        score += newScore;
        scoreLabel.setText("Score: " + score);

        // 점수 결과 메시지 출력
        if (newScore > 0) {
            JOptionPane.showMessageDialog(mainFrame, "이번 점수: " + newScore, "점수 결과", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "일치하는 기호가 없습니다. 점수는 0입니다.", "점수 결과", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUIMain());
    }
}
