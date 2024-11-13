package Userinfo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.CardLayout;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Image;

public class Userinfo implements ActionListener {
    public void startGUI() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    static JFrame mainFrame = new JFrame("User Info");
    static Userinfo main = null;

    private JLabel imageLabel;
    private int currentImageIndex = 0;
    private String[] imagePaths = {"imgs/example.png", "imgs/example2.png", "imgs/example3.png"};

    private void createAndShowGUI() {
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel pane = setupMainPanel();
        mainFrame.setPreferredSize(new Dimension(840, 540));
        mainFrame.getContentPane().add(pane);
        mainFrame.setBackground(Color.RED);

        // Display the window.
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    JPanel setupMainPanel() {
        int winrate = 100;
        String id = "최재원";
        JPanel mainPane = new JPanel(new CardLayout());

        // 카드 레이아웃을 위한 패널 생성
        JPanel card1 = new JPanel(new BorderLayout()); // 기존 화면
        JPanel card2 = new JPanel(new BorderLayout()); // 수정 화면

        // 기존 화면 설정 (기본 정보 표시)
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));

        Font font = new Font("Dialog", Font.BOLD, 20);

        // 첫 번째 테이블 (이름, 비밀번호, 아이디)
        String[] personalInfoColumns = {"항목", "정보"};
        Object[][] personalInfoData = {
            {"아이디", id},
            {"비밀번호", id},
            {"이름", "홍길동"}
        };

