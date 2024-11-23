package Userinfo;

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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class testpage extends JFrame {
    private JLabel[][] reels;
    private Timer timer;
    private Random random;
    private String[] symbolPaths = { "imgs/cherry.png", "imgs/lemon.png", "imgs/banana.png", "imgs/watermelon.png",
            "imgs/golden-bell.png", "imgs/lucky 7.png", "imgs/singlebar.png" };
    private ImageIcon[] symbolIcons;
    private String[] sideBarImagePaths = { "imgs/payline1.png", "imgs/payline2.png", "imgs/payline3.png",
            "imgs/payline4.png", "imgs/payline5.png", "imgs/payline6.png", "imgs/payline7.png", "imgs/payline8.png",
            "imgs/payline9.png", "imgs/payline10.png" };
    private Map<String, ImageIcon> symbolImages;
    private JLabel balanceLabel;
    private JLabel totalLabel;
    private JLabel clockLabel;
    private int balance = 1000000;
    private JComboBox<Integer> paylineSelector;
    private Map<Integer, Double> paylineRTP; // 페이라인별 RTP 저장
    private final Map<Integer, int[][]> paylines = new HashMap<>();
    private int totalWinnings = 0;
    private JLabel animatedMessageLabel;
    private JLabel[] sideBarImageLabels; // 사이드바 이미지 라벨 배열

    private String currentUserId; // 로그인된 사용자 ID
    private ScheduledExecutorService scheduler; // 시계 업데이트 스케줄러

    // 생성자에서 로그인된 사용자 ID를 받아서 초기화

    public testpage(String loggedInUserId) {
        this.currentUserId = loggedInUserId;
        initializePaylines();
        initializeSymbols();
        setTitle("Slot Machine");
        setSize(1000, 920);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        random = new Random();

        // 배경색과 글자색 설정
        getContentPane().setBackground(new Color(18, 18, 18)); // 다크 배경

        // 애니메이션 메시지 라벨 초기화
        animatedMessageLabel = new JLabel("", SwingConstants.CENTER);
        animatedMessageLabel.setFont(new Font("Monospaced", Font.BOLD, 32)); // 한국어 지원 폰트로 설정
        animatedMessageLabel.setForeground(new Color(255, 87, 34)); // 주황색 글자색
        animatedMessageLabel.setBackground(new Color(255, 255, 255, 200)); // 약간 투명한 흰색 배경 설정
        animatedMessageLabel.setOpaque(true);
        animatedMessageLabel.setVisible(false);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(1000, 920));
        add(layeredPane, BorderLayout.CENTER);

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
        clockLabel.setFont(new Font("Monospaced", Font.PLAIN, 16)); // 한국어 지원 폰트로 설정
        clockLabel.setForeground(Color.WHITE); // 흰색 글자
        updateClock();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateClock, 0, 1, TimeUnit.SECONDS);

        // 시계 표시 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(18, 18, 18)); // 배경색 설정
        totalLabel = new JLabel("누적 당첨 점수 : " + 0);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Monospaced", Font.PLAIN, 25));
        totalLabel.setForeground(Color.WHITE); // 흰색 글자
        topPanel.add(clockLabel, BorderLayout.WEST);
        topPanel.add(totalLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // 슬롯 화면 패널
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(4, 5));
        slotPanel.setBounds(0, 0, 1300, 750); // 슬롯 패널의 크기 설정 (높이를 800으로 설정)
        slotPanel.setBackground(new Color(18, 18, 18)); // 슬롯 화면 배경색 설정
        layeredPane.add(slotPanel, Integer.valueOf(1));
        reels = new JLabel[4][5];

        for (int i = 0; i < 4; i++) {
            int randomIndexForRow = random.nextInt(symbolIcons.length); // 각 행에 대해 동일한 랜덤 인덱스 선택
            for (int j = 0; j < 5; j++) {
                reels[i][j] = new JLabel(symbolIcons[randomIndexForRow], SwingConstants.CENTER); // 실제 아이콘 설정
                slotPanel.add(reels[i][j]);
            }
        }

        animatedMessageLabel.setBounds(0, 300, 1300, 100); // 애니메이션 메시지 위치 설정
        layeredPane.add(animatedMessageLabel, Integer.valueOf(2));

        JPanel sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(200, 750)); // 슬롯 패널과 높이를 맞춤
        sideBar.setBackground(new Color(34, 34, 34)); // 다크 그레이 배경
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));

        sideBarImageLabels = new JLabel[sideBarImagePaths.length]; // 사이드바 이미지 라벨 배열 초기화

        // 사이드바에 이미지 추가
        for (int i = 0; i < sideBarImagePaths.length; i++) {
            sideBarImageLabels[i] = new JLabel();
            ImageIcon icon = new ImageIcon(sideBarImagePaths[i]);
            Image scaledImage = icon.getImage().getScaledInstance(150, 80, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
            sideBarImageLabels[i].setIcon(icon);
            sideBar.add(sideBarImageLabels[i]);
            sideBar.add(Box.createRigidArea(new Dimension(0, 5))); // 이미지 사이 간격
        }

        add(sideBar, BorderLayout.EAST);

        // 버튼과 정보 패널
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(18, 18, 18)); // 배경색 설정
        balanceLabel = new JLabel("Balance: " + balance + "점");
        balanceLabel.setFont(new Font("Monospaced", Font.BOLD, 32)); // 폰트 크기 확대
        balanceLabel.setForeground(Color.WHITE); // 흰색 글자

        Integer[] paylinesArray = { 1, 3, 5, 7, 9, 10 };
        paylineSelector = new JComboBox<>(paylinesArray);
        paylineSelector.setFont(new Font("Monospaced", Font.BOLD, 32)); // 폰트 크기 확대
        paylineSelector.setBackground(new Color(34, 34, 34));
        paylineSelector.setForeground(Color.WHITE);
        paylineSelector.addActionListener(e -> updateSidebarHighlights());

        JButton spinButton = new JButton("Spin");
        spinButton.setFont(new Font("Monospaced", Font.BOLD, 32)); // 폰트 크기 확대
        spinButton.setBackground(new Color(255, 87, 34)); // 버튼 배경색 설정
        spinButton.setForeground(Color.WHITE); // 버튼 글자색 설정
        JButton endGameButton = new JButton("End Game");
        endGameButton.setFont(new Font("Monospaced", Font.BOLD, 32)); // 폰트 크기 확대
        endGameButton.setBackground(new Color(255, 87, 34)); // 버튼 배경색 설정
        endGameButton.setForeground(Color.WHITE); // 버튼 글자색 설정

        controlPanel.add(balanceLabel);
        JLabel paylineLabel = new JLabel("Paylines: ");
        paylineLabel.setFont(new Font("Monospaced", Font.BOLD, 32)); // 폰트 크기 확대
        paylineLabel.setForeground(Color.WHITE); // 흰색 글자
        controlPanel.add(paylineLabel);
        controlPanel.add(paylineSelector);
        controlPanel.add(spinButton);
        controlPanel.add(endGameButton);

        spinButton.addActionListener(e -> startGame());
        endGameButton.addActionListener(e -> endGame());

        add(topPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void initializeSymbols() {
        symbolIcons = new ImageIcon[symbolPaths.length];
        for (int i = 0; i < symbolPaths.length; i++) {
            // 이미지 로드 및 크기 조정
            ImageIcon originalIcon = new ImageIcon(symbolPaths[i]);
            Image scaledImage = originalIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            symbolIcons[i] = new ImageIcon(scaledImage);
        }
    }

    private void updateSidebarHighlights() {
        int selectedPayline = (Integer) paylineSelector.getSelectedItem();
        for (int i = 0; i < sideBarImageLabels.length; i++) {
            if (i < selectedPayline) {
                sideBarImageLabels[i].setBorder(new LineBorder(new Color(255, 87, 34), 3)); // 선택된 페이라인 강조
            } else {
                sideBarImageLabels[i].setBorder(null); // 강조 제거
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
	
	private void showAnimatedMessage(String message) {
	    animatedMessageLabel.setText(message); // 메시지 설정
	    animatedMessageLabel.setVisible(true); // 라벨 보이기

	    // 타이머를 사용해 2초 후 메시지 숨김
	    Timer hideTimer = new Timer(2000, e -> {
	        animatedMessageLabel.setVisible(false); // 라벨 숨기기
	    });
	    hideTimer.setRepeats(false); // 반복하지 않음
	    hideTimer.start();
	}

	private void updateClock() {
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		clockLabel.setText("Current Time: " + formatter.format(new Date()));
	}

	private void startGame() {
		int selectedPayline = (Integer) paylineSelector.getSelectedItem();
		int deductionAmount = selectedPayline * 10;

		if (balance < deductionAmount) {
			showAnimatedMessage("잔액이 부족합니다!");
			return;
		}

		balance -= deductionAmount;
		balanceLabel.setText("Balance: " + balance + "점");
		showAnimatedMessage("스핀 중입니다...");
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
						reels[i][j].setIcon(symbolIcons[index]);
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
	    int selectedPaylines = (Integer) paylineSelector.getSelectedItem();
	    int winnings = 0;
	    int baseScore = 100;

	    for (int line = 1; line <= selectedPaylines; line++) {
	        int[][] paylinePattern = paylines.get(line);

	        if (paylinePattern != null && checkPaylineMatch(paylinePattern)) {
	            highlightWin(paylinePattern);
	            double multiplier = getMultiplier(selectedPaylines);
	            winnings += (int) (baseScore * multiplier);
	        }
	    }

	    if (winnings > 0) {
            balance += winnings; // 당첨 점수 반영
            totalWinnings += winnings; // 누적 당첨 점수 추가
            balanceLabel.setText("Balance: " + balance + "점");
            totalLabel.setText("누적 당첨 점수 : " + totalWinnings);
            showAnimatedMessage("축하합니다! " + winnings + "점 획득!"); // 당첨 메시지 표시
        } else {
            showAnimatedMessage("아쉽게도 당첨되지 않았습니다."); // 당첨되지 않은 경우 메시지 표시
        }
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
	    showAnimatedMessage("게임을 종료합니다."); // 애니메이션 메시지 표시

	    List<userread> userData = readUserFile("user.txt");
	    String currentUserId = this.currentUserId;

	    boolean userFound = false;
	    for (userread user : userData) {
	        if (user.getId().equals(currentUserId)) {
	            user.setScore(user.getScore() + totalWinnings);
	            userFound = true;
	            break;
	        }
	    }

	    if (!userFound) {
	        showAnimatedMessage("사용자 정보를 찾을 수 없습니다."); // 애니메이션 메시지 표시
	    } else {
	        writeUserFile(userData);
	        new Ranking();
	        setVisible(false);
	        dispose();
	        totalWinnings = 0;
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