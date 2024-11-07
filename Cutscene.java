import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class Cutscene extends JPanel {
    private Image[] cutsceneImages;
    private int currentImageIndex = 0;
    private static final int DELAY = 3000; 
    private Timer timer;
    private Rectangle skipArea;
    private boolean isHovering = false;
    private Clip clip;
    
    
    public Cutscene(JFrame frame) {
        setLayout(new BorderLayout());
        playMusic("assets\\sound\\35mm film and sound effect.wav");
        loadImages();
        skipArea = new Rectangle(900, 700, 200, 50);  
        
      
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (skipArea.contains(e.getPoint())) {
                    timer.stop();
                    startGame(frame);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                if (skipArea.contains(e.getPoint())) {
                    isHovering = true;
                    repaint();
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                boolean wasHovering = isHovering;
                isHovering = skipArea.contains(e.getPoint());
                if (wasHovering != isHovering) {
                    repaint();
                }
            }
        });
        
        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex++;
                if (currentImageIndex >= cutsceneImages.length) {
                    timer.stop();
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
            
            Graphics2D g2d = (Graphics2D) g;
          
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
       
            if (isHovering) {
                g2d.setColor(new Color(255, 255, 255, 80));  
            } else {
                g2d.setColor(new Color(255, 255, 0, 100));  
            }
            g2d.fill(skipArea);
            
           
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            FontMetrics metrics = g2d.getFontMetrics();
            String skipText = "Skip";
            int x = skipArea.x + (skipArea.width - metrics.stringWidth(skipText)) / 2;
            int y = skipArea.y + ((skipArea.height - metrics.getHeight()) / 2) + metrics.getAscent();
            g2d.drawString(skipText, x, y);
        }
    }
    public void playMusic(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Music failed: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }


    private void startGame(JFrame frame) {
        stopMusic();
        frame.getContentPane().removeAll();
        frame.add(new InitGameUI(frame));
        frame.revalidate();
        frame.repaint();
    }
}