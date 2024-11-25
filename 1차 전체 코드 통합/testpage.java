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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class testpage extends JPanel {
    private JLabel[][] reels;
    private Timer timer;
    private Random random;
    private String[] symbolPaths = { "imgs/cherry.png", "imgs/lemon.png", "imgs/banana.png", "imgs/watermelon.png",
            "imgs/golden-bell.png", "imgs/lucky 7.png"};
    private ImageIcon[] symbolIcons;
    private String[] sideBarImagePaths = { "imgs/payline1.png", "imgs/payline2.png", "imgs/payline3.png",
            "imgs/payline4.png", "imgs/payline5.png", "imgs/payline6.png", "imgs/payline7.png", "imgs/payline8.png",
            "imgs/payline9.png", "imgs/payline10.png", "imgs/coin.png" };
    private JLabel balanceLabel;
    private JLabel totalLabel;
    private JLabel clockLabel;
    private int balance = 1000000;
    private JComboBox<Integer> paylineSelector;
    private final Map<Integer, int[][]> paylines = new HashMap<>();
    private int totalWinnings = 0;
    private JLabel animatedMessageLabel;
    private JLabel[] sideBarImageLabels;

    private String currentUserId;
    private ScheduledExecutorService scheduler;
    private static CardLayout cardLayout;
    private static JPanel mainContainer;

    public testpage(String loggedInUserId, CardLayout cardLayout, JPanel mainContainer) {
        this.currentUserId = loggedInUserId;
        this.cardLayout = cardLayout;
        this.mainContainer = mainContainer;
        initializePaylines();
        initializeSymbols();
        initializeSlotGame();
    }

    public void initializeSlotGame() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(850, 800));
        setBackground(new Color(18, 18, 18));
        random = new Random();

        animatedMessageLabel = new JLabel("", SwingConstants.CENTER);
        animatedMessageLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        animatedMessageLabel.setForeground(new Color(255, 87, 34));
        animatedMessageLabel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
        animatedMessageLabel.setBackground(new Color(0, 0, 0, 150));
        animatedMessageLabel.setOpaque(true);
        animatedMessageLabel.setBounds(0, 300, 600, 60);
        animatedMessageLabel.setVisible(false);

        Timer blinkTimer = new Timer(500, e -> animatedMessageLabel.setVisible(!animatedMessageLabel.isVisible()));
        blinkTimer.setRepeats(true);

        clockLabel = new JLabel();
        clockLabel.setFont(new Font("Monospaced", Font.PLAIN, 12));
        clockLabel.setForeground(Color.WHITE);
        updateClock();
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::updateClock, 0, 1, TimeUnit.SECONDS);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(18, 18, 18));
        totalLabel = new JLabel("누적 당첨 점수 : " + 0);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setFont(new Font("Monospaced", Font.PLAIN, 20));
        totalLabel.setForeground(Color.WHITE);
        topPanel.add(clockLabel, BorderLayout.WEST);
        topPanel.add(totalLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
        layeredPane.setPreferredSize(new Dimension(850, 600));
        add(layeredPane, BorderLayout.CENTER);

        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(new GridLayout(4, 5, 2, 2));
        slotPanel.setBounds(0, 0, 600, 500);
        slotPanel.setBackground(new Color(18, 18, 18));
        layeredPane.add(slotPanel, Integer.valueOf(1));
        reels = new JLabel[4][5];

        for (int i = 0; i < 4; i++) {
            int randomIndexForRow = random.nextInt(symbolIcons.length);
            for (int j = 0; j < 5; j++) {
                reels[i][j] = new JLabel(symbolIcons[randomIndexForRow], SwingConstants.CENTER);
                reels[i][j].setPreferredSize(new Dimension(100, 100));
                slotPanel.add(reels[i][j]);
            }
        }

        layeredPane.add(animatedMessageLabel, Integer.valueOf(2));

        JPanel sideBar = new JPanel();
        sideBar.setPreferredSize(new Dimension(150, 500));
        sideBar.setBackground(new Color(34, 34, 34));
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));

        sideBarImageLabels = new JLabel[sideBarImagePaths.length];

        for (int i = 0; i < sideBarImagePaths.length; i++) {
            sideBarImageLabels[i] = new JLabel();
            ImageIcon icon = new ImageIcon(sideBarImagePaths[i]);
            Image scaledImage = icon.getImage().getScaledInstance(100, 50, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImage);
            sideBarImageLabels[i].setIcon(icon);
            sideBarImageLabels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            int index = i;
            sideBarImageLabels[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (index == sideBarImagePaths.length - 1) { // 마지막 이미지는 칩 교환 버튼
                        cardLayout.show(mainContainer, "ChipExchangePage");
                    }
                }
            });
            sideBar.add(sideBarImageLabels[i]);
            sideBar.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        add(sideBar, BorderLayout.EAST);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(18, 18, 18));
        balanceLabel = new JLabel("Balance: " + balance + "점");
        balanceLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        balanceLabel.setForeground(Color.WHITE);

        Integer[] paylinesArray = { 1, 3, 5, 7, 9, 10 };
        paylineSelector = new JComboBox<>(paylinesArray);
        paylineSelector.setFont(new Font("Monospaced", Font.BOLD, 24));
        paylineSelector.setBackground(new Color(34, 34, 34));
        paylineSelector.setForeground(Color.WHITE);
        paylineSelector.addActionListener(e -> updateSidebarHighlights());

        JButton spinButton = new JButton("Spin");
        spinButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        spinButton.setBackground(new Color(255, 87, 34));
        spinButton.setForeground(Color.WHITE);
        JButton endGameButton = new JButton("End Game");
        endGameButton.setFont(new Font("Monospaced", Font.BOLD, 24));
        endGameButton.setBackground(new Color(255, 87, 34));
        endGameButton.setForeground(Color.WHITE);

        controlPanel.add(balanceLabel);
        JLabel paylineLabel = new JLabel("Paylines: ");
        paylineLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
        paylineLabel.setForeground(Color.WHITE);
        controlPanel.add(paylineLabel);
        controlPanel.add(paylineSelector);
        controlPanel.add(spinButton);
        controlPanel.add(endGameButton);

        spinButton.addActionListener(e -> {
            startGame();
            blinkTimer.start();
        });
        endGameButton.addActionListener(e -> endGame());

        add(controlPanel, BorderLayout.SOUTH);
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
        for (int i = 0; i < sideBarImageLabels.length; i++) {
            if (i < selectedPayline) {
                sideBarImageLabels[i].setBorder(new LineBorder(new Color(255, 87, 34), 2));
            } else {
                sideBarImageLabels[i].setBorder(null);
            }
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

    private void updateClock() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        clockLabel.setText("Current Time: " + formatter.format(new Date()));
    }

    private void startGame() {
        int selectedPayline = (Integer) paylineSelector.getSelectedItem();
        int deductionAmount = selectedPayline * 20; // 페이라인이 클수록 차감 금액이 증가

        if (balance < deductionAmount) {
            showAnimatedMessage("잔액이 부족합니다!");
            return;
        }

        balance -= deductionAmount;
        balanceLabel.setText("Balance: " + balance + "점");
        showAnimatedMessage("스핀 중입니다...");
        startSpin();
    }

    private void showAnimatedMessage(String message) {
        animatedMessageLabel.setText(message);
        animatedMessageLabel.setVisible(true);

        Timer hideTimer = new Timer(2000, e -> animatedMessageLabel.setVisible(false));
        hideTimer.setRepeats(false);
        hideTimer.start();
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
            balance += winnings;
            totalWinnings += winnings;
            balanceLabel.setText("Balance: " + balance + "점");
            totalLabel.setText("누적 당첨 점수 : " + totalWinnings);
            showAnimatedMessage("축하합니다! " + winnings + "점 획득!");
        } else {
            showAnimatedMessage("아쉽게도 당첨되지 않았습니다.");
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

            String loggedInUserId = "exampleUser";
            testpage slotPanel = new testpage(loggedInUserId, cardLayout, mainContainer);
            mainContainer.add(slotPanel, "testpage");

            frame.add(mainContainer);
            frame.setVisible(true);

            cardLayout.show(mainContainer, "testpage");
        });
    }
}