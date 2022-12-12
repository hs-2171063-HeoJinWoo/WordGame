import javax.swing.*;
import java.awt.*;

public class FacePanel extends JPanel {
    private ImageIcon defaultFace;
    private ImageIcon damageFace;
    private ImageIcon healFace;

    private ImageIcon display;
    private ChangeFace changeFace;

    public FacePanel() {
        defaultFace = new ImageIcon("defaultFace.jpg");
        damageFace = new ImageIcon("damageFace.jpg");
        healFace = new ImageIcon("healFace.jpg");
        display = defaultFace;
    }

    public void setDamageFace() {
        changeFace = new ChangeFace(this, damageFace);
        changeFace.start();
    }

    public void setHealFace() {
        changeFace = new ChangeFace(this, healFace);
        changeFace.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(display.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }

    private class ChangeFace extends Thread {
        private final int HOLDTIME = 2;
        private FacePanel parent;
        private ImageIcon face;

        public ChangeFace(FacePanel parent, ImageIcon face) {
            this.parent = parent;
            this.face = face;
        }

        @Override
        public void run() {
            display = face;
            parent.repaint();
            try {
                sleep(HOLDTIME * 1000);
            } catch (InterruptedException e) {
                display = defaultFace;
                parent.repaint();
            }
            display = defaultFace;
            parent.repaint();
        }
    }
}
