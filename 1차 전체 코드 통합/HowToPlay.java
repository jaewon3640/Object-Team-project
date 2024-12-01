package united;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.LineBorder;


public class HowToPlay extends JPanel {
    public HowToPlay(CardLayout cardLayout, JPanel mainPanel) {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // 상단 제목
        JLabel titleLabel = new JLabel("How to Play", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 34));
        titleLabel.setForeground(Color.YELLOW);
        add(titleLabel, BorderLayout.NORTH);
      Timer  timer = new Timer(500, e -> {
			if (titleLabel.getForeground().equals(Color.YELLOW)) {
				titleLabel.setForeground(Color.BLACK);
			} else {
				titleLabel.setForeground(Color.YELLOW);
			}
		});
      	timer.start();
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
                + "Good Luck!");
        instructions.setFont(new Font("Monospaced", Font.BOLD, 16));
        instructions.setForeground(Color.YELLOW);
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
        paylineExplanation.setFont(new Font("Monospaced", Font.BOLD, 16));
        paylineExplanation.setForeground(Color.YELLOW);
        paylineExplanation.setHorizontalAlignment(SwingConstants.LEFT); // 왼쪽 정렬
        paylineExplanation.setVerticalAlignment(SwingConstants.TOP);
        explanationPanel.add(paylineExplanation, BorderLayout.WEST); // 왼쪽에 완전히 붙이기
        contentPanel.add(explanationPanel);

        // 페이라인 이미지 추가
        ArrayList<String> imagePaths = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            imagePaths.add("imgs/test_payline"+i+".png");
        }
       
       
        for (int i=0;i<imagePaths.size();i++) {
            
        	 JPanel paylinePanel = new JPanel();
             paylinePanel.setLayout(new BorderLayout());
             paylinePanel.setBackground(Color.BLACK);
             
              ImageIcon imageIcon1=new ImageIcon(imagePaths.get(i));
              Image image1=imageIcon1.getImage();
              ImageIcon imageIcon2=new ImageIcon(imagePaths.get(i+1));
              Image image2=imageIcon2.getImage();
              
              int width=image1.getWidth(null)+image2.getWidth(null);
              int height=Math.max(image1.getHeight(null),image2.getHeight(null));
              
              BufferedImage combinedImage=new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
              Graphics g=combinedImage.getGraphics();
              g.drawImage(image1,0,0,null);
              g.drawImage(image2,image1.getWidth(null),0,null);
              ImageIcon combinedIcon=new ImageIcon(combinedImage);
              JLabel imageLabel = new JLabel(combinedIcon);
              paylinePanel.add(imageLabel);
              i++;
           
          //  paylinePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 간격 조정
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
        paylineDetails.setFont(new Font("Monospaced", Font.BOLD, 16));
        paylineDetails.setForeground(Color.YELLOW);
        paylineDetails.setBackground(Color.BLACK);
        paylineDetails.setEditable(false);
        paylineDetails.setLineWrap(true);
        paylineDetails.setWrapStyleWord(true);
        paylineDetails.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        paylineDetails.setBorder(new LineBorder(Color.YELLOW));
      
        contentPanel.add(paylineDetails);

        // 스크롤 패널에 내용 추가
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        JScrollBar vertical=scrollPane.getVerticalScrollBar();
        vertical.setBackground(Color.BLACK);
        vertical.setBlockIncrement(90);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        SwingUtilities.invokeLater(()->{
        	vertical.setValue(0);
        	
        });
      
        add(scrollPane, BorderLayout.CENTER);

        // 뒤로가기 버튼
      
        JButton  backButton = createRetroButton("back", 700, 700, 80, 30);
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "MainPage"));
        add(backButton,BorderLayout.SOUTH);
       
   
    }
    private JButton createRetroButton(String text, int x, int y, int z, int u) {
		JButton button = new JButton(text);
		button.setBounds(x, y, z, u);
		button.setFont(new Font("Monospaced", Font.BOLD, 16));
		button.setBackground(Color.BLACK);
		button.setForeground(Color.YELLOW);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

		button.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				button.setBackground(Color.YELLOW);
				button.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent evt) {
				button.setBackground(Color.BLACK);
				button.setForeground(Color.YELLOW);
			}
		});
		return button;
	}
    
}
