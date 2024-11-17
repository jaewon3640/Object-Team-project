package Userinfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ranking extends JFrame {

    public Ranking() {
        setTitle("게임 랭킹");
        setSize(700, 500); // 창 크기 수정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 상단 제목 추가
        JLabel titleLabel = new JLabel("Ranking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // 여백 추가        
        
        // User.txt 데이터를 읽어와 JTable에 표시
        String[] columnNames = {"랭킹", "아이디", "비밀번호", "이름", "생일", "점수"};
        List<userread> userData = readUserFile("user.txt");

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        int rank = 1;
        for (userread user : userData) {
            tableModel.addRow(new Object[]{
                    rank++, // 랭킹
                    user.getId(),
                    user.getPassword(),
                    user.getName(),
                    user.getBirthdate(),
                    user.getScore()
            });
        }

        JTable userTable = new JTable(tableModel);

        // 테이블의 폰트와 행 높이 설정
        userTable.setFont(new Font("Serif", Font.PLAIN, 14));
        userTable.setRowHeight(25);

        // 테이블을 스크롤 패널에 추가
        JScrollPane scrollPane = new JScrollPane(userTable);

        // 레이아웃에 컴포넌트 추가
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH); // 제목을 위쪽에 추가
        add(scrollPane, BorderLayout.CENTER); // 테이블은 중앙에 추가

        setVisible(true);
    }

    // User.txt를 읽는 메서드
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

    public static void main(String[] args) {
        // User.txt 데이터를 표시하는 테이블 실행
        new Ranking();
    }
}
