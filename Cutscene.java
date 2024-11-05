import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Cutscene extends JPanel {
    private Image[] cutsceneImages;
    private int currentImageIndex = 0;
    private static final int DELAY = 3000; 

    public Cutscene(JFrame frame) {
        loadImages();
        Timer timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex++;
                if (currentImageIndex >= cutsceneImages.length) {
                    ((Timer) e.getSource()).stop();
                    startGame(frame);
                }
                repaint();
            }
        });
        timer.start();
    }

    private void loadImages() {
      
        cutsceneImages = new Image[3]; 
        cutsceneImages[0] = new ImageIcon("assets\\git\\cut2.gif").getImage();
        cutsceneImages[1] = new ImageIcon("assets\\git\\cut3.gif").getImage();
        cutsceneImages[2] = new ImageIcon("assets\\git\\cut1.gif").getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (cutsceneImages.length > 0) {
            g.drawImage(cutsceneImages[currentImageIndex], 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void startGame(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.add(new InitGameUI(frame));
        frame.revalidate();
        frame.repaint();
    }
}
