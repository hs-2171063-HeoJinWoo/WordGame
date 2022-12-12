import java.awt.*;

import javax.swing.*;

public class ScorePanel extends JPanel {
    private int score = 0;
    private int hungry = 100;
    private int life = 3;
    private int maxScore = 20;
    private int maxHungry = 100;
    private int maxLife = 3;

    private JLabel scoreNameLabel = new JLabel("구출도: " + Integer.toString(score));
    private BarLabel scoreBar = new BarLabel(maxScore, Color.cyan, Color.black);
    private JLabel hungryNameLabel = new JLabel("허기: " + Integer.toString(hungry));
    private DecreaseBarLabel hungryBar = new DecreaseBarLabel(maxHungry, Color.green, Color.black, this);
    private JLabel lifeNameLabel = new JLabel("생명: " + Integer.toString(life));
    private JLabel heart[] = new JLabel[maxLife];

    private Font labelFont = new Font("나눔고딕", Font.PLAIN, 16);
    private ImageIcon icon = new ImageIcon("backpack.jpg");
    private Image img = icon.getImage();
    private ImageIcon fillHeartIcon = new ImageIcon("fillHeart.jpg");
    private ImageIcon voidHeartIcon = new ImageIcon("voidHeart.jpg");

    public ScorePanel() {
        setLayout(null);
        setBackground(Color.YELLOW);

        scoreNameLabel.setFont(labelFont);
        scoreNameLabel.setOpaque(true);
        scoreNameLabel.setSize(80, 20);
        scoreNameLabel.setLocation(20, 50);
        scoreBar.setLocation(20, 72);

        hungryNameLabel.setFont(labelFont);
        hungryNameLabel.setOpaque(true);
        hungryNameLabel.setSize(80, 20);
        hungryNameLabel.setLocation(20, 100);
        hungryBar.setLocation(20, 122);

        lifeNameLabel.setSize(80, 20);
        lifeNameLabel.setLocation(20, 150);
        lifeNameLabel.setFont(labelFont);
        lifeNameLabel.setOpaque(true);

        for (int i = 0; i < life; i++) {
            heart[i] = new JLabel(fillHeartIcon);
            heart[i].setSize(fillHeartIcon.getIconWidth(), fillHeartIcon.getIconHeight());
            heart[i].setLocation(20 + (fillHeartIcon.getIconWidth() + 5) * i, 170);
            add(heart[i]);
        }

        add(scoreNameLabel);
        add(scoreBar);
        add(hungryNameLabel);
        add(hungryBar);
        add(lifeNameLabel);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public void increaseScore(int baseScore) {
        score += baseScore;
        scoreNameLabel.setText("구출도: " + Integer.toString(score));
        scoreBar.fillBar(baseScore);
    }

    public synchronized void increaseHungry(int hungry) {
        this.hungry += hungry;
        hungryNameLabel.setText("허기: " + Integer.toString((this.hungry)));
        hungryBar.fillBar(hungry);
    }

    public void changeLife(int dlife) {
        // 생명이 최대인 경우에서 추가하려는 경우
        if (life == maxLife && dlife > 0) return;

        int i;
        life += dlife;

        if (life == 0) System.exit(0);
        lifeNameLabel.setText("생명: " + Integer.toString(life));

        for (i = 0; i < life; i++) {
            JLabel lb = heart[i];
            lb.setIcon(fillHeartIcon);
        }
        for (; i < heart.length; i++) {
            JLabel lb = heart[i];
            lb.setIcon(voidHeartIcon);
        }
    }

    public void start() {
        hungryBar.start();
    }
}

class BarLabel extends JLabel {
    protected int barSize = 0;
    protected int maxBarSize = 0;
    protected Color barColor;
    protected Color backGroundColor;

    public BarLabel(int maxBarSize, Color barColor, Color backGroundColor) {
        setOpaque(true);
        this.maxBarSize = maxBarSize;
        this.barColor = barColor;
        this.backGroundColor = backGroundColor;
        setSize(220, 20);
        setBackground(backGroundColor);
    }

    public void fillBar(int n) {
        barSize += n;
        this.getParent().repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(barColor);
        int width = (int)(((double)(this.getWidth())) / maxBarSize * barSize);
        if (width == 0) return;
        g.fillRect(0, 0, width, this.getHeight());
    }
}

class DecreaseBarLabel extends BarLabel {
    private Decrease th;
    private ScorePanel parent;

    public DecreaseBarLabel(int maxBarSize, Color barColor, Color backGroundColor, ScorePanel panel) {
        super(maxBarSize, barColor, backGroundColor);
        barSize = maxBarSize;
        parent = panel;
        th = new Decrease(this);
    }

    public void start () {
        th.start();
    }

    class Decrease extends Thread {
        private DecreaseBarLabel panel;
        private int decreaseValue = 2;
        public Decrease(DecreaseBarLabel panel) {
            this.panel = panel;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    return;
                }
                if (barSize < decreaseValue) {
                    barSize = 0; // 게임 종료처리 필요
                    System.out.println("끝");
//                    System.exit(0);
                }
                parent.increaseHungry(-1 * decreaseValue);
            }
        }
    }
}