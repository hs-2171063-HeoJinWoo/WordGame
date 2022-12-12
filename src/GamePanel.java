import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GamePanel extends JPanel{
    private JTextField tf = new JTextField(20);
    private GroundPanel groundPanel = new GroundPanel();
    private WordVector wVector = null;
    private ScorePanel scorePanel = null;
    private FacePanel facePanel = null;
    private Vector<BaseLabel> target = new Vector<BaseLabel>();
    private FallWord th;
    private boolean isPlaySound = true;
    private int difficulty = 1;

    // 생성자 : tf 액션 이벤트 처리를 위해서 입력받는다.
    public GamePanel(WordVector wVector, ScorePanel scorePanel, FacePanel facePanel) {
        this.wVector = wVector;
        this.scorePanel = scorePanel;
        this.facePanel = facePanel;

        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(24, 67, 100));
        inputPanel.add(tf);
        add(groundPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField t = (JTextField)e.getSource();
                String input = t.getText();

                for (int i = 0; i < target.size(); i++) {
                    BaseLabel search = target.get(i);
                    if (search.getText().equals(input)) {
                        if (search instanceof BasicLabel) {
                            scorePanel.increaseScore(input.length());
                            if (isPlaySound) audio("basic");
                        }
                        else if (search instanceof FoodLabel) {
                            scorePanel.increaseScore(input.length() / 2);
                            scorePanel.increaseHungry(input.length());
                            if (isPlaySound) audio("eat");
                        }
                        else {
                            scorePanel.changeLife(1);
                            if (isPlaySound) audio("heal");
                        }
                        facePanel.setHealFace();
                        invisibleLabel(search);
                        groundPanel.remove(search);
                        target.remove(search);
                    }
                }
                t.setText("");
                groundPanel.repaint();
            }
        });
    }

    public void setPlaySound(boolean flag) {
        isPlaySound = flag;
    }

    public void setDifficulty(int diff) {
        difficulty = diff;
    }

    public void audio(String fileName) {
        try {
            File file = new File(fileName + ".wav");
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(file));
            clip.start();
        } catch (Exception e) {
            System.out.println(fileName + ": 찾을 수 없음.");
            return;
        }
    }

    private void invisibleLabel(BaseLabel lb) {
        lb.setBackground(null);
        lb.setForeground(null);
        lb.setText("");
    }

    // FontMaxrix : 폰트에 기반하여 실제 픽셀을 계산해주는 클래스
    private class GroundPanel extends JPanel {
        private ImageIcon icon = new ImageIcon("background.jpg");
        private Image img = icon.getImage();

        public GroundPanel() {
            setLayout(null);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    public void start() {
        th = new FallWord(groundPanel);
        th.start();
    }

    public void interrupt() {
        th.interrupt();
    }

    class FallWord extends Thread {
        private GroundPanel panel;
        Random r = new Random();
        public FallWord(GroundPanel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            BaseLabel newLabel;
            String word;

            while(true) {
                // 단어의 개수가 10개 미만일 때, 50%의 확률로 생성한다.
                if (target.size() < 10 && r.nextInt(2) == 1) {
                    int newX, newY, speed, gen;
                    speed = r.nextInt(3) + difficulty;
                    gen = r.nextInt(100) + 1;
                    word = wVector.getWord();

//                    if (gen <= 2) { // 생명 단어를 생성한다. 2%의 확률로 생성된다.
//                        newLabel = new LifeLabel(word, speed);
//                    } else if (gen <= 12) { // 음식 단어를 생성한다. 10%의 확률로 생성된다.
//                        newLabel = new hungry(word, speed);
//                    } else { // 기본 단어를 생성한다. 88%의 확률로 생성한다.
//                        newLabel = new BasicLabel(word, speed);
//                    }

                    // 테스트용 확률 조정
                    if (gen <= 2) { // 생명 단어를 생성한다. 2%의 확률로 생성된다.
                        newLabel = new LifeLabel(word, speed);
                    } else if (gen <= 22) { // 음식 단어를 생성한다. 20%의 확률로 생성된다.
                        newLabel = new FoodLabel(word, speed);
                    } else { // 기본 단어를 생성한다. 78%의 확률로 생성한다.
                        newLabel = new BasicLabel(word, speed);
                    }

                    // 랜덤 위치 지정
                    newX = r.nextInt(panel.getWidth() - newLabel.getWidth());
                    newY = r.nextInt(panel.getHeight() / 4);
                    newLabel.setLocation(newX, newY);
                    panel.add(newLabel);
                    target.add(newLabel);

                }
                // 단어 움직이기.
                for(int i = 0; i < target.size(); i++) {
                    BaseLabel lb = target.get(i);
                    int nextY = lb.getY() + lb.getSpeed();

                    // 범위를 넘어간 경우
                    if (nextY >= panel.getHeight() && !(lb.getText().equals(""))) {
                        invisibleLabel(lb);
                        panel.remove(lb);
                        target.remove(i);
                        scorePanel.changeLife(-1);
                        facePanel.setDamageFace();
                    }
                    else lb.setLocation(lb.getX(), nextY);
                }
                panel.repaint();
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }
}

class BaseLabel extends JLabel {
    protected int speed;
    public BaseLabel(String word, int speed) {
        super(word);
        this.speed = speed;
        setOpaque(true);
        setSize(120, 20);
        setFont(new Font("나눔고딕", Font.PLAIN, 16));
        setHorizontalAlignment(JLabel.CENTER);
    }
    public int getSpeed() { return speed; }
}

class BasicLabel extends BaseLabel {
    public BasicLabel(String word, int speed) {
        super(word, speed);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);
    }
}

class FoodLabel extends BaseLabel {
    public FoodLabel(String word, int speed) {
        super(word, speed);
        setBackground(Color.CYAN);
        setForeground(Color.BLACK);
    }
}

class LifeLabel extends BaseLabel {
    public LifeLabel(String word, int speed) {
        super(word, speed);
        setBackground(Color.RED);
        setForeground(Color.WHITE);
    }
}