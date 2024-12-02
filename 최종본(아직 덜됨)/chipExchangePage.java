package united;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;

public class chipExchangePage extends JPanel {
    private JTextField txtmoney = new JTextField(15);
    private JButton excbutton;
    private JButton backbutton;
    private JLabel label;
    private JLabel confirm;
    private int chips;
    private User user;
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private Timer timer;
    private ArrayList<Chip> chip = new ArrayList<>();
    private Timer chipTimer;
    private Image chipImage;
    private Userinfo userInfoPage;

    public chipExchangePage(User user, CardLayout cardLayout, JPanel mainContainer, Userinfo userInfoPage) {
        this.user = user;
        this.cardLayout = cardLayout;
        this.mainContainer = mainContainer;
        this.userInfoPage = userInfoPage;
        chipImage = new ImageIcon("imgs/coin.png").getImage();

        setLayout(null);
        setBackground(Color.BLACK);

        // 컴포넌트 초기화
        initializeComponents();

        // 창 크기 조정 리스너 추가
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentSizes();
            }
        });

        // 칩 애니메이션 설정
        initializeChipAnimation();
    }

    private void initializeComponents() {
        // Label 설정
        label = new JLabel("How much?:");
        label.setForeground(Color.YELLOW);
        label.setFont(new Font("Monospaced", Font.BOLD, 15));
        add(label);

        // TextField 설정
        txtmoney.setBackground(Color.BLACK);
        txtmoney.setForeground(Color.CYAN);
        txtmoney.setFont(new Font("Monospaced", Font.BOLD, 12));
        txtmoney.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        add(txtmoney);

        // Confirm Label 설정
        confirm = new JLabel("", SwingConstants.CENTER);
        confirm.setFont(new Font("Monospaced", Font.BOLD, 45));
        confirm.setForeground(Color.CYAN);
        confirm.setVisible(false);
        add(confirm);

        timer = new Timer(300, e -> {
            if (confirm.getForeground().equals(Color.CYAN)) {
                confirm.setForeground(Color.BLACK);
            } else {
                confirm.setForeground(Color.CYAN);
            }
        });

        // 교환 버튼 설정
        excbutton = createRetroButton("Exchange");
        excbutton.addActionListener(e -> handleExchange());
        add(excbutton);

        // 뒤로가기 버튼 설정
        backbutton = createRetroButton("Back");
        backbutton.addActionListener(e -> handleBackButton());
        add(backbutton);
    }

    private void initializeChipAnimation() {
        Random rand = new Random();
        chipTimer = new Timer(50, e -> {
            if (rand.nextInt(10) < 4) {
                int x = rand.nextInt(this.getWidth());
                int y = rand.nextInt(this.getHeight());
                chip.add(new Chip(x, y, chipImage));
            }
            Iterator<Chip> iterator = chip.iterator();
            while (iterator.hasNext()) {
                Chip chip = iterator.next();
                if (!chip.update()) {
                    iterator.remove();
                }
            }
            this.repaint();
        });
    }

    private void adjustComponentSizes() {
        int width = getWidth();
        int height = getHeight();

        int centerX = width / 2;
        int centerY = height / 2;

        // "How much?" 라벨 위치 조정
        label.setBounds(centerX - 100, centerY - 80, 100, 40);
        
        // 텍스트필드 위치 조정
        txtmoney.setBounds(centerX , centerY - 70, 80, 20);
        
        // "Confirm" 라벨 위치 조정
        confirm.setBounds(centerX - 300, centerY - 200, 600, 100);
        
        // "Exchange" 버튼 위치 조정
        excbutton.setBounds(centerX - 100, centerY + 20, 200, 50); // x좌표를 "How much?" 라벨과 맞춤
        
        // 뒤로가기 버튼 위치 조정
        backbutton.setBounds(width - 120, height - 50, 100, 30);
    }


    private void handleExchange() {
        if (txtmoney.getText().length() >= 5) {
            int result = JOptionPane.showConfirmDialog(null, "Confirm exchange?", "Exchange Confirm",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    int acquiredChips = getChip();
                    confirm.setText(acquiredChips + " Chips Acquired!");
                    chipTimer.start();
                    confirm.setVisible(true);
                    timer.start();

                    user.setChipNum(user.getChipNum() + acquiredChips);
                    userInfoPage.updateChipLabel(user.getChipNum());
                    txtmoney.setText("");

                    updateTestPageChipLabel();
                } catch (NumberFormatException n) {
                    showErrorDialog("Exceeded maximum money amount");
                }
            } else {
                showErrorDialog("Exchange cancelled");
            }
        } else {
            showErrorDialog("Enter at least 10000 KRW.");
        }
    }

    private void handleBackButton() {
        confirm.setVisible(false);
        chip.clear();
        chipTimer.stop();
        this.repaint();
        cardLayout.show(mainContainer, "MainPage");
        updateTestPageChipLabel();
    }

    private int getChip() {
        int money = Integer.parseInt(txtmoney.getText());
        return money / 1000;
    }

    private void updateTestPageChipLabel() {
        for (Component component : mainContainer.getComponents()) {
            if (component instanceof testpage) {
                testpage testPage = (testpage) component;
                testPage.updateChipLabel();
            }
        }
    }

    private void showErrorDialog(String message) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("Monospaced", Font.BOLD, 15));
        label.setForeground(Color.RED);
        JOptionPane.showMessageDialog(null, label, "ERROR", JOptionPane.WARNING_MESSAGE);
    }

    private JButton createRetroButton(String text) {
        JButton button = new JButton(text);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);

        // "Insert Money" 텍스트를 가로로만 중앙에 위치시킴
        g.setColor(Color.YELLOW);
        g.setFont(new Font("Monospaced", Font.BOLD, 60));

        // 텍스트의 너비를 계산
        FontMetrics metrics = g.getFontMetrics();
        int textWidth = metrics.stringWidth("Insert Money, " + user.getName() + "!");

        // 텍스트가 가로로 중앙에 오도록 x 좌표 계산
        int x = (getWidth() - textWidth) / 2;
        int y = 100; // y 좌표는 고정 (원래 위치)

        // 텍스트 그리기
        g.drawString("Insert Money, " + user.getName() + "!", x, y);

        // 칩 애니메이션 그리기
        for (Chip c : chip) {
            c.draw(g);
        }
    }


    private static class Chip {
        private int x, y, alpha;
        private Image image;

        public Chip(int x, int y, Image image) {
            this.x = x;
            this.y = y;
            this.image = image;
            this.alpha = 255;
        }

        public boolean update() {
            y += 2;
            alpha -= 10;
            return alpha > 0;
        }

        public void draw(Graphics g) {
            if (alpha > 0) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha / 255f));
                g2d.drawImage(image, x, y, 80, 80, null);
            }
        }
    }
}
