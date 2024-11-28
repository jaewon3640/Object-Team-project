package united;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
public class HowToPlay extends JPanel {
    public HowToPlay(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        // 상단 제목
        JLabel titleLabel = new JLabel("How to Play", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.YELLOW);
        add(titleLabel, BorderLayout.NORTH);
        // 중앙 패널 (스크롤 가능한 페이라인 이미지 + 설명 포함)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.BLACK);
        // 게임 설명 추가
        JTextArea instructions = new JTextArea();
        instructions.setText("슬롯 머신 게임에 오신 것을 환영합니다!\n\n"
                + "1. 게임을 시작하기 위해 'Slot Game' 버튼을 누르세요.\n"
                + "2. 칩을 교환하기 위해 'Chip Exchange' 버튼을 누르세요.\n"
                + "3. 'Ranking' 버튼을 눌러 사용자들의 순위를 확인할 수 있습니다.\n"
                + "4. 'User Info' 버튼을 눌러 당신의 정보를 확인할 수 있습니다.\n\n"
                + "행운을 빕니다!");
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 16));
        instructions.setForeground(Color.WHITE);
        instructions.setBackground(Color.BLACK);
        instructions.setEditable(false);
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(instructions);
        // 구분선 추가
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.YELLOW);
        contentPanel.add(separator);
        // 페이라인 설명 텍스트 (왼쪽 완전히 붙임)
        JPanel explanationPanel = new JPanel(new BorderLayout());
        explanationPanel.setBackground(Color.BLACK);
        JLabel paylineExplanation = new JLabel("<html><b>페이라인이란?</b><br>슬롯의 정해진 시스템에 따라 당첨금을 지급하는 방식."
                + "<br><br>이 슬롯머신에는 아래 그림과 같은 10개의 페이라인이 존재합니다.</html>");
        paylineExplanation.setFont(new Font("Serif", Font.PLAIN, 16));
        paylineExplanation.setForeground(Color.WHITE);
        paylineExplanation.setHorizontalAlignment(SwingConstants.LEFT); // 왼쪽 정렬
        paylineExplanation.setVerticalAlignment(SwingConstants.TOP);
        explanationPanel.add(paylineExplanation, BorderLayout.WEST); // 왼쪽에 완전히 붙이기
        contentPanel.add(explanationPanel);
        // 페이라인 이미지 추가
        ArrayList<String> imagePaths = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            imagePaths.add("imgs/payline" + i + ".png");
        }
        for (int i = 0; i < imagePaths.size(); i++) {
            JPanel paylinePanel = new JPanel();
            paylinePanel.setLayout(new BorderLayout());
            paylinePanel.setBackground(Color.BLACK);
            // 페이라인 이미지
            JLabel imageLabel = new JLabel(new ImageIcon(imagePaths.get(i)));
            paylinePanel.add(imageLabel, BorderLayout.WEST);
            paylinePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 간격 조정
            contentPanel.add(paylinePanel);
        }
        // 페이라인 설명 텍스트 추가
        JTextArea paylineDetails = new JTextArea();
        paylineDetails.setText("- 게임 화면에서 1, 3, 5, 7, 9, 10개의 페이라인을 선택할 수 있습니다.\n"
                + "- 모든 페이라인은 기본 점수 100점을 가집니다. 페이라인 개수 선택 별로 아래와 같은 배수로 점수를 획득합니다.\n"
                + "- 페이라인 1개 선택 시 : 10배\n"
                + "- 페이라인 3개 선택 시 : 5배\n"
                + "- 페이라인 5개 선택 시 : 3배\n"
                + "- 페이라인 7개 선택 시 : 2배\n"
                + "- 페이라인 9개 선택 시 : 1.5배\n"
                + "- 페이라인 10개 선택 시 : 1배\n\n"
                + "- 페이라인을 많이 선택할 수록 당첨될 확률이 올라가지만, 게임 당 소비되는 칩의 개수가 늘어나고, 당첨 시 획득 점수도 줄어듭니다.");
        paylineDetails.setFont(new Font("Monospaced", Font.PLAIN, 16));
        paylineDetails.setForeground(Color.WHITE);
        paylineDetails.setBackground(Color.BLACK);
        paylineDetails.setEditable(false);
        paylineDetails.setLineWrap(true);
        paylineDetails.setWrapStyleWord(true);
        paylineDetails.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.add(paylineDetails);
        // 스크롤 패널에 내용 추가
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
        // 뒤로가기 버튼
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        backButton.setBackground(Color.YELLOW);
        backButton.setForeground(Color.BLACK);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainPage"));
        add(backButton, BorderLayout.SOUTH);
    }
}