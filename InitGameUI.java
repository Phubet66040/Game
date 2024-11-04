
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;


public class InitGameUI extends JPanel {

    private final JLabel gameMessage;
    private final Image background;
    private final Image jumpscareImage;
    private final Image cctvImage;
    private final Image monsterImg;
    private volatile boolean isDoorLocked = false;
    private volatile boolean isWatchingCamera = false;
    private volatile boolean isMonitorActive = false;
    private volatile int power = 100;
    private volatile int resources = 5;
    private volatile int hour = 0;
    private Timer powerTimer;
    private Timer gameTimer;
    private Timer monsterTimer;
    private volatile boolean isMonsterNear = false;
    private final Rectangle doorArea;
    private final Rectangle cameraArea;
    private final Rectangle monitorArea;
    private volatile boolean showStatic = false;
    private volatile boolean showJumpscare = false;
    private final Random random = new Random();
    private final Map<Integer, AtomicBoolean> monsterLocations;
    private volatile int currentMonsterCam = -1;
    private final List<Image> randomImages = new ArrayList<>();
    private JFrame frame;
    private JButton powerIncreaseButton; // Button to increase power


    public InitGameUI(JFrame frame) {
        this.frame = frame;
        setLayout(null);
        initializeUIComponents();

        //img
        ImageIcon bg1Icon = new ImageIcon("assets/background/bgcctv.jpg");
        background = bg1Icon.getImage();
        ImageIcon jumpscareIcon = new ImageIcon("assets\\git\\jumps.gif");
        jumpscareImage = jumpscareIcon.getImage();
        ImageIcon cctvIcon = new ImageIcon("assets\\git\\download.gif");
        cctvImage = cctvIcon.getImage();
        ImageIcon monIcon = new ImageIcon("assets\\git\\evil-scary.gif");
        monsterImg = monIcon.getImage();
        randomImages.add(new ImageIcon("assets\\git\\evil-scary.gif").getImage());
        randomImages.add(new ImageIcon("assets\\git\\Private Website.gif").getImage());
        randomImages.add(new ImageIcon("assets\\git\\[Creepy GIF] When a Shotgun Isn't Enough.gif").getImage());
        randomImages.add(new ImageIcon("assets\\git\\d794cc8f-349c-452d-b596-2bead5189d94.gif").getImage());

        //area
        doorArea = new Rectangle(50, 175, 195, 450);
        cameraArea = new Rectangle(800, 190, 3200, 250);
        monitorArea = new Rectangle(800, 190, 320, 250);

        gameMessage = new JLabel("Survive the night!",SwingConstants.CENTER);
        gameMessage.setFont(new Font("VT323", Font.BOLD, 36));
        gameMessage.setForeground(Color.WHITE);
        gameMessage.setBounds(400, 20, 400, 50);
        add(gameMessage);

        monsterLocations = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            monsterLocations.put(i, new AtomicBoolean(false));
        }

        setBackground(Color.BLACK);

