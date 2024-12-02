package united;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class testpage extends JPanel {
    private JLabel[][] reels;
    private Timer timer;
    private Random random;
    private String[] symbolPaths = { "imgs/배.png", "imgs/복숭아.png", "imgs/키위.png",};
    private ImageIcon[] symbolIcons;
    private String[] sideBarImagePaths = { "imgs/payline1-2.png", "imgs/payline2-2.png", "imgs/payline3-2.png" };
    private JComboBox<Integer> paylineSelector;
    private final Map<Integer, int[][]> paylines = new HashMap<>();
    private int totalWinnings = 0;
    private JLabel animatedMessageLabel;
    private JLabel[] sideBarImageLabels;
    private JLabel scoreLabel; // 클래스 멤버로 선언

    private String currentUserId;
    private ScheduledExecutorService scheduler;
    private static CardLayout cardLayout;
    private static JPanel mainContainer;

    private JLabel chipLabel; // 칩 개수를 나타낼 라벨
    private User currentUser; // User 객체
    private JButton winningsButton;
    private Clip spinClip;

    public testpage(String loggedInUserId, User user, CardLayout cardLayout, JPanel mainContainer) {
        this.currentUserId = loggedInUserId;
        this.currentUser = user; // User 객체 초기화
        this.cardLayout = cardLayout;
        this.mainContainer = mainContainer;
        mainContainer.add(new Ranking(cardLayout, mainContainer), "RankingPage");
        initializePaylines();
        initializeSymbols();
        initializeSlotGame();
        updateChipLabel(); // 칩 개수 초기화 시점에 업데이트
        
        
    }

    public void initializeSlotGame() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(850, 800)); // StartScreen과 동일한 크기 설정
        setSize(850, 800);
        setBackground(new Color(18, 18, 18));
        random = new Random();
        
        chipLabel = new JLabel("Chips: " + currentUser.getChipNum() + "개");
        chipLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        chipLabel.setForeground(Color.WHITE);

        // 애니메이션 메시지 라벨 설정
        animatedMessageLabel = new JLabel("", SwingConstants.CENTER);
        animatedMessageLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        animatedMessageLabel.setForeground(Color.YELLOW);
        animatedMessageLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        animatedMessageLabel.setBackground(new Color(0, 0, 0, 150));
        animatedMessageLabel.setOpaque(true);
        animatedMessageLabel.setBounds(0, 300, 600, 60);
        animatedMessageLabel.setVisible(false);

        Timer blinkTimer = new Timer(500, e -> animatedMessageLabel.setVisible(!animatedMessageLabel.isVisible()));
        blinkTimer.setRepeats(true);

        // 상단바 타이틀 라벨 설정
        JLabel titleLabel = new JLabel("Slot Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 36)); // 큰 글씨 크기
        titleLabel.setForeground(Color.YELLOW); // 노란색 글씨
        titleLabel.setBackground(new Color(18, 18, 18));
        titleLabel.setOpaque(true);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(18, 18, 18));
        topPanel.add(titleLabel, BorderLayout.CENTER); // 상단바에 "Slot Game" 추가
        add(topPanel, BorderLayout.NORTH);

        // 레이어드 페인 설정
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(850, 800));
        layeredPane.setBounds(0, 0, 850, 600); // JLayeredPane의 크기를 조정하여 슬롯 패널이 더 잘 맞도록 설정
        add(layeredPane, BorderLayout.CENTER);

        // 슬롯 패널 설정
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(1, 5, 0, 0)); // 열 단위로 그룹화하기 위해 1행 5열로 설정
        slotPanel.setBounds(0, 0, 650, 600); // X와 Y 좌표를 재조정하여 공간 없애기
        slotPanel.setBackground(new Color(18, 18, 18));
        layeredPane.add(slotPanel, Integer.valueOf(1));
        reels = new JLabel[4][5];


        for (int j = 0; j < 5; j++) { // 열 단위 반복
            // 열을 감싸는 패널 생성
            JPanel columnPanel = new JPanel();
            columnPanel.setLayout(new GridLayout(4, 1, 0, 0)); // 각 열은 4행 1열
            columnPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // 노란색 테두리 추가
            columnPanel.setBackground(new Color(18, 18, 18)); // 배경색 설정

            for (int i = 0; i < 4; i++) { // 각 열의 슬롯 생성
                int randomIndexForRow = random.nextInt(symbolIcons.length);
                reels[i][j] = new JLabel(symbolIcons[randomIndexForRow], SwingConstants.CENTER);
                reels[i][j].setPreferredSize(new Dimension(150, 130)); // 슬롯 크기 조정
                columnPanel.add(reels[i][j]); // 열 패널에 슬롯 추가
            }

            slotPanel.add(columnPanel); // 메인 패널에 열 패널 추가
        }

        layeredPane.add(animatedMessageLabel, Integer.valueOf(2));

     // 사이드바 설정 (FlowLayout으로 변경)
        JPanel sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(160, 800)); // 사이드바 높이를 화면에 맞게 설정
        sideBar.setBackground(Color.BLACK); // 배경색을 검정색으로 설정
        sideBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5)); // FlowLayout으로 변경하여 왼쪽 정렬

        // 누적 당첨 점수 라벨을 사이드바의 맨 위에 추가하고 테두리 설정
        JPanel totalPanel = new JPanel();
        totalPanel.setBackground(new Color(34, 34, 34));
        totalPanel.setPreferredSize(new Dimension(160, 60)); // 사이드바와 동일한 너비로 설정
        totalPanel.setLayout(new BorderLayout());

     // 기존 코드에서 지역 변수였던 scoreLabel 제거하고, 멤버로 선언된 scoreLabel 사용
        scoreLabel = new JLabel("Score : " + currentUser.getScore() + "점", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 22));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBackground(Color.black);
        scoreLabel.setOpaque(true);

        scoreLabel.setPreferredSize(new Dimension(160, 60));
        totalPanel.add(scoreLabel, BorderLayout.CENTER);


        sideBar.add(totalPanel);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));        

        // 사이드바 이미지 라벨 설정
     // 사이드바 이미지 라벨 설정
        if (sideBarImagePaths != null) {
            sideBarImageLabels = new JLabel[sideBarImagePaths.length];
            for (int i = 0; i < sideBarImagePaths.length; i++) {
                sideBarImageLabels[i] = new JLabel();
                ImageIcon icon = new ImageIcon(sideBarImagePaths[i]);
                Image scaledImage = icon.getImage().getScaledInstance(150, 110, Image.SCALE_SMOOTH); // 사이드바 이미지 크기 조정
                icon = new ImageIcon(scaledImage);
                sideBarImageLabels[i].setIcon(icon);
                sideBarImageLabels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                sideBarImageLabels[i].setBackground(Color.BLACK); // 배경색을 검정색으로 설정
                sideBarImageLabels[i].setOpaque(true);

                int index = i;

                sideBar.add(sideBarImageLabels[i]);

                // 이미지 라벨 사이에 간격 추가
                if (i < sideBarImagePaths.length) {
                    sideBar.add(Box.createRigidArea(new Dimension(20, 20))); // 수직 간격 10px
                }
            }
        }

        JButton rankingButton = createRetroButton("Ranking");
        rankingButton.addActionListener(e -> {
            // 랭킹 페이지로 전환
            cardLayout.show(mainContainer, "RankingPage");
        });
        sideBar.add(Box.createRigidArea(new Dimension(0, 30))); // 랭킹 버튼 위에 간격 추가
        sideBar.add(rankingButton); // 사이드바의 맨 아래에 랭킹 버튼 추가
        
        JButton chipExchangeButton = createRetroButton("Exchange");
        rankingButton.addActionListener(e -> {
            // 랭킹 페이지로 전환
            cardLayout.show(mainContainer, "chipExchangePage");
        });
        sideBar.add(Box.createRigidArea(new Dimension(90, 0))); // 랭킹 버튼 위에 간격 추가
        sideBar.add(chipExchangeButton); // 사이드바의 맨 아래에 랭킹 버튼 추가

        add(sideBar, BorderLayout.EAST); // 사이드바 추가


        // 하단 컨트롤 패널 설정
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(18, 18, 18));

        chipLabel = new JLabel("Chip: " + currentUser.getChipNum());
        chipLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        chipLabel.setForeground(Color.WHITE);

        Integer[] paylinesArray = { 1, 3, 5, 7, 9, 10 };
        paylineSelector = new JComboBox<>(paylinesArray);
        paylineSelector.setFont(new Font("Monospaced", Font.BOLD, 24));
        paylineSelector.setBackground(new Color(34, 34, 34));
        paylineSelector.setForeground(Color.WHITE);
        paylineSelector.addActionListener(e -> updateSidebarHighlights());

        JButton spinButton = createRetroButton("Spin");
        JButton endGameButton = createRetroButton("End Game");

        controlPanel.add(chipLabel);
        JLabel paylineLabel = new JLabel("Paylines: ");
        paylineLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        paylineLabel.setForeground(Color.WHITE);
        controlPanel.add(paylineLabel);
        controlPanel.add(paylineSelector);
        controlPanel.add(spinButton);
        controlPanel.add(endGameButton);

        add(controlPanel, BorderLayout.SOUTH);

        // Spin 버튼 클릭 이벤트
        spinButton.addActionListener(e -> startGame());
        endGameButton.addActionListener(e -> endGame());
    }

    // 칩 개수 업데이트
    public void updateChipLabel() {
        if (currentUser != null) {
            chipLabel.setText("Chips: " + currentUser.getChipNum() + "개");
        } else {
            chipLabel.setText("Chips: 0개");
        }
    }
    private void initializeSymbols() {
        symbolIcons = new ImageIcon[symbolPaths.length];
        for (int i = 0; i < symbolPaths.length; i++) {
            ImageIcon originalIcon = new ImageIcon(symbolPaths[i]);
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            symbolIcons[i] = new ImageIcon(scaledImage);
        }
    }
    
    private void updateSidebarHighlights() {
        int selectedPayline = (Integer) paylineSelector.getSelectedItem();
        
        // sideBarImageLabels의 모든 라벨을 초기화 (테두리 없애기)
        for (int i = 0; i < sideBarImageLabels.length; i++) {
            sideBarImageLabels[i].setBorder(null);
        }

        // 선택된 payline에 맞게 이미지 라벨에 테두리 추가
        switch (selectedPayline) {
            case 1:
            case 3:
                // 첫 번째 이미지에만 빨간 테두리 추가
                sideBarImageLabels[0].setBorder(new LineBorder(new Color(255, 87, 34), 4));
                break;
            case 5:
            case 7:
                // 첫 번째와 두 번째 이미지에 빨간 테두리 추가
                sideBarImageLabels[0].setBorder(new LineBorder(new Color(255, 87, 34), 4));
                sideBarImageLabels[1].setBorder(new LineBorder(new Color(255, 87, 34), 4));
                break;
            case 9:
            case 10:
                // 모든 이미지에 빨간 테두리 추가
                for (int i = 0; i < sideBarImageLabels.length; i++) {
                    sideBarImageLabels[i].setBorder(new LineBorder(new Color(255, 87, 34), 4));
                }
                break;
            default:
                // 선택되지 않은 값에 대해서는 테두리 설정을 하지 않음
                break;
        }
    }



    private void initializePaylines() {
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

    private void startGame() {
        int selectedPayline = (Integer) paylineSelector.getSelectedItem();
        int deductionAmount = selectedPayline * 20; // 페이라인이 클수록 차감 금액이 증가
        int cost = selectedPayline * 10; // 한 Payline 당 10칩 소모

        if (currentUser.getChipNum() < deductionAmount) {
            showAnimatedMessage("칩이 부족합니다!");
            return;
        }

        currentUser.setChipNum(currentUser.getChipNum() - cost); // 칩 소모
        updateChipLabel(); // 칩 라벨 업데이트
        showAnimatedMessage("스핀 중입니다...");
        startSpin();
    }

    private void showAnimatedMessage(String message) {
        animatedMessageLabel.setText(message);
        animatedMessageLabel.setForeground(Color.YELLOW); // 글자 색을 노란색으로 설정
        animatedMessageLabel.setVisible(true); // 메시지를 화면에 표시

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow(); // 기존 실행 중인 스케줄러가 있으면 종료
        }

        scheduler = Executors.newScheduledThreadPool(1);

        // 깜빡임을 제어하는 작업
        Runnable blinkTask = new Runnable() {
            int blinkCount = 0;

            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    animatedMessageLabel.setVisible(!animatedMessageLabel.isVisible()); // 깜빡이게 설정
                });
                blinkCount++;
                if (blinkCount >= 6) { // 3초 동안 (500ms x 6번) 깜빡이고 종료
                    scheduler.shutdown(); // 스케줄러 종료
                    SwingUtilities.invokeLater(() -> animatedMessageLabel.setVisible(false));
                }
            }
        };

        // 500ms 주기로 깜빡임 작업 스케줄링
        scheduler.scheduleAtFixedRate(blinkTask, 0, 500, TimeUnit.MILLISECONDS);
    }

    private void startSpin() {
        resetBorders();
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        // 이전에 재생 중인 사운드가 있으면 멈춤
        if (spinClip != null && spinClip.isRunning()) {
            spinClip.stop();
            spinClip.close();
        }

        // 새로운 사운드 재생
        spinClip = playSound("spinSound.wav"); // 사운드 파일 경로

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

                    // 타이머가 멈추면 사운드도 멈춤
                    if (spinClip != null && spinClip.isRunning()) {
                        spinClip.stop();
                        spinClip.close();
                    }

                    checkWin();
                }
            }
        });

        timer.start();
    }

    private Clip playSound(String soundFile) {
        try {
            File audioFile = new File(soundFile);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            return clip; // Clip 객체 반환
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 예외 발생 시 null 반환
        }
    }

	private JButton createRetroButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Monospaced", Font.BOLD, 20));
        button.setBackground(Color.BLACK);
        button.setForeground(Color.yellow);
        button.setPreferredSize(new Dimension(150, 50));
        button.setFocusPainted(false);
        return button;
    }

    private void checkWin() {
        int selectedPaylines = (Integer) paylineSelector.getSelectedItem();
        int winnings = 0;

        for (int line = 1; line <= selectedPaylines; line++) {
            int[][] paylinePattern = paylines.get(line);

            if (paylinePattern != null && checkPaylineMatch(paylinePattern)) {
                highlightWin(paylinePattern);
                double multiplier = getMultiplier(selectedPaylines);
                winnings += (int) (getBaseScoreForIcon(reels[paylinePattern[0][0]][paylinePattern[0][1]].getIcon())
                        * multiplier);
            }
        }
        if (winnings > 0) {
            totalWinnings += winnings;
            currentUser.setScore(totalWinnings); // 현재 유저의 점수 업데이트
            scoreLabel.setText("Score : " + totalWinnings + "점"); // 점수 라벨 업데이트
            showAnimatedMessage("축하합니다! " + winnings + "점 획득!"); // 메시지 깜빡이게 표시
        } else {
            showAnimatedMessage("아쉽게도 당첨되지 않았습니다."); // 메시지 깜빡이게 표시
        }

    }

	private int getBaseScoreForIcon(Icon icon) {
		for (int i = 0; i < symbolIcons.length; i++) {
			if (symbolIcons[i].equals(icon)) {
				return switch (i) {
				case 0 -> random.nextInt(5) + 1; // 체리: 1배에서 5배 사이의 점수
				case 1 -> random.nextInt(11) + 5; // 레몬: 5배에서 15배 사이의 점수
				case 2 -> random.nextInt(11) + 10; // 바나나: 10배에서 20배 사이의 점수
				case 3 -> random.nextInt(31) + 20; // 수박: 20배에서 50배 사이의 점수
				case 4 -> random.nextInt(71) + 30; // 황금 벨: 30배에서 100배 사이의 점수
				case 5 -> random.nextInt(451) + 50; // 행운의 숫자 7: 50배에서 500배 사이의 점수
				default -> 0;
				};
			}
		}
		return 0;
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
			return 1.2;
		default:
			return 1.0;
		}
	}

	private boolean checkPaylineMatch(int[][] paylinePattern) {
		ImageIcon firstIcon = (ImageIcon) reels[paylinePattern[0][0]][paylinePattern[0][1]].getIcon();
		if (firstIcon == null) {
			return false;
		}

		for (int[] pos : paylinePattern) {
			ImageIcon currentIcon = (ImageIcon) reels[pos[0]][pos[1]].getIcon();
			if (!firstIcon.equals(currentIcon)) {
				return false;
			}
		}
		return true;
	}

	private void highlightWin(int[][] positions) {
		for (int[] pos : positions) {
			reels[pos[0]][pos[1]].setBorder(new LineBorder(Color.RED, 2));
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
		showAnimatedMessage("게임을 종료합니다.");

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
			showAnimatedMessage("사용자 정보를 찾을 수 없습니다.");
		} else {
			writeUserFile(userData);
			cardLayout.show(mainContainer, "MainPage");
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Slot Machine Test Page");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1020, 960);

			CardLayout cardLayout = new CardLayout();
			JPanel mainContainer = new JPanel(cardLayout);
			// User 객체 생성 (아이디, 비밀번호, 이름, 생일 등 필요한 정보 입력)
			User loggedInUser = new User("exampleUser", "password123", "홍길동", "1990-01-01");

			// testpage 객체 생성
			testpage slotPanel = new testpage(loggedInUser.getId(), loggedInUser, cardLayout, mainContainer);
			mainContainer.add(slotPanel, "testpage");

			frame.add(mainContainer);
			frame.setVisible(true);

			cardLayout.show(mainContainer, "testpage");
		});
	}

}