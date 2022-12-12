import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.util.Vector;

public class GamePanel extends JPanel{
    private JLabel textLabel = new JLabel("이곳을 보세요");
    private JTextField tf = new JTextField(20);
    private GroundPanel groundPanel = new GroundPanel();
    private WordVector wVector = null;
    private ScorePanel scorePanel = null;
    private Vector<BaseLabel> target = new Vector<BaseLabel>();
    private FallWord th;

    // 생성자 : tf 액션 이벤트 처리를 위해서 입력받는다.
    public GamePanel(WordVector wVector, ScorePanel scorePanel) {
        this.wVector = wVector;
        this.scorePanel = scorePanel;

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
                System.out.println("input: " + input);

                for (int i = 0; i < target.size(); i++) {
                    BaseLabel search = target.get(i);
                    if (search.getText().equals(input)) {

                        if (search instanceof BasicLabel) {
                            scorePanel.increaseScore(input.length());

                        }
                        else if (search instanceof FoodLabel) {
                            scorePanel.increaseScore(input.length() / 2);
                            scorePanel.increaseHungry(input.length());
                        }
                        else {
                            scorePanel.changeLife(1);
                        }
                        invisibleLabel(search);
                        groundPanel.remove(search);
                        target.remove(search);

                        System.out.print(input + ": 삭제");
                        if (search instanceof BasicLabel) {
                            System.out.println("/기본 단어");
                        }
                        else if (search instanceof FoodLabel) {
                            System.out.println("/음식 단어");
                        }
                        else {
                            System.out.println("/생명 단어");
                        }
                    }
                }
                t.setText("");
                groundPanel.repaint();
            }
        });
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

    public void setWord(String word) { textLabel.setText(word); }
    public void start() {
        th = new FallWord(groundPanel);
        th.start();
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
                    speed = r.nextInt(3) + 1;
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
                    if (gen <= 33) { // 생명 단어를 생성한다. 2%의 확률로 생성된다.
                        newLabel = new LifeLabel(word, speed);
                    } else if (gen <= 66) { // 음식 단어를 생성한다. 10%의 확률로 생성된다.
                        newLabel = new FoodLabel(word, speed);
                    } else { // 기본 단어를 생성한다. 88%의 확률로 생성한다.
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
                        // 콘솔 출력을 통한 검증 코드
                        System.out.print(lb.getText() + ": 경계 도달-삭제");
                        if (lb instanceof BasicLabel) {
                            System.out.println("/기본 단어");
                        }
                        else if (lb instanceof FoodLabel) {
                            System.out.println("/음식 단어");
                        }
                        else {
                            System.out.println("/생명 단어");
                        }
                        invisibleLabel(lb);
                        panel.remove(lb);
                        target.remove(i);
                        scorePanel.changeLife(-1);
                    }
                    else lb.setLocation(lb.getX(), nextY);
                }
                panel.repaint();
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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