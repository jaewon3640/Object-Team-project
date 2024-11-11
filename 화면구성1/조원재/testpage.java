package testslot;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class testpage extends JFrame {
	private JLabel[][] reels;
	private Timer timer;
	private Random random;
	private String[] symbols = { "1", "2", "3","4","5" };
	private JLabel balanceLabel;
	private JLabel totalLabel;
	private JLabel clockLabel;
	private int balance = 1000;
	private JComboBox<Integer> paylineSelector;
	private Map<Integer, Double> paylineRTP; // 페이라인별 RTP 저장
	private final Map<Integer, int[][]> paylines = new HashMap<>();
	private int totalWinnings = 0;
	
	
	
	public void testpage() {
		initializePaylines();
		setTitle("Slot Machine");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		random = new Random();

		// 페이라인별 RTP 값 초기화
		paylineRTP = new HashMap<>();
		paylineRTP.put(1, 0.9678);   
		paylineRTP.put(3, 0.9588);   
		paylineRTP.put(5, 0.9512);  
		paylineRTP.put(7, 0.9382);	
		paylineRTP.put(9, 0.912);  
		paylineRTP.put(10, 0.8652); 

		// 시계 라벨 초기화
		clockLabel = new JLabel();
		clockLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		updateClock();
		Timer clockTimer = new Timer(1000, e -> updateClock());
		clockTimer.start();

		//시계 표시 패널
		JPanel topPanel = new JPanel(new BorderLayout());
		totalLabel = new JLabel("누적 당첨 점수 : " + 0);
		totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		topPanel.add(clockLabel, BorderLayout.WEST);
		topPanel.add(totalLabel, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		// 슬롯 화면 패널
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

		// 버튼과 정보 패널
		JPanel controlPanel = new JPanel();
		balanceLabel = new JLabel("Balance: " + balance + "점");

		Integer[] paylines = { 1, 3, 5, 7, 9, 10 };
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

	private void initializePaylines() { // 페이라인 별 행령 당첨 모양
		paylines.put(1, new int[][] { { 0, 0 }, { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 } });
		paylines.put(2, new int[][] { { 3, 0 }, { 3, 1 }, { 3, 2 }, { 3, 3 }, { 3, 4 } });
		paylines.put(3, new int[][] { { 1, 0 }, { 1, 1 }, { 1, 2 }, { 1, 3 }, { 1, 4 } });
		paylines.put(4, new int[][] { { 2, 0 }, { 2, 1 }, { 2, 2 }, { 2, 3 }, { 2, 4 } });
		paylines.put(5, new int[][] { { 0, 0 }, { 1, 1 }, { 2, 2 }, { 1, 3 }, { 0, 4 } });
		paylines.put(6, new int[][] { { 3, 0 }, { 2, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 } });
		paylines.put(7, new int[][] { { 2, 0 }, { 1, 1 }, { 0, 2 }, { 1, 3 }, { 2, 4 } });
		paylines.put(8, new int[][] { { 1, 0 }, { 2, 1 }, { 3, 2 }, { 2, 3 }, { 1, 4 } });
		paylines.put(9, new int[][] { { 0, 0 }, { 1, 1 }, { 0, 2 }, { 1, 3 }, { 0, 4 } });
		paylines.put(10, new int[][] { { 3, 0 }, { 2, 1 }, { 3, 2 }, { 2, 3 }, { 3, 4 } });

	}

	private void updateClock() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		clockLabel.setText("Current Time: " + formatter.format(new Date()));
	}
	
	private void startGame() {  
	    int selectedPaylines = (Integer) paylineSelector.getSelectedItem(); // 선택된 페이라인 수
	    int deduction = 10 * selectedPaylines; // 페이라인 수에 따른 차감되는 Balance 계산

	    if (balance < deduction) { // 잔액이 부족할 경우 메시지 출력
	        JOptionPane.showMessageDialog(this, "잔액이 부족합니다.");
	        return;
	    }

	    balance -= deduction; // 계산된 차감량만큼 Balance 감소
	    balanceLabel.setText("Balance: " + balance + "점"); // 잔액 라벨 업데이트
	    startSpin(); // 게임 회전 시작
	}
	
	/*
	 * protected void betMax() { if (balance > 0) { int maxBet = balance;
	 * totalBettingAmount += maxBet; balance = 0; balanceLabel.setText("Balance: $"
	 * + balance); bettingAmountLabel.setText("Total Betting Amount: $" +
	 * totalBettingAmount); startSpin(); } else {
	 * JOptionPane.showMessageDialog(this, "Not enough balance to bet max."); } }
	 */ // 나중에 쌓이는 배팅금액이 정의되면 다시 되살리기

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
	    double rtp = paylineRTP.getOrDefault(selectedPaylines, 1.0); // 선택한 페이라인에 해당하는 RTP 가중치 가져오기
	    int winnings = 0;
	    
	    

	    // 선택한 페이라인 수만큼 모든 페이라인에 동일한 RTP 가중치를 적용하여 당첨 점수 계산
	    for (int line = 1; line <= selectedPaylines; line++) {
	        int[][] paylinePattern = paylines.get(line); // 해당 페이라인의 패턴 가져오기

	        if (paylinePattern != null && checkPaylineMatch(paylinePattern)) { // 패턴이 일치하면 당첨
	            highlightWin(paylinePattern); // 당첨된 페이라인 강조
	            double multiplier = getMultiplier(line); // 페이라인에 따른 배수
	            winnings += (int) (10 * rtp + (multiplier*10)) ; // 당첨 점수 계산
	        }
	    }

	    // 당첨이 있을 경우 점수 및 잔액 업데이트
	    if (winnings > 0) {
	        balance += winnings; // 당첨 점수와 추가 점수
	        totalWinnings += winnings; // 당첨된 점수 따로 누적
	        JOptionPane.showMessageDialog(this, "당첨! " + winnings + "점 획득!");
	    } else {
	        JOptionPane.showMessageDialog(this, "아쉽게도 당첨되지 않았습니다.");
	    }

	    balanceLabel.setText("Balance: " + balance + "점"); // 잔액 라벨 업데이트
	    totalLabel.setText("누적 당첨 점수 : " + totalWinnings); // 누적 당첨 점수 업데이트
	    }

	private double getMultiplier(int line) {
		switch (line) {
		case 1:
			return 10;
		case 3:
			return 3;
		case 5:
			return 2;
		case 7:
			return 1.5;
		case 9:
			return 1;
		case 10:
			return 0.8;
		default:
			return 1;
		}
	}

	// 페이라인 패턴이 맞는지 확인하는 메서드
	private boolean checkPaylineMatch(int[][] paylinePattern) {
		for (int[] pos : paylinePattern) {
			String symbol = reels[pos[0]][pos[1]].getText();
			if (!symbol.equals(reels[paylinePattern[0][0]][paylinePattern[0][1]].getText())) {
				return false;
			}
		}
		return true; // 모든패턴이 맞으면 true
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
		System.exit(0);
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