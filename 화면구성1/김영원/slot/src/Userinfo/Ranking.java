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
        setSize(700, 500); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 창 닫기 옵션 변경
        setLocationRelativeTo(null);

        // 상단 제목 추가
        JLabel titleLabel = new JLabel("Ranking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // 테이블에 사용자 데이터 표시
        String[] columnNames = {"랭킹", "아이디", "이름", "생일", "점수"};
        List<userread> userData = readUserFile("user.txt");

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        int rank = 1;
        for (userread user : userData) {
            tableModel.addRow(new Object[] {
                rank++, 
                user.getId(),
                user.getName(),
                user.getBirthdate(),
                user.getScore()
            });
        }

        JTable userTable = new JTable(tableModel);
        userTable.setFont(new Font("Serif", Font.PLAIN, 14));
        userTable.setRowHeight(25);

        // 스크롤 패널 추가
        JScrollPane scrollPane = new JScrollPane(userTable);

        // 레이아웃 배치
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // User.txt 읽기
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

        userData.sort((u1, u2) -> Integer.compare(u2.getScore(), u1.getScore())); // 정렬 추가
        return userData;
    }
}