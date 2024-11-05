import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class minigame extends JPanel {
    private static final int BALLOON_RADIUS = 40; 
    private static final int BALL_RADIUS = 15; 
    private static final int GUN_LENGTH = 80; 
    private static final int GAME_WIDTH = 400;
    private static final int GAME_HEIGHT = 800;
    private Hallroom hall;
    private Balloon balloon;
    private ArrayList<Projectile> projectiles = new ArrayList<>();
    private int gunAngle = 90; 
    private int score = 0; 

    public minigame(JFrame frame, Hallroom hall) {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        setLayout(new BorderLayout());

        balloon = new Balloon(); 
        addMouseListener(new GameMouseHandler());
        setFocusable(true);
        requestFocusInWindow();

        Timer timer = new Timer(30, e -> {
            updateProjectiles();
            checkCollisions();
            repaint();
        });
        timer.start();

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            frame.getContentPane().removeAll();
            frame.add(hall);
            hall.setscore(score);
            frame.revalidate();
            frame.repaint();
        });
        add(exitButton, BorderLayout.SOUTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGun(g); 
        balloon.paint(g);
        for (Projectile p : projectiles) {
            p.paint(g); 
        }
        drawScore(g); 
    }

    private void paintGun(Graphics g) {
        int x = (int) (GUN_LENGTH * Math.cos(Math.toRadians(gunAngle)) + GAME_WIDTH / 2); 
        int y = (int) (GAME_HEIGHT - GUN_LENGTH * Math.sin(Math.toRadians(gunAngle))); 
        g.drawLine(GAME_WIDTH / 2, GAME_HEIGHT, x, y);
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20); 
    }

    private class GameMouseHandler extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();

           
            int dx = mouseX - (GAME_WIDTH / 2);
            int dy = GAME_HEIGHT - mouseY;
            gunAngle = (int) Math.toDegrees(Math.atan2(dy, dx)); 

          
            projectiles.add(new Projectile(GAME_WIDTH / 2, GAME_HEIGHT, gunAngle, GUN_LENGTH));
            repaint();
        }
    }

    private void updateProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.move(); 
            if (p.isOutOfBounds(GAME_WIDTH, GAME_HEIGHT)) {
                projectiles.remove(i); 
                i--; 
            }
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            if (balloon.isHit(p.getX(), p.getY(), BALL_RADIUS)) {
                balloon.explode(); 
                projectiles.remove(i);
                score++; 
                i--; 
            }
        }
    }

    private class Balloon {
        private int x; 
        private int y; 
        private boolean isHit = false; 

        public Balloon() {
            resetPosition(); 
        }

        public void paint(Graphics g) {
            if (isHit) {
                paintExplosion(g); 
                resetPosition(); 
                isHit = false;
                return;
            }
            g.drawOval(x - BALLOON_RADIUS, y - BALLOON_RADIUS, 2 * BALLOON_RADIUS, 2 * BALLOON_RADIUS); 
        }

        public void explode() {
            isHit = true; 
        }

        private void paintExplosion(Graphics g) {
            g.drawOval(x - BALLOON_RADIUS - 10, y - BALLOON_RADIUS, BALLOON_RADIUS * 2 + 10, BALLOON_RADIUS * 2 + 10);
            g.drawOval(x + BALLOON_RADIUS, y, BALLOON_RADIUS * 2 + 10, BALLOON_RADIUS * 2 + 10);
        }

        private void resetPosition() {
            x = (int) (Math.random() * GAME_WIDTH); 
            y = (int) (Math.random() * GAME_HEIGHT / 2); 
        }

        public boolean isHit(int x, int y, int radius) {
            return Math.sqrt((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y)) <= BALLOON_RADIUS + radius; 
        }
    }

    private class Projectile {
        private int x; 
        private int y; 
        private int angle; 

        public Projectile(int x, int y, int angle, int length) {
            this.x = x;
            this.y = y;
            this.angle = angle;
        }

        public void move() {
            x += 10 * Math.cos(Math.toRadians(angle)); 
            y -= 10 * Math.sin(Math.toRadians(angle));
        }

        public void paint(Graphics g) {
            g.fillOval((int) (x - BALL_RADIUS), (int) (y - BALL_RADIUS), 2 * BALL_RADIUS, 2 * BALL_RADIUS);
        }

        public boolean isOutOfBounds(int width, int height) {
            return x < 0 || x > width || y < 0 || y > height; 
        }

        public int getX() {
            return (int) x; 
        }

        public int getY() {
            return (int) y; 
        }
    }
}
