import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Hallroom extends JPanel {
    private Image bgImag;
    private JFrame frame;
    private Rectangle backButtonArea;
    private Rectangle miniButtonArea;
    private InitGameUI fpage;
    private boolean hoverbackbut;
    private boolean hoverminibut;
    private minigame mng;
    private int score = 0;

    public Hallroom(JFrame frame, InitGameUI initGameUI) {
        this.frame = frame;
        this.fpage = initGameUI;
        this.mng = new minigame(frame, this); // สร้าง minigame ที่นี่

        UI();

        backButtonArea = new Rectangle(1200, 100, 350, 500);
        miniButtonArea = new Rectangle(700, 300, 230, 200);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (backButtonArea.contains(e.getPoint())) {
                    goback();
                }
                if (miniButtonArea.contains(e.getPoint())) {
                    gominigame();
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                hoverbackbut = backButtonArea.contains(e.getPoint());
                repaint();
                hoverminibut = miniButtonArea.contains(e.getPoint());
                repaint();
            }
        });
    }

    private void UI() {
        setLayout(new BorderLayout(10, 10));

        ImageIcon bgIcon = new ImageIcon("assets\\background\\Untitled.jpg");
        bgImag = bgIcon.getImage();
        setOpaque(false);

        JLabel titleLabel = new JLabel("Hall room", SwingConstants.CENTER);
        titleLabel.setFont(new Font("VT323", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);
    }

    private void goback() {
        frame.getContentPane().removeAll();
        fpage.setscore(score);
        frame.add(fpage);
        frame.revalidate();
        frame.repaint();
    }
    public void setscore(int score){
        this.score += score;
    }

    private void gominigame() {
        frame.getContentPane().removeAll();
        frame.add(mng); 
        frame.revalidate();
        frame.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImag, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(hoverbackbut ? new Color(100, 50, 0, 100) : new Color(200, 100, 0, 100));
        g2d.fill(backButtonArea);
        g2d.setColor(hoverminibut ? new Color(100, 50, 0, 100) : new Color(200, 100, 0, 100));
        g2d.fill(miniButtonArea);
    }
}
