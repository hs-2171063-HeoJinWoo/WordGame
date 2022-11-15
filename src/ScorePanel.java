import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel {
    private int score = 0;
    private JLabel textLabel = new JLabel("점수");
    private JLabel scoreLabel = new JLabel(Integer.toString(score));

    public ScorePanel() {
        setLayout(null);
        setBackground(Color.YELLOW);
        textLabel.setSize(80, 20);
        textLabel.setLocation(20, 50);

        scoreLabel.setSize(80, 20);
        scoreLabel.setLocation(100, 50);

        add(textLabel);
        add(scoreLabel);
    }

    public void increase() {
        score += 10;
        scoreLabel.setText(Integer.toString(score));
    }
}
