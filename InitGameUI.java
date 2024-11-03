import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class InitGameUI extends JPanel {
    private JLabel gameMessage;
    private Image background;
    private boolean isDoorLocked = false;
    private boolean isWatchingCamera = false;
    private int power = 100;
    private int hour = 12;
    private Timer powerTimer;
    private Timer gameTimer;
    private Timer monsterTimer;
    private boolean isMonsterNear = false;
    private Rectangle doorArea;
    private Rectangle cameraArea;
    private Rectangle monitorArea;
    private boolean showStatic = false;
    private Random random = new Random();

    public InitGameUI(JFrame frame) {
        initializeGame(frame);
        startGameTimers();
        setupClickHandlers();
    }

    private void initializeGame(JFrame frame) {
        setLayout(null);
        ImageIcon bg1Icon = new ImageIcon("bgcctv.jpg");
        background = bg1Icon.getImage();


        doorArea = new Rectangle(50, 80, 170, 500);
        cameraArea = new Rectangle(700, 100, 200, 200);
        monitorArea = new Rectangle(600, 50, 300, 250);

      
        gameMessage = new JLabel("Survive the night!", SwingConstants.CENTER);
        gameMessage.setFont(new Font("VT323", Font.BOLD, 36));
        gameMessage.setForeground(Color.WHITE);
        gameMessage.setBounds(200, 20, 400, 50);
        add(gameMessage);

        setBackground(Color.BLACK);
    }

    private void setupClickHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (power <= 0) return;
                
                if (doorArea.contains(e.getPoint())) {
                    toggleDoor();
                } else if (cameraArea.contains(e.getPoint())) {
                    toggleCamera();
                }
            }
        });
    }

    private void startGameTimers() {
        
        powerTimer = new Timer(1000, e -> {
            int drain = 1;
            if (isDoorLocked) drain += 2;
            if (isWatchingCamera) drain += 1;
            power = Math.max(0, power - drain);
            
            if (power <= 0) {
                gameOver("Power ran out!");
            }
            repaint();
        });
        powerTimer.start();

        
        gameTimer = new Timer(20000, e -> { 
            hour++;
            if (hour >= 6) {
                victory();
            }
            repaint();
        });
        gameTimer.start();

       
        monsterTimer = new Timer(5000, e -> {
            if (!isDoorLocked && random.nextInt(100) < 30) {
                isMonsterNear = true;
                if (!isDoorLocked) {
                    gameOver("The monster got in!");
                }
            } else {
                isMonsterNear = false;
            }
            repaint();
        });
        monsterTimer.start();
    }

    private void toggleDoor() {
        isDoorLocked = !isDoorLocked;
        playSound("door_" + (isDoorLocked ? "close" : "open") + ".wav");
        power -= 5;
        repaint();
    }

    private void toggleCamera() {
        isWatchingCamera = !isWatchingCamera;
        showStatic = true;
        Timer staticTimer = new Timer(500, e -> {
            showStatic = false;
            repaint();
            ((Timer)e.getSource()).stop();
        });
        staticTimer.start();
        power -= 2;
        repaint();
    }

    private void gameOver(String reason) {
        stopTimers();
        gameMessage.setText("Game Over: " + reason);
        JOptionPane.showMessageDialog(this, "Game Over: " + reason);
        System.exit(0);
    }

    private void victory() {
        stopTimers();
        gameMessage.setText("You survived the night!");
        JOptionPane.showMessageDialog(this, "Congratulations! You survived until 6 AM!");
        System.exit(0);
    }

    private void stopTimers() {
        powerTimer.stop();
        gameTimer.stop();
        monsterTimer.stop();
    }

    private void playSound(String soundFile) {
        
        System.out.println("Playing sound: " + soundFile);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

       
        g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);

       
        if (!isWatchingCamera) {
            drawOfficeView(g2d);
        } else {
            drawCameraView(g2d);
        }

       
        drawHUD(g2d);

        
        if (showStatic) {
            drawStaticEffect(g2d);
        }
    }

    private void drawOfficeView(Graphics2D g2d) {
       
        g2d.setColor(isDoorLocked ? new Color(0, 255, 0, 100) : new Color(255, 0, 0, 100));
        g2d.fill(doorArea);
        
       
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("VT323", Font.BOLD, 24));
        g2d.drawString(isDoorLocked ? "LOCKED" : "UNLOCKED", 80, 80);

        
        if (isMonsterNear && !isDoorLocked) {
            g2d.setColor(Color.RED);
            g2d.fillOval(30, 300, 50, 50);
        }
    }

    private void drawCameraView(Graphics2D g2d) {
       
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRect(0, 0, getWidth(), getHeight());

       
        g2d.setColor(Color.GREEN);
        g2d.setFont(new Font("VT323", Font.PLAIN, 16));
        for (int i = 0; i < 4; i++) {
            g2d.drawRect(100 + i*200, 100, 180, 120);
            g2d.drawString("CAM " + (i+1), 180 + i*200, 160);
        }

        
        drawStaticEffect(g2d);
    }

    private void drawHUD(Graphics2D g2d) {
       
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("VT323", Font.BOLD, 24));
        g2d.drawString("Power: " + power + "%", 20, 30);
        
      
        g2d.setColor(power < 20 ? Color.RED : Color.GREEN);
        g2d.fillRect(20, 40, power * 2, 20);
        
       
        g2d.setColor(Color.WHITE);
        g2d.drawString(hour + " AM", getWidth() - 100, 30);
    }

    private void drawStaticEffect(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        for (int i = 0; i < 1000; i++) {
            int x = random.nextInt(getWidth());
            int y = random.nextInt(getHeight());
            g2d.setColor(random.nextBoolean() ? Color.WHITE : Color.GRAY);
            g2d.fillRect(x, y, 2, 2);
        }
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }


}