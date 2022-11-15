import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel{
    private JLabel textLabel = new JLabel("이곳을 보세요");
    private JTextField tf = new JTextField(20);
    private GroundPanel groundPanel = new GroundPanel();
    private WordVector wVector = null;
    private ScorePanel scorePanel = null;

    // 생성자 : tf 액션 이벤트 처리를 위해서 입력받는다.
    public GamePanel(WordVector wVector, ScorePanel scorePanel) {
        this.wVector = wVector;
        this.scorePanel = scorePanel;

        setLayout(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(Color.CYAN);
        inputPanel.add(tf);
        add(groundPanel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        tf.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField t = (JTextField)e.getSource();
                if (textLabel.getText().equals(t.getText())) {
                    scorePanel.increase();
                    // textLabel.setLocation(/*랜덤 위치*/);
                    setWord(wVector.getWord());
                    t.setText("");
                }
            }
        });
    }
    
    // FontMaxrix : 폰트에 기반하여 실제 픽셀을 계산해주는 클래스
    private class GroundPanel extends JPanel {
        public GroundPanel() {
            setLayout(null);
            textLabel.setSize(200, 20);
            textLabel.setLocation(100, 30);
            add(textLabel);
        }
    }

    public void setWord(String word) {
        textLabel.setText(word);
    }
}
