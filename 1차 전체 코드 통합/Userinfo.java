package united;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;

public class Userinfo implements ActionListener {
    private User user;  // User 객체
    private JFrame mainFrame;
    private JLabel imageLabel;
    private int currentImageIndex = 0;
    private String[] imagePaths = {"imgs/example.png", "imgs/example2.png", "imgs/example3.png"};
         
    public Userinfo(User user) {
        this.user = user;  // 생성자에서 User 객체를 전달받음
    }

    public void startGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
        mainFrame = new JFrame("User Info");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = setupMainPanel();
        mainFrame.setPreferredSize(new Dimension(840, 540));
        mainFrame.getContentPane().add(pane);
        mainFrame.setBackground(Color.RED);

        // Display the window.
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    private JPanel setupMainPanel() {
        JPanel mainPane = new JPanel(new CardLayout());

        // 카드 레이아웃을 위한 패널 생성
        JPanel card1 = new JPanel(new BorderLayout()); // 기존 화면
        JPanel card2 = new JPanel(new BorderLayout()); // 수정 화면

        // 기본 정보 테이블
        String[] personalInfoColumns = {"항목", "정보"};
        Object[][] personalInfoData = {
            {"아이디", user.getId()},
            {"비밀번호", user.getPassword()},
            {"이름", user.getName()},
            {"생일", user.getBirthdate()}
        };

        DefaultTableModel personalInfoModel = new DefaultTableModel(personalInfoData, personalInfoColumns);
        JTable personalInfoTable = new JTable(personalInfoModel);
        
        // 테이블 폰트와 글자 크기 설정
        Font tableFont = new Font("맑은 고딕", Font.PLAIN, 18);  // 폰트와 크기 설정
        personalInfoTable.setFont(tableFont);
        personalInfoTable.setRowHeight(40);  // 각 행의 높이 설정
        personalInfoTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 20));  // 헤더 폰트 설정

        JScrollPane personalInfoScrollPane = new JScrollPane(personalInfoTable);

        // 승률 및 통계 테이블
        String[] statsColumns = {"항목", "정보"};
        Object[][] statsData = {
            {"승률", "100%"},
            {"금액", "1000"},
            {"환전한 칩 개수",user.getChipNum()}
        };

        DefaultTableModel statsModel = new DefaultTableModel(statsData, statsColumns);
        JTable statsTable = new JTable(statsModel);
        
        // 승률 및 통계 테이블 폰트와 글자 크기 설정
        statsTable.setFont(tableFont);
        statsTable.setRowHeight(40);
        statsTable.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 20));

        JScrollPane statsScrollPane = new JScrollPane(statsTable);

        // 수정 버튼 및 나가기 버튼
        JButton editButton = new JButton("수정");
        JButton exitButton = new JButton("나가기");

        editButton.addActionListener(e -> {
            // 수정 화면으로 전환
            CardLayout cardLayout = (CardLayout) mainPane.getLayout();
            cardLayout.show(mainPane, "수정 화면");
        });

        exitButton.addActionListener(e -> {
            // 나가기 버튼 처리
            mainFrame.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(exitButton);

        // 기존 화면에 테이블과 버튼 추가
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(personalInfoScrollPane);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(statsScrollPane);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(buttonPanel);

        // 이미지 설정
        imageLabel = new JLabel(new ImageIcon(imagePaths[currentImageIndex]));
        JPanel imagePanel1 = new JPanel(new BorderLayout());
        imagePanel1.add(imageLabel, BorderLayout.CENTER);

        // 이미지 변경 버튼
        JButton prevButton = new JButton("이전 이미지");
        JButton nextButton = new JButton("다음 이미지");
        prevButton.addActionListener(e -> changeImage(-1));
        nextButton.addActionListener(e -> changeImage(1));

        JPanel imageButtonPanel = new JPanel();
        imageButtonPanel.add(prevButton);
        imageButtonPanel.add(nextButton);

        imagePanel1.add(imageButtonPanel, BorderLayout.SOUTH);

        card1.add(labelPanel, BorderLayout.CENTER);
        card1.add(imagePanel1, BorderLayout.WEST);

        // 수정 화면 설정
        JPanel labelPanelEdit = new JPanel();
        labelPanelEdit.setLayout(new BoxLayout(labelPanelEdit, BoxLayout.Y_AXIS));

        JTextField idField = new JTextField(user.getId(), 10);
        JTextField passwordField = new JTextField(user.getPassword(), 10);
        JTextField nameField = new JTextField(user.getName(), 10);
        JTextField birthField = new JTextField(user.getBirthdate(), 10);

        JButton updateButton = new JButton("수정 완료");
        updateButton.addActionListener(e -> {
            // 수정된 값 반영
            user.setId(idField.getText());
            user.setPassword(passwordField.getText());
            user.setName(nameField.getText());
            user.setBirthdate(birthField.getText());

            personalInfoModel.setValueAt(user.getId(), 0, 1);
            personalInfoModel.setValueAt(user.getPassword(), 1, 1);
            personalInfoModel.setValueAt(user.getName(), 2, 1);
            personalInfoModel.setValueAt(user.getBirthdate(), 3, 1);

            // 기존 화면으로 돌아가기
            CardLayout cardLayout = (CardLayout) mainPane.getLayout();
            cardLayout.show(mainPane, "기존 화면");
        });

        labelPanelEdit.add(new JLabel("아이디:"));
        labelPanelEdit.add(idField);
        labelPanelEdit.add(new JLabel("비밀번호:"));
        labelPanelEdit.add(passwordField);
        labelPanelEdit.add(new JLabel("이름:"));
        labelPanelEdit.add(nameField);
        labelPanelEdit.add(new JLabel("생일:"));
        labelPanelEdit.add(birthField);
        labelPanelEdit.add(updateButton);

        card2.add(labelPanelEdit, BorderLayout.CENTER);

        // 카드 레이아웃에 두 카드 추가
        mainPane.add(card1, "기존 화면");
        mainPane.add(card2, "수정 화면");

        return mainPane;
    }
    private void changeImage(int direction) {
        currentImageIndex = (currentImageIndex + direction + imagePaths.length) % imagePaths.length;
        ImageIcon originalIcon = new ImageIcon(imagePaths[currentImageIndex]);
        Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
    }

    public static void main(String args[]) {
        User user = new User("user1", "password123", "홍길동", "1990-01-01");
        Userinfo userInfoPage = new Userinfo(user);
        userInfoPage.startGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}