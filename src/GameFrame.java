import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
	private ImageIcon normalIcon = new ImageIcon("normal.jpg");
	private ImageIcon pressedIcon = new ImageIcon("pressed.jpg");
	private ImageIcon overredIcon = new ImageIcon("overred.jpg");
	private JButton startBtn = new JButton(normalIcon);
	private ScorePanel scorePanel = new ScorePanel();
	private EditPanel editPanel = new EditPanel();
	private WordVector wordVector = new WordVector();
	private GamePanel gamePanel = new GamePanel(wordVector, scorePanel);

	public GameFrame () {
		super("게임");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		makeMenu();
		makeToolbar();
		makeSplitPane();

		setVisible(true);
	}

	private void makeSplitPane() {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(500);
		getContentPane().add(hPane, BorderLayout.CENTER);

		JSplitPane vPane = new JSplitPane();
		vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		vPane.setDividerLocation(300);
		vPane.setTopComponent(scorePanel);
		vPane.setBottomComponent(editPanel);

		hPane.setLeftComponent(gamePanel);
		hPane.setRightComponent(vPane);
	}

	private void makeMenu() {
		JMenuBar bar = new JMenuBar();
		this.setJMenuBar(bar);

		JMenu fMenu = new JMenu("파일");
		JMenu eMenu = new JMenu("편집");
		bar.add(fMenu);
		bar.add(eMenu);

		JMenuItem openItem = new JMenuItem("열기");
		JMenuItem saveItem = new JMenuItem("저장");
		JMenuItem exitItem = new JMenuItem("종료");

		fMenu.add(openItem);
		fMenu.add(saveItem);
		fMenu.addSeparator();
		fMenu.add(exitItem);
	}

	private void makeToolbar() {
		JToolBar tBar = new JToolBar();
		getContentPane().add(tBar, BorderLayout.NORTH);
		JButton breakBtn = new JButton("break");
		startBtn.setPressedIcon(pressedIcon);
		startBtn.setRolloverIcon(overredIcon);
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String word = wordVector.getWord();
				gamePanel.setWord(word);
			}
		});
		tBar.add(startBtn);
		tBar.add(breakBtn);
	}
}
