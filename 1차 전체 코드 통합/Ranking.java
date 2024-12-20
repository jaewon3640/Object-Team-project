package united;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Ranking extends JPanel {
	// Ranking 클래스
	public Ranking(CardLayout cardLayout, JPanel mainContainer) {
		setLayout(new BorderLayout());
		setSize(700, 500); // 창 크기 수정
		setBackground(Color.BLACK); // 배경을 검정색으로 설정

		// 상단 제목 추가
		JLabel titleLabel = new JLabel("Ranking", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 42)); // 폰트를 Monospaced로 설정
		titleLabel.setForeground(Color.YELLOW); // 글씨 색을 노란색으로 설정
		titleLabel.setBorder(BorderFactory.createEmptyBorder(27, 0, 27, 0)); // 여백 추가

		// User.txt 데이터를 읽어와 JTable에 표시
		String[] columnNames = { "RANKING", "ID", "NAME", "BIRTHDATE", "SCORE" };
		List<userread> userData = readUserFile("user.txt");

		// 점수 내림차순으로 정렬 (랭킹 순)
		userData.sort((user1, user2) -> Integer.compare(user2.getScore(), user1.getScore()));

		DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
		int rank = 1;
		for (userread user : userData) {
			tableModel.addRow(new Object[] { rank++, // 랭킹
					user.getId(), user.getName(), user.getBirthdate(), user.getScore() });
		}

		JTable userTable = new JTable(tableModel);

		// 테이블의 폰트와 행 높이 설정
		userTable.setFont(new Font("Monospaced", Font.PLAIN, 14)); // 폰트를 Monospaced로 설정
		userTable.setRowHeight(45); // 셀의 세로 길이를 약간 늘림 (기존 30 -> 35)
		userTable.setForeground(Color.YELLOW); // 글씨 색을 노란색으로 설정
		userTable.setBackground(Color.BLACK); // 테이블 배경을 검정색으로 설정

		// 테이블 헤더 스타일 수정
		JTableHeader tableHeader = userTable.getTableHeader();
		tableHeader.setBackground(Color.BLACK); // 헤더 배경을 검정색으로 설정
		tableHeader.setForeground(Color.YELLOW); // 헤더 글씨 색을 노란색으로 설정
		tableHeader.setFont(new Font("Monospaced", Font.BOLD, 14)); // 헤더 폰트 설정

		tableHeader.setPreferredSize(new Dimension(tableHeader.getPreferredSize().width, 50));

		// 셀 렌더러 설정
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setBackground(Color.BLACK);
		cellRenderer.setForeground(Color.YELLOW);
		cellRenderer.setHorizontalAlignment(SwingConstants.CENTER); // 가운데 정렬
		for (int i = 0; i < userTable.getColumnCount(); i++) {
			userTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
		}

		// 테이블 그리드 선을 보이도록 설정, 그리드 색상을 투명하게 설정
		userTable.setShowGrid(true); // 그리드(셀 경계선) 보이게 설정
		userTable.setGridColor(new Color(0, 0, 0, 0)); // 그리드 색상을 투명하게 설정

		// 테이블을 스크롤 패널에 추가
		JScrollPane scrollPane = new JScrollPane(userTable);
		scrollPane.getViewport().setBackground(Color.BLACK); // 스크롤 패널 배경 검정색
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 테두리 제거

		// 레이아웃에 컴포넌트 추가
		add(titleLabel, BorderLayout.NORTH); // 제목을 위쪽에 추가
		add(scrollPane, BorderLayout.CENTER); // 테이블은 중앙에 추가

		// 뒤로가기 버튼 추가
		JButton backButton = new JButton("Back");
		backButton.setFont(new Font("Monospaced", Font.BOLD, 16)); // 버튼 폰트 설정
		backButton.setForeground(Color.YELLOW); // 버튼 글씨 색
		backButton.setBackground(Color.BLACK); // 버튼 배경 색
		backButton.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2)); // 노란색 테두리
		backButton.setPreferredSize(new Dimension(120, 40)); // 버튼 크기 조정
		backButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				backButton.setBackground(Color.YELLOW);
				backButton.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				backButton.setBackground(Color.BLACK);
				backButton.setForeground(Color.YELLOW);
			}
		});
		backButton.addActionListener(e -> cardLayout.show(mainContainer, "MainPage")); // 뒤로가기 버튼 클릭 시 MainPage로 이동

		// GridBagLayout을 이용하여 버튼 위치 조정
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		buttonPanel.setBackground(Color.BLACK); // 버튼 패널 배경색

		// GridBagConstraints로 위치 설정 (버튼을 아래에서 위로 올리기)
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(20, 0, 20, 0); // 위와 아래 여백 추가
		buttonPanel.add(backButton, gbc); // 버튼 추가

		add(buttonPanel, BorderLayout.SOUTH); // 버튼은 아래쪽에 추가
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
		// 참고로 main에서 바로 실행하려면 CardLayout과 mainContainer를 넘겨줘야 합니다.
	}
}