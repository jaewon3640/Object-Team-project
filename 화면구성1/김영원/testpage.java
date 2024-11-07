package testslot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class testpage extends JFrame {
    private JLabel[][] reels;
    private Timer timer;
    private Random random;
    private String[] symbols = { "1", "2", "3" }; // 예시 심볼
    private JLabel balanceLabel;
    private JLabel bettingAmountLabel; // 누적된 배팅 금액 표시
    private JLabel clockLabel; // 시계 라벨
    private int balance = 1000; // 초기 잔액 설정
    private int totalBettingAmount = 0; // 누적된 배팅 금액
    private JTextField betAmountField; // 사용자 입력 배팅 금액 필드

    public void testpage() {
        setTitle("Slot Machine");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        random = new Random();

        // 시계 라벨 초기화
        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        updateClock(); // 초기 시간 설정
        Timer clockTimer = new Timer(1000, e -> updateClock()); // 1초마다 시간 업데이트
        clockTimer.start();

        // 상단 배팅 금액과 시계 표시 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        bettingAmountLabel = new JLabel("Total Betting Amount: $" + totalBettingAmount);
        bettingAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        topPanel.add(clockLabel, BorderLayout.WEST); // 왼쪽에 시계 추가
        topPanel.add(bettingAmountLabel, BorderLayout.CENTER); // 중앙에 배팅 금액 추가
        add(topPanel, BorderLayout.NORTH);

        // 슬롯 화면 패널
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(3, 3)); // 3x3 슬롯 구성
        reels = new JLabel[3][3];

        // 각 슬롯 심볼을 초기화
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                reels[i][j] = new JLabel(symbols[0], SwingConstants.CENTER);
                reels[i][j].setFont(new Font("Arial", Font.PLAIN, 48));
                slotPanel.add(reels[i][j]);
            }
        }

        // 버튼과 정보 패널
        JPanel controlPanel = new JPanel();
        balanceLabel = new JLabel("Balance: $" + balance);
        betAmountField = new JTextField(5); // 배팅 금액 입력 필드
        JButton spinButton = new JButton("Spin");
        spinButton.setPreferredSize(new Dimension(150, 50)); // 버튼 크기 설정
        JButton betMaxButton = new JButton("BetMax");
        betMaxButton.setPreferredSize(new Dimension(150, 50)); // 버튼 크기 설정

        controlPanel.add(balanceLabel);
        controlPanel.add(new JLabel("Bet Amount: "));
        controlPanel.add(betAmountField);
        controlPanel.add(spinButton);
        controlPanel.add(betMaxButton);

        spinButton.addActionListener(e -> placeBetAndSpin());
        betMaxButton.addActionListener(e -> betMax());

        // 메인 화면에 슬롯과 컨트롤 패널 추가
        add(slotPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    // 시계 업데이트 메서드
    private void updateClock() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        clockLabel.setText("Current Time: " + formatter.format(new Date()));
    }

    protected void betMax() {
        if (balance > 0) {
            int maxBet = balance;
            totalBettingAmount += maxBet;
            balance = 0;
            balanceLabel.setText("Balance: $" + balance);
            bettingAmountLabel.setText("Total Betting Amount: $" + totalBettingAmount);
            startSpin();
        } else {
            JOptionPane.showMessageDialog(this, "Not enough balance to bet max.");
        }
    }

    private void placeBetAndSpin() {
        try {
            int betAmount = Integer.parseInt(betAmountField.getText());
            if (betAmount <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid bet amount.");
                return;
            }
            if (betAmount > balance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance for this bet.");
                return;
            }

            balance -= betAmount;
            totalBettingAmount += betAmount;
            balanceLabel.setText("Balance: $" + balance);
            bettingAmountLabel.setText("Total Betting Amount: $" + totalBettingAmount);

            startSpin();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid bet amount. Please enter a number.");
        }
    }

    private void startSpin() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        timer = new Timer(100, new ActionListener() {
            int ticks = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
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
        if (reels[1][0].getText().equals(reels[1][1].getText())
                && reels[1][1].getText().equals(reels[1][2].getText())) {
            balance += totalBettingAmount + 100;
            JOptionPane.showMessageDialog(this, "JackPot! You've won $" + (totalBettingAmount + 100) + "!");
            totalBettingAmount = 0;
        } else {
            JOptionPane.showMessageDialog(this, "No win this time.");
        }
        balanceLabel.setText("Balance: $" + balance);
        bettingAmountLabel.setText("Total Betting Amount: $" + totalBettingAmount);
    }
    
    private void createSlotGame() {
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
    	testpage();
    	pack();
    	setVisible(true);
    	
    }

    public static void main(String[] args) {
            testpage slot = new testpage();
            slot.createSlotGame();
    }
}