        DefaultTableModel personalInfoModel = new DefaultTableModel(personalInfoData, personalInfoColumns);
        JTable personalInfoTable = new JTable(personalInfoModel);
        personalInfoTable.setFont(font);
        personalInfoTable.setRowHeight(30);
        personalInfoTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 20));
        JScrollPane personalInfoScrollPane = new JScrollPane(personalInfoTable);

        // 두 번째 테이블 (승률, 금액, 환전한 칩 개수)
        String[] statsColumns = {"항목", "정보"};
        Object[][] statsData = {
            {"승률", winrate + "%"},
            {"금액", "1000"},
            {"환전한 칩 개수", "50"}
        };

        DefaultTableModel statsModel = new DefaultTableModel(statsData, statsColumns);
        JTable statsTable = new JTable(statsModel);
        statsTable.setFont(font);
        statsTable.setRowHeight(30);
        statsTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 20));
        JScrollPane statsScrollPane = new JScrollPane(statsTable);

        // 기존 화면에 보여줄 버튼 패널
        JButton editButton = new JButton("수정");
        JButton exitButton = new JButton("나가기");
        
        editButton.addActionListener(e -> {
            // 수정 화면으로 전환
            CardLayout cardLayout = (CardLayout) mainPane.getLayout();
            cardLayout.show(mainPane, "수정 화면");
        });

        // 버튼 패널 생성 및 추가
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); // 버튼 사이 여백
        buttonPanel.add(exitButton);

        // 기존 화면에 테이블과 버튼 추가
        labelPanel.add(personalInfoScrollPane); // 첫 번째 테이블 추가
        labelPanel.add(Box.createVerticalStrut(10)); // 테이블 사이 여백
        labelPanel.add(statsScrollPane); // 두 번째 테이블 추가
        labelPanel.add(Box.createVerticalStrut(10)); // 버튼 위쪽 여백
        labelPanel.add(buttonPanel); // 버튼 패널 추가

        // 이미지 아이콘과 변경 버튼 추가
        imageLabel = new JLabel(new ImageIcon(imagePaths[currentImageIndex]));
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));
        imagePanel.add(imageLabel);

     // 이미지 아이콘과 변경 버튼 추가
        imageLabel = new JLabel(new ImageIcon(imagePaths[currentImageIndex]));
        JPanel imagePanel1 = new JPanel(new BorderLayout()); // BorderLayout 사용
        imagePanel1.add(imageLabel, BorderLayout.CENTER);

        // 이미지 패널에 왼쪽 여백 추가
        imagePanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 20, 0, 0)); // 왼쪽으로 20픽셀 여백 추가

     // 이미지 설정 제목 라벨 추가
        JLabel title = new JLabel("사용자 이미지 설정", JLabel.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, 22));  // 글자 크기 키우기
        title.setVerticalAlignment(JLabel.BOTTOM);  // 텍스트를 아래로 내리기
        imagePanel1.add(title, BorderLayout.NORTH);

        // 이미지 변경 버튼
        JButton prevButton = new JButton("이전 이미지");
        JButton nextButton = new JButton("다음 이미지");
        JButton saveButton = new JButton("이미지 저장");

        prevButton.addActionListener(e -> changeImage(-1));
        nextButton.addActionListener(e -> changeImage(1));
        saveButton.addActionListener(e -> saveCurrentImage());

        JPanel imageButtonPanel = new JPanel();
        imageButtonPanel.add(prevButton);
        imageButtonPanel.add(nextButton);
        imageButtonPanel.add(saveButton);

        imagePanel1.add(imageButtonPanel, BorderLayout.SOUTH);

        card1.add(labelPanel, BorderLayout.CENTER);
        card1.add(imagePanel1, BorderLayout.WEST);

        // 수정 화면 설정
        JPanel labelPanelEdit = new JPanel();
        labelPanelEdit.setLayout(new BoxLayout(labelPanelEdit, BoxLayout.Y_AXIS));

        JTextField idField = new JTextField(id, 10); // 아이디 입력 필드
        JTextField passwordField = new JTextField(id, 10); // 비밀번호 입력 필드
        JTextField nameField = new JTextField("홍길동", 10); // 이름 입력 필드

        // 수정 버튼 클릭 시 값 반영
        JButton updateButton = new JButton("수정 완료");
        updateButton.addActionListener(e -> {
            String newId = idField.getText();
            String newPassword = passwordField.getText();
            String newName = nameField.getText();
            personalInfoModel.setValueAt(newId, 0, 1);
            personalInfoModel.setValueAt(newPassword, 1, 1);
            personalInfoModel.setValueAt(newName, 2, 1);

            CardLayout cardLayout = (CardLayout) mainPane.getLayout();
            cardLayout.show(mainPane, "기존 화면");
        });

        // 수정 화면에 텍스트 필드와 버튼 추가
        labelPanelEdit.add(new JLabel("아이디:"));
        labelPanelEdit.add(idField);
        labelPanelEdit.add(new JLabel("비밀번호:"));
        labelPanelEdit.add(passwordField);
        labelPanelEdit.add(new JLabel("이름:"));
        labelPanelEdit.add(nameField);
        labelPanelEdit.add(updateButton);

        card2.add(labelPanelEdit, BorderLayout.CENTER);

        // 카드 레이아웃에 두 카드 추가
        mainPane.add(card1, "기존 화면");
        mainPane.add(card2, "수정 화면");

        return mainPane;
    }

    private void changeImage(int direction) {
        // 이미지 인덱스를 이동 (순환)
        currentImageIndex = (currentImageIndex + direction + imagePaths.length) % imagePaths.length;

        // JLabel의 고정 크기 설정 (처음 크기와 동일하게 유지)
        int width = 250;  // 예시로 고정 크기를 400x300으로 설정
        int height = 250;

        // 이미지 아이콘을 스케일링하여 설정
        ImageIcon originalIcon = new ImageIcon(imagePaths[currentImageIndex]);
        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        imageLabel.setIcon(new ImageIcon(scaledImage));
    }

    private void saveCurrentImage() {
        try {
            BufferedImage image = ImageIO.read(new File(imagePaths[currentImageIndex]));
            File outputfile = new File("saved_image.png");
            ImageIO.write(image, "png", outputfile);
            System.out.println("이미지 저장 완료: " + outputfile.getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("이미지 저장 실패: " + ex.getMessage());
        }
    }

    public static void main(String args[]) {
        main = new Userinfo();
        main.startGUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
    }
}
