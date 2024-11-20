

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.*;


public class GUIMain {
	public void startGUI() {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	static JFrame mainFrame = new JFrame("TableSelectionDemo");
	static GUIMain main = null;

	private void createAndShowGUI() {
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel pane = setupMainPanel();
		mainFrame.setPreferredSize(new Dimension(500, 300));
		mainFrame.getContentPane().add(pane);
// Display the window. mainFrame.pack();
		mainFrame.setVisible(true);
	}

	JPanel setupMainPanel() {
		JPanel mainPane = new JPanel(new BorderLayout());
		// Create and set up the content pane.
		JButton redButton = new JButton("Red");
		redButton.setSize(new Dimension(300, 200));
		JButton blueButton = new JButton("Blue");
		redButton.setSize(new Dimension(300, 200));
		mainPane.add(redButton, BorderLayout.PAGE_START);
		mainPane.add(blueButton, BorderLayout.PAGE_END);
		JPanel center = new JPanel();
		center.setBackground(Color.white);
		JLabel title = new JLabel("Color");
		title.setOpaque(true);
		title.setBackground(Color.yellow);
		center.add(title);
		mainPane.add(center);
		return mainPane;
	}

	public static void main(String args[]) {
		main = new GUIMain();
		main.startGUI();
	}
}