        SwingUtilities.invokeLater(this::initializeGame);
    }

    private void initializeGame() {
        startGameTimers();
        setupClickHandlers();
    }

    private void setupClickHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (power <= 0) {
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    if (doorArea.contains(e.getPoint())) {
                        toggleDoor();
                    } else if (cameraArea.contains(e.getPoint())) {
                        toggleCamera();
                    } else if (monitorArea.contains(e.getPoint())) {
                        toggleMonitor();
                    } else {
                        for (int i = 0; i < 4; i++) {
                            Rectangle camArea = new Rectangle(100 + i * 200, 100, 180, 120);
                            if (isWatchingCamera && camArea.contains(e.getPoint())) {
                                checkCamera(i);
                                break;
                            }
                        }
                    }
                });
            }
        });
    }

    private void toggleMonitor() {
        SwingUtilities.invokeLater(() -> {
            isMonitorActive = !isMonitorActive;
            playSound("assets/sound/TV_" + (isMonitorActive ? "On" : "On") + ".wav");
            power -=1;
            repaint();
        });
    }

    private void startGameTimers() {
        powerTimer = new Timer(5000, e -> {
            resources++;
            power -=3;
            SwingUtilities.invokeLater(() -> {
                int drain = 0;
                if (isDoorLocked) {
                    drain += 2;
                }
                if (isWatchingCamera) {
                    drain += 1;
                }
                if (isMonitorActive) {
                    drain += 1;
                }
                power = Math.max(0, power - drain);

                if (power <= 0) {
                    gameOver("Power ran out!");
                }
                repaint();
            });
        });

        gameTimer = new Timer(20000, e -> {
            SwingUtilities.invokeLater(() -> {
                hour++;
                if (hour >= 6) {
                    victory();
                }
                repaint();
            });
        });

        monsterTimer = new Timer(5000, e -> {
            SwingUtilities.invokeLater(this::updateMonsterPositions);
        });

        powerTimer.start();
        gameTimer.start();
        monsterTimer.start();
    }

    private void checkCamera(int camNumber) {
        SwingUtilities.invokeLater(() -> {
            power -= 1;
            showStatic = true;
            currentMonsterCam = camNumber;

            Timer staticTimer = new Timer(500, evt -> {
                SwingUtilities.invokeLater(() -> {
                    showStatic = false;
                    repaint();
                    ((Timer) evt.getSource()).stop();
                });
            });
            staticTimer.setRepeats(false);
            staticTimer.start();
            repaint();
        });
    }

    private void toggleDoor() {
        SwingUtilities.invokeLater(() -> {
            isDoorLocked = !isDoorLocked;
            playSound("assets/sound/door_" + (isDoorLocked ? "open" : "close") + ".wav");
            power -= 5;
            repaint();
        });
    }

    private void toggleCamera() {
        SwingUtilities.invokeLater(() -> {
            isWatchingCamera = !isWatchingCamera;
            showStatic = true;

            Timer staticTimer = new Timer(500, e -> {
                SwingUtilities.invokeLater(() -> {
                    showStatic = false;
                    repaint();
                    ((Timer) e.getSource()).stop();
                });
            });
            staticTimer.setRepeats(false);
            staticTimer.start();

            power -= 2;
            repaint();
        });
    }

    private void updateMonsterPositions() {
        monsterLocations.forEach((key, value) -> value.set(false));

        if (random.nextInt(100) < 50) {
            int newLocation = random.nextInt(4);
            monsterLocations.get(newLocation).set(true);

            if (newLocation == 0) {
                isMonsterNear = true;
                if (!isDoorLocked) {
                    Timer attackTimer = new Timer(1000, e -> {
                        SwingUtilities.invokeLater(() -> {
                            if (!isDoorLocked) {
                                gameOver("The monster got in!");
                            }
                            ((Timer) e.getSource()).stop();
                        });
                    });
                    attackTimer.setRepeats(false);
                    attackTimer.start();
                }
            } else {
                isMonsterNear = false;
            }
        }
        repaint();
    }

    private void gameOver(String reason) {
        stopTimers();
        showJumpscare = true;
        playSound("assets\\sound\\Jumpsc.wav");

        Timer jumpscareTimer = new Timer(3000, e -> {
            showJumpscare = false;
            repaint();
            ((Timer) e.getSource()).stop();

            gameMessage.setText("Game Over: " + reason);
            JOptionPane.showMessageDialog(this, "Game Over: " + reason);
            System.exit(0);
        });
        jumpscareTimer.setRepeats(false);
        jumpscareTimer.start();

        repaint();
    }

    private void victory() {
        stopTimers();
        gameMessage.setText("You survived the night!");
        JOptionPane.showMessageDialog(this, "You survived the night!");
        System.exit(0);
    }

    private void stopTimers() {
        if (powerTimer != null) {
            powerTimer.stop();
        }
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (monsterTimer != null) {
            monsterTimer.stop();
        }
    }

    private void playSound(String soundFile) {
        try {
            File soundPath = new File(soundFile);
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInput);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGame(g2d);

        if (showJumpscare) {
            g2d.drawImage(jumpscareImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void drawGame(Graphics2D g2d) {
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
        g2d.drawString(isDoorLocked ? " LOCKED" : "UNLOCKED", 80, 200);

        if (isMonsterNear && !isDoorLocked) {
            g2d.setColor(Color.RED);
            g2d.fillOval(30, 300, 50, 50);
        }

        g2d.setColor(isMonitorActive ? new Color(0, 0, 255, 100) : new Color(255, 255, 0, 100));
        g2d.fill(monitorArea);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("VT323", Font.BOLD, 24));
        g2d.drawString("Monitor: " + (isMonitorActive ? "ON" : "OFF"), 800, 150);
    }

    private final Map<Integer, Image> cameraGhostImages = new HashMap<>();

    private void drawCameraView(Graphics2D g2d) {
        for (int i = 0; i < 4; i++) {
            Rectangle camArea = new Rectangle(100 + i * 200, 100, 180, 120);

            g2d.setColor(Color.DARK_GRAY);
            g2d.fill(camArea);

            if (monsterLocations.get(i).get()) {

                cameraGhostImages.putIfAbsent(i, randomImages.get(random.nextInt(randomImages.size())));

                g2d.drawImage(cameraGhostImages.get(i), 100 + i * 200, 100, 180, 120, this);
            } else {

                cameraGhostImages.remove(i);

                g2d.drawImage(cctvImage, 100 + i * 200, 100, 180, 120, this);
            }

            g2d.setColor(Color.WHITE);
            g2d.drawString("CAM " + (i + 1), 100 + i * 200, 80);
        }
    }

    private void drawHUD(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("VT323", Font.BOLD, 20));
        g2d.drawString("Power: " + power + "%", 10, 30);
        g2d.drawString("Hour: " + (12 - hour), 10, 60);
        g2d.drawString("Resources: " + resources, 10, 80);
        int barWidth = 100;
        int barHeight = 20;
        int barX = 137;
        int barY = 14;
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(barX, barY, barWidth, barHeight);

        int filledWidth = (int) (barWidth * (power / 100.0));
        if (power > 60) {
            g2d.setColor(Color.GREEN);
        } else if (power > 30) {
            g2d.setColor(Color.YELLOW);
        } else {
            g2d.setColor(Color.RED);
        }
        g2d.fillRect(barX, barY, filledWidth, barHeight);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(barX, barY, barWidth, barHeight);
    }

    private void drawStaticEffect(Graphics2D g2d) {
        g2d.setColor(new Color(255, 255, 255, 50));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }



    //incres
    private void initializeUIComponents() {
        
        powerIncreaseButton = new JButton("PowerSupplies");
        powerIncreaseButton.setFont(new Font("VT323", Font.PLAIN, 20));
        powerIncreaseButton.setBounds(50, 90, 200, 50);
        powerIncreaseButton.setBackground(Color.BLACK); 
        powerIncreaseButton.setForeground(Color.WHITE);
        powerIncreaseButton.setFocusPainted(false); 
        powerIncreaseButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        powerIncreaseButton.setPreferredSize(new Dimension(200, 40)); 

        powerIncreaseButton.addActionListener(e -> openPowerIncreasePanel());
        add(powerIncreaseButton);
        
        
    }

    

    private void openPowerIncreasePanel() { 
        
        frame.getContentPane().removeAll();
        PowerIncreasePanel powerPanel = new PowerIncreasePanel(frame, power,resources, this); 
        powerPanel.setOnReturnToGame(() -> {
            startGameTimers();
        });
        frame.add(powerPanel);
        frame.revalidate();
        frame.repaint();
    }
    
    

    public void setPower(int updatedPower, int resources) {
        this.power = updatedPower;
        this.resources = resources;  
        repaint(); 
    }

    
}
