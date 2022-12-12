import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
	private JButton startBtn = new JButton("start");
	private	JButton breakBtn = new JButton("break");
	private	JButton configBtn = new JButton("Config");
	private ScorePanel scorePanel = new ScorePanel();
	private WordVector wordVector = new WordVector();
	private FacePanel facePanel = new FacePanel();
	private GamePanel gamePanel = new GamePanel(wordVector, scorePanel, facePanel);
	private ConfigDialog configdia = new ConfigDialog(this, "config");

	public GameFrame () {
		super("survive on island");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1280, 960);
		makeToolbar();
		makeSplitPane();

		setVisible(true);
		setResizable(false);
	}

	private void makeSplitPane() {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(900);
		getContentPane().add(hPane, BorderLayout.CENTER);

		JSplitPane vPane = new JSplitPane();
		vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		vPane.setDividerLocation(400);
		vPane.setTopComponent(facePanel);
		vPane.setBottomComponent(scorePanel);

		hPane.setLeftComponent(gamePanel);
		hPane.setRightComponent(vPane);
	}

	private void makeToolbar() {
		JToolBar tBar = new JToolBar();
		getContentPane().add(tBar, BorderLayout.NORTH);

		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.start();
				scorePanel.start();
			}
		});

		configBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				configdia.setVisible(true);

				boolean soundFlag = configdia.getPlaySound();
				int diff = configdia.getDifficulty();

				gamePanel.setPlaySound(soundFlag);
				gamePanel.setDifficulty(diff);

			}
		});
		tBar.add(startBtn);
		tBar.add(configBtn);
	}

	private class ConfigDialog extends JDialog {
		private JLabel soundLabel = new JLabel("효과음");
		private JRadioButton isPlaySound[] = new JRadioButton[2];
		private JLabel difficultyLabel = new JLabel("난이도");
		private JSlider difficulty = new JSlider(JSlider.HORIZONTAL, 1, 5,1);
		private JScrollPane wordList = new JScrollPane(new JList<String>(wordVector.getWordList()));
		private JTextField input = new JTextField(15);
		private JButton addBtn = new JButton("추가");
		private JLabel info = new JLabel("");
		private JButton acceptBtn = new JButton("확인");

		private Font font = new Font("나눔고딕", Font.BOLD, 16);

		public ConfigDialog(JFrame frame, String title) {
			super(frame, title, true);
			setLayout(null);
//			setResizable(false);
			setSize(350, 500);

			soundLabel.setFont(font);
			soundLabel.setSize(80, 20);
			soundLabel.setLocation(10, 10);
			add(soundLabel);

			isPlaySound[0] = new JRadioButton("on", true);
			isPlaySound[0].setSize(60, 60);
			isPlaySound[0].setBorderPainted(true);
			isPlaySound[0].setLocation(soundLabel.getX(),
					               soundLabel.getY() + soundLabel.getHeight() + 10);
			add(isPlaySound[0]);

			isPlaySound[1] = new JRadioButton("off");
			isPlaySound[1].setSize(60, 60);
			isPlaySound[1].setBorderPainted(true);
			isPlaySound[1].setLocation(isPlaySound[0].getX() + isPlaySound[0].getWidth() + 10,
					                   isPlaySound[0].getY());
			add(isPlaySound[1]);

			ButtonGroup g = new ButtonGroup();
			g.add(isPlaySound[0]);
			g.add(isPlaySound[1]);

			difficultyLabel.setFont(font);
			difficultyLabel.setSize(80, 20);
			difficultyLabel.setLocation(isPlaySound[0].getX(),
					                    isPlaySound[0].getY() + isPlaySound[0].getHeight() + 30);
			add(difficultyLabel);

			difficulty.setPaintLabels(true);
			difficulty.setPaintTicks(true);
			difficulty.setPaintTrack(true);
			difficulty.setMajorTickSpacing(1);
			difficulty.setSize(250, 50);
			difficulty.setLocation(difficultyLabel.getX(),
					               difficultyLabel.getY() + difficultyLabel.getHeight() + 10);
			add(difficulty);

			wordList.setSize(100, 150);
			wordList.setLocation(difficulty.getX(),
					             difficulty.getY() + difficulty.getHeight() + 20);
			add(wordList);

			input.setSize(100, 20);
			input.setLocation(wordList.getX() + wordList.getWidth() + 10,
			                  wordList.getY());
			add(input);

			info.setSize(200, 20);
			info.setLocation(input.getX(),
					      input.getY() + input.getHeight() + 10);
			add(info);

			addBtn.setSize(80, 20);
			addBtn.setLocation(input.getX() + input.getWidth() + 10,
					           input.getY());
			addBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String target = input.getText();

					int status = wordVector.addWord(target);

					if (status == 1) {
						info.setText("단어 추가 성공: " + target);
					}
					else if (status == 0) {
						info.setText("이미 있는 단어: " + target);
					}
					else if (status == -1) {
						info.setText("유효하지 않은 단어");
					}
					else if (status == -999) {
						info.setText("치명적인 오류: 파일을 찾을 수 없습니다.");
					}

					input.setText("");
				}
			});
			add(addBtn);

			acceptBtn.setSize(80, 20);
			acceptBtn.setFont(font);
			acceptBtn.setLocation(135, 440);
			acceptBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setVisible(false);
				}
			});
			add(acceptBtn);
		}

		public boolean getPlaySound() {
			if (isPlaySound[0].isSelected()) return true;
			else if (isPlaySound[1].isSelected()) return false;
			else return true;
		}

		public int getDifficulty() {
			return difficulty.getValue();
		}
	}
}
