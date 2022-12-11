import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EditPanel extends JPanel{
    private JTextField tf = new JTextField(20);
    private JButton saveBtn = new JButton("save");
    private JLabel info = new JLabel(" ");
    private WordVector wVec = null;

    public EditPanel(WordVector v) {
        wVec = v;
        setLayout(new GridLayout(3, 1));
        // info 라벨 수직 배치
        info.setHorizontalAlignment(JLabel.CENTER);
        AddEvent e = new AddEvent();
        tf.addActionListener(e);
        saveBtn.addActionListener(e);
        add(tf);
        add(saveBtn);
        add(info);
    }

    private class AddEvent implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = tf.getText();
            int status = wVec.addWord(input);

            if (status == 1) {
                info.setText("단어 추가 성공: " + input);
            }
            else if (status == 0) {
                info.setText("이미 있는 단어: " + input);
            }
            else if (status == -1) {
                info.setText("유효하지 않은 단어");
            }
            else if (status == -999) {
                info.setText("치명적인 오류: 파일을 찾을 수 없습니다.");
            }
            tf.setText("");
        }
    }
}
