package united;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class Userinfo extends JPanel implements ActionListener {
    private User user;  // User 객체
    private  DefaultTableModel statsModel;
    private  JTable statsTable ;
    
  

	public Userinfo(User user) {
        this.user = user;  // 생성자에서 User 객체를 전달받음
        setupUI();
    }

    private void setupUI() {
        setLayout(new CardLayout());

        // 첫 번째 카드: 기본 정보 화면
        JPanel card1 = createInfoPanel();

        // 두 번째 카드: 수정 화면
        JPanel card2 = createEditPanel();

        // 카드 추가
        add(card1, "기존 화면");
        add(card2, "수정 화면");
    }

    private JPanel createInfoPanel() {
        JPanel card1 = new JPanel(new BorderLayout());

        // 기본 정보 테이블
        String[] personalInfoColumns = {"항목", "정보"};
        Object[][] personalInfoData = {
            {"아이디", user.getId()},
            {"비밀번호", user.getPassword()},
            {"이름", user.getName()},
            {"생일", user.getBirthdate()}
        };

        DefaultTableModel personalInfoModel = new DefaultTableModel(personalInfoData, personalInfoColumns);
        JTable personalInfoTable = createStyledTable(personalInfoModel);

        // 승률 및 통계 테이블
        String[] statsColumns = {"항목", "정보"};
        Object[][] statsData = {
            {"승률", "100%"},
            {"금액", "1000"},
            {"환전한 칩 개수",0}
        };

         statsModel = new DefaultTableModel(statsData, statsColumns);
         statsTable = createStyledTable(statsModel);

        // 스크롤 팬으로 테이블 감싸기
        JScrollPane personalInfoScrollPane = new JScrollPane(personalInfoTable);
        JScrollPane statsScrollPane = new JScrollPane(statsTable);

        // 버튼
        JButton editButton = new JButton("수정");
        JButton exitButton = new JButton("나가기");

        editButton.addActionListener(e -> {
            CardLayout cardLayout = (CardLayout) getLayout();
            cardLayout.show(this, "수정 화면");
        });

        exitButton.addActionListener(e -> {
            // 메인 페이지로 돌아가기 (MainPage에서 관리되도록 수정)
            CardLayout cardLayout = (CardLayout) getParent().getLayout();
            cardLayout.show(getParent(), "MainPage"); // MainPage로 돌아가기
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(editButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(exitButton);

        // 카드 구성
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.add(personalInfoScrollPane);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(statsScrollPane);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(buttonPanel);

        card1.add(labelPanel, BorderLayout.CENTER);

        return card1;
    }

    private JPanel createEditPanel() {
        JPanel card2 = new JPanel(new BorderLayout());

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

            // 기존 화면으로 돌아가기
            CardLayout cardLayout = (CardLayout) getLayout();
            cardLayout.show(this, "기존 화면");
        });

        labelPanelEdit.add(new JLabel("아이디:"));
        labelPanelEdit.add(idField);
        labelPanelEdit.add(new JLabel("비밀번호:"));
        labelPanelEdit.add(passwordField);
        labelPanelEdit.add(new JLabel("이름:"));
        labelPanelEdit.add(nameField);
        labelPanelEdit.add(new JLabel("생일:"));
        labelPanelEdit.add(birthField);
        labelPanelEdit.add(Box.createVerticalStrut(10));
        labelPanelEdit.add(updateButton);

        card2.add(labelPanelEdit, BorderLayout.CENTER);

        return card2;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        Font tableFont = new Font("맑은 고딕", Font.PLAIN, 18);

        table.setFont(tableFont);
        table.setRowHeight(40);
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 20));

        return table;
    }

    public void updateTableData(int row, int column, Object newValue) {
        statsModel.setValueAt(newValue, row, column);
    }

    public JTable getTable() {
        return statsTable;
    }

    public DefaultTableModel getTableModel() {
        return statsModel;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // 필요한 경우 구현
    }
}
