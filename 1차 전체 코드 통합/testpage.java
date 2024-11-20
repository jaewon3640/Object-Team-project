package united;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.util.List;

public class testpage extends JFrame {
	private JLabel[][] reels;
	private Timer timer;
	private Random random;
	private String[] symbolPaths = { "imgs/cherry.png", "imgs/lemon.png", "imgs/banana.png", "imgs/watermelon.png",
			"imgs/golden-bell.png", "imgs/lucky 7.png", "imgs/singlebar.png" };
	private ImageIcon[] symbolIcons;
	private Map<String, ImageIcon> symbolImages;
	private JLabel balanceLabel;
	private JLabel totalLabel;
	private JLabel clockLabel;
	private int balance = 1000000;
	private JComboBox<Integer> paylineSelector;
	private Map<Integer, Double> paylineRTP; // 페이라인별 RTP 저장
	private final Map<Integer, int[][]> paylines = new HashMap<>();
	private int totalWinnings = 0;

	private String currentUserId; // 로그인된 사용자 ID

	// 생성자에서 로그인된 사용자 ID를 받아서 초기화
	public testpage(String loggedInUserId) {
		this.currentUserId = loggedInUserId; // 로그인된 사용자 ID 초기화
		initializeSymbols(); // 심볼 아이콘 초기화
		initializePaylines();
		setTitle("Slot Machine");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		random = new Random();

		// 페이라인별 RTP 값 초기화
		paylineRTP = new HashMap<>();
		paylineRTP.put(1, 0.9878); // 1라인 RTP를 높임
		paylineRTP.put(3, 0.9788); // 3라인 RTP를 높임
		paylineRTP.put(5, 0.9512); // 5라인 RTP를 높임
		paylineRTP.put(7, 0.9482); // 7라인 RTP를 높임
		paylineRTP.put(9, 0.922); // 9라인 RTP를 높임
		paylineRTP.put(10, 0.8852); // 10라인 RTP를 높임

		// 시계 라벨 초기화
		clockLabel = new JLabel();
		clockLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		updateClock();
		Timer clockTimer = new Timer(1000, e -> updateClock());
		clockTimer.start();

		// 시계 표시 패널
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
				reels[i][j] = new JLabel(symbolPaths[0], SwingConstants.CENTER);
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

	

	private void initializeSymbols() {
	    symbolIcons = new ImageIcon[symbolPaths.length];
	    for (int i = 0; i < symbolPaths.length; i++) {
	        try {
	            ImageIcon originalIcon = new ImageIcon(symbolPaths[i]);
	            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	            symbolIcons[i] = new ImageIcon(scaledImage);
	        } catch (Exception e) {
	            System.err.println("이미지 로드 실패: " + symbolPaths[i] + " - " + e.getMessage());
	            symbolIcons[i] = null; // 실패한 경우 null로 설정
	        }
	    }
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

	private void startGame() { // 페이라인별로 차감되는 Balance를 다르게 설정
		int selectedPayline = (Integer) paylineSelector.getSelectedItem(); // 선택된 페이라인 수
		int deductionAmount = payLineDeduction(selectedPayline); // 페이라인에 따른 차감 포인트 계산

		// 차감할 포인트가 현재 Balance보다 높으면 잔액 부족 메시지 출력
		if (balance < deductionAmount) {
			JOptionPane.showMessageDialog(this, "잔액이 부족합니다.");
			return;
		}

		balance -= deductionAmount; // Balance에서 페이라인에 따라 차감
		balanceLabel.setText("Balance: " + balance + "점");
		startSpin();
	}

	// 페이라인별로 차감할 포인트를 반환하는 메서드
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
	                    int index = random.nextInt(symbolIcons.length);
	                    if (symbolIcons[index] != null) {
	                        reels[i][j].setIcon(symbolIcons[index]);
	                    } else {
	                        System.err.println("Null icon for index: " + index); // 디버깅 로그
	                        reels[i][j].setIcon(new ImageIcon()); // 빈 아이콘 설정
	                    }
	                    reels[i][j].setText(null);
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

		// 당첨이 있을 경우 점수 및 잔액 업데이트
		if (winnings > 0) {
			balance += winnings; // 당첨 점수 반영
			totalWinnings += winnings; // 누적 당첨 점수 추가
			JOptionPane.showMessageDialog(this, "당첨! " + winnings + "점 획득!");
		} else {
			JOptionPane.showMessageDialog(this, "아쉽게도 당첨되지 않았습니다.");
		}
		balanceLabel.setText("Balance: " + balance + "점"); // 잔액 라벨 업데이트
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

	// 페이라인 패턴이 맞는지 확인하는 메서드
	private boolean checkPaylineMatch(int[][] paylinePattern) {
	    // 첫 번째 위치의 아이콘을 기준으로 비교
	    ImageIcon firstIcon = (ImageIcon) reels[paylinePattern[0][0]][paylinePattern[0][1]].getIcon();
	    if (firstIcon == null) {
	        return false; // 첫 번째 아이콘이 없으면 일치하지 않음
	    }

	    for (int[] pos : paylinePattern) {
	        ImageIcon currentIcon = (ImageIcon) reels[pos[0]][pos[1]].getIcon();
	        if (!firstIcon.equals(currentIcon)) {
	            return false; // 하나라도 다르면 false
	        }
	    }
	    return true; // 모두 같으면 true
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

		// 현재 사용자 정보를 읽어와서 누적 점수를 반영
		List<userread> userData = readUserFile("user.txt");

		// 로그인된 사용자 ID를 사용
		String currentUserId = this.currentUserId; // 이미 로그인 시 전달된 currentUserId 사용

		boolean userFound = false;
		for (userread user : userData) {
			if (user.getId().equals(currentUserId)) {
				user.setScore(user.getScore() + totalWinnings); // 누적 점수 추가
				userFound = true;
				break; // 해당 사용자만 업데이트하고 반복문 종료
			}
		}

		// 사용자가 발견되지 않았을 경우
		if (!userFound) {
			JOptionPane.showMessageDialog(this, "로그인된 사용자 정보를 찾을 수 없습니다.");
		} else {
			// 사용자 정보를 다시 파일에 저장
			writeUserFile(userData);

			// Ranking 화면으로 이동
			new Ranking();
			setVisible(false);
			dispose(); // JFrame 자원 해제
			totalWinnings = 0; // 누적 당첨 점수 초기화
		}
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

	// 사용자 데이터를 파일에 저장하는 메서드
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

	private void createSlotGame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		// 예시로 로그인된 사용자 ID를 지정합니다. 실제로는 로그인 화면에서 이 값을 받아야 합니다.
		String loggedInUserId = "exampleUser"; // 로그인된 사용자 ID

		// 생성자에 로그인된 사용자 ID를 전달
		testpage slot = new testpage(loggedInUserId);
		slot.initializeSymbols();
		slot.createSlotGame();
	}
}