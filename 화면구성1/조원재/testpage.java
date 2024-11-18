package Userinfo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class testpage extends JFrame {
    private JLabel[][] reels;
    private Timer timer;
    private Random random;
    private String[] symbols = {"1", "2", "3"};
    private JLabel balanceLabel;
    private JLabel totalLabel;
    private JLabel clockLabel;
    private int chips; // balance 대신 사용할 변수
    private JComboBox<Integer> paylineSelector;
    private Map<Integer, Double> paylineRTP;
    private final Map<Integer, int[][]> paylines = new HashMap<>();
    private int totalWinnings = 0;

    public testpage(int chips) {
        this.chips = chips;
        initializePaylines();
        setTitle("Slot Machine");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        random = new Random();

        paylineRTP = new HashMap<>();
        paylineRTP.put(1, 0.9678);
        paylineRTP.put(3, 0.9588);
        paylineRTP.put(5, 0.9512);
        paylineRTP.put(7, 0.9382);
        paylineRTP.put(9, 0.912);
        paylineRTP.put(10, 0.8652);

        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        updateClock();
        Timer clockTimer = new Timer(1000, e -> updateClock());
        clockTimer.start();

        JPanel topPanel = new JPanel(new BorderLayout());
        totalLabel = new JLabel("누적 당첨 점수 : " + 0);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(clockLabel, BorderLayout.WEST);
        topPanel.add(totalLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(4, 5));
        reels = new JLabel[4][5];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                reels[i][j] = new JLabel(symbols[0], SwingConstants.CENTER);
                reels[i][j].setFont(new Font("Arial", Font.PLAIN, 48));
                slotPanel.add(reels[i][j]);
            }
        }

        JPanel controlPanel = new JPanel();
        balanceLabel = new JLabel("Chips: " + chips + "개");

        Integer[] paylines = {1, 3, 5, 7, 9, 10};
        paylineSelector = new JComboBox<>(paylines);

        JButton spinButton = new JButton("Spin");
        spinButton.setPreferredSize(new Dimension(150, 50));
        JButton endGameButton = new JButton("End Game");

        controlPanel.add(totalLabel);
        controlPanel.add(balanceLabel);
        controlPanel.add(new JLabel("Paylines: "));
        controlPanel.add(paylineSelector);
        controlPanel.add(spinButton);
        controlPanel.add(endGameButton);

        spinButton.addActionListener(e -> startGame());
        endGameButton.addActionListener(e -> endGame());

        add(slotPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void initializePaylines() {
        paylines.put(1, new int[][]{{0, 0}, {0, 1}, {0, 2}, {0, 3}, {0, 4}});
        paylines.put(2, new int[][]{{3, 0}, {3, 1}, {3, 2}, {3, 3}, {3, 4}});
        paylines.put(3, new int[][]{{1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}});
        paylines.put(4, new int[][]{{2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}});
        paylines.put(5, new int[][]{{0, 0}, {1, 1}, {2, 2}, {1, 3}, {0, 4}});
        paylines.put(6, new int[][]{{3, 0}, {2, 1}, {1, 2}, {2, 3}, {3, 4}});
        paylines.put(7, new int[][]{{2, 0}, {1, 1}, {0, 2}, {1, 3}, {2, 4}});
        paylines.put(8, new int[][]{{1, 0}, {2, 1}, {3, 2}, {2, 3}, {1, 4}});
        paylines.put(9, new int[][]{{0, 0}, {1, 1}, {0, 2}, {1, 3}, {0, 4}});
        paylines.put(10, new int[][]{{3, 0}, {2, 1}, {3, 2}, {2, 3}, {3, 4}});
    }

    private void updateClock() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        clockLabel.setText("Current Time: " + formatter.format(new Date()));
    }

    private void startGame() {
        int selectedPayline = (Integer) paylineSelector.getSelectedItem(); // 선택된 페이라인 수
        int deductionAmount = payLineDeduction(selectedPayline); // 페이라인에 따른 차감 포인트 계산

        if (chips < deductionAmount) {
            JOptionPane.showMessageDialog(this, "잔액이 부족합니다.");
            return;
        }

        chips -= deductionAmount; // Chips에서 페이라인에 따라 차감
        balanceLabel.setText("Chips: " + chips + "개");
        startSpin();
    }

    int payLineDeduction(int payLine) {
        switch (payLine) {
            case 1:
                return 10; // 페이라인 1의 차감 포인트
            case 3:
                return 30; // 페이라인 3의 차감 포인트
            case 5:
                return 50; // 페이라인 5의 차감 포인트
            case 7:
                return 70; // 페이라인 7의 차감 포인트
            case 9:
                return 90; // 페이라인 9의 차감 포인트
            case 10:
                return 100; // 페이라인 10의 차감 포인트
            default:
                return 10; // 기본 차감 포인트 (정의되지 않은 경우)
        }
    }

    private void startSpin() {
        resetBorders();
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(100, new ActionListener() {
            int ticks = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 5; j++) {
                        int index = random.nextInt(symbols.length);
                        reels[i][j].setText(symbols[index]);
                    }
                }

                ticks++;
                if (ticks > 10) {
                    timer.stop();
                    checkWin();
                }
            }
        });

        timer.start();
    }

    private void checkWin() {
        int selectedPaylines = (Integer) paylineSelector.getSelectedItem(); // 선택된 페이라인 수
        int winnings = 0;
        int baseScore = 100; // 모든 페이라인에 대해 동일한 기본 점수

        for (int line = 1; line <= selectedPaylines; line++) {
            int[][] paylinePattern = paylines.get(line); // 해당 페이라인의 패턴 가져오기

            if (paylinePattern != null && checkPaylineMatch(paylinePattern)) { // 패턴이 일치하면 당첨
                highlightWin(paylinePattern); // 당첨된 페이라인 강조

                double multiplier = getMultiplier(selectedPaylines); // 선택된 페이라인 수에 따른 배수 계산
                winnings += (int) (baseScore * multiplier); // 배수를 적용한 당첨 점수 계산
            }
        }

        if (winnings > 0) {
            chips += winnings; // 당첨 점수 반영
            totalWinnings += winnings; // 누적 당첨 점수 추가
            JOptionPane.showMessageDialog(this, "당첨! " + winnings + "점 획득!");
        } else {
            JOptionPane.showMessageDialog(this, "아쉽게도 당첨되지 않았습니다.");
        }
        balanceLabel.setText("Chips: " + chips + "개"); // 잔액 라벨 업데이트
        totalLabel.setText("누적 당첨 점수 : " + totalWinnings); // 누적 당첨 점수 업데이트
    }

    private double getMultiplier(int selectedPaylines) {
        switch (selectedPaylines) {
            case 1:
                return 10.0;
            case 3:
                return 5.0;
            case 5:
                return 3.0;
            case 7:
                return 2.0;
            case 9:
                return 1.5;
            case 10:
                return 1.0;
            default:
                return 1.0;
        }
    }

    private boolean checkPaylineMatch(int[][] paylinePattern) {
        for (int[] pos : paylinePattern) {
            String symbol = reels[pos[0]][pos[1]].getText();
            if (!symbol.equals(reels[paylinePattern[0][0]][paylinePattern[0][1]].getText())) {
                return false;
            }
        }
        return true; // 모든 패턴이 맞으면 true
    }

    private void highlightWin(int[][] positions) {
        for (int[] pos : positions) {
            reels[pos[0]][pos[1]].setBorder(new LineBorder(Color.RED, 3));
        }
    }

    private void resetBorders() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                reels[i][j].setBorder(null);
            }
        }
    }

    private void endGame() {
        JOptionPane.showMessageDialog(this, "게임을 종료합니다.");

        List<userread> userData = readUserFile("user.txt");
        for (userread user : userData) {
            user.score += totalWinnings; // 누적 점수 추가
        }

        writeUserFile(userData);

        new Ranking();
        setVisible(false);
        dispose(); // JFrame 자원 해제
        totalWinnings = 0;
    }

    private List<userread> readUserFile(String filePath) {
        List<userread> userData = new ArrayList<>();

        try (Scanner filein = new Scanner(new File(filePath))) {
            while (filein.hasNext()) {
                userread user = new userread();
                user.read(filein);
                userData.add(user);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return userData;
    }

    private void writeUserFile(List<userread> userData) {
        try (PrintWriter writer = new PrintWriter(new File("user.txt"))) {
            for (userread user : userData) {
                writer.printf("%s %s %s %s %d\n", user.getId(), user.getPassword(), user.getName(), user.getBirthdate(),
                        user.getScore());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void createSlotGame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        testpage slot = new testpage(10000); // 테스트를 위한 기본 칩 값 설정
        slot.createSlotGame();
    }
}