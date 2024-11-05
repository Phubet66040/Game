import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class PowerIncreasePanel extends JPanel {
    private JFrame frame;
    private int power;
    private int agi;
    private int count = 30;
    private int resources; 
    private JLabel powerLabel;
    private JLabel resourcesLabel;
    private InitGameUI gameUI;
    private Runnable onReturnToGame;
    private boolean isPowerIncreasing;
    private Image backgImage;
    private Timer powerDecreaseTimer; 

   
    private Rectangle increasePowerArea;
    private Rectangle increaseResourceArea;
    private Rectangle backButtonArea;
    
   
    private boolean hoverIncreasePower;
    private boolean hoverIncreaseResource;
    private boolean hoverBackButton;

    public PowerIncreasePanel(JFrame frame, int currentPower, int currentResources, int agi, InitGameUI gameUI) {
        this.frame = frame;
        this.power = currentPower;
        this.resources = currentResources;
        this.gameUI = gameUI;
        this.agi = agi;
        this.isPowerIncreasing = false;
        initUI();
        startPowerDecreaseTimer(); 

       
        increasePowerArea = new Rectangle(50, 200, 300, 50); 
        increaseResourceArea = new Rectangle(50, 300, 300, 50); 
        backButtonArea = new Rectangle(50, 400, 100, 50); 

     
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (increasePowerArea.contains(e.getPoint())) {
                    increasePower();
                } else if (increaseResourceArea.contains(e.getPoint())) {
                    increaseResource();
                } else if (backButtonArea.contains(e.getPoint())) {
                    goBack();
                }
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
               
                hoverIncreasePower = increasePowerArea.contains(e.getPoint());
                hoverIncreaseResource = increaseResourceArea.contains(e.getPoint());
                hoverBackButton = backButtonArea.contains(e.getPoint());
                repaint(); 
            }
        });
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        
        ImageIcon bIcon = new ImageIcon("assets\\background\\restory.jpg");
        backgImage = bIcon.getImage();
        setOpaque(false);

       
        JLabel titleLabel = new JLabel("Power Supplies Room", SwingConstants.CENTER);
        titleLabel.setFont(new Font("VT323", Font.BOLD, 30));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

      
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        powerLabel = new JLabel("Current Power: " + power + "%", SwingConstants.CENTER);
        powerLabel.setFont(new Font("VT323", Font.PLAIN, 24));
        powerLabel.setForeground(Color.WHITE);
        infoPanel.add(powerLabel);

        resourcesLabel = new JLabel("Resources: " + resources, SwingConstants.CENTER);
        resourcesLabel.setFont(new Font("VT323", Font.PLAIN, 24));
        resourcesLabel.setForeground(Color.WHITE);
        infoPanel.add(resourcesLabel);

        add(infoPanel, BorderLayout.CENTER);
    }

    private void startPowerDecreaseTimer() {
        powerDecreaseTimer = new Timer(1000, e -> {
            if (power > 0) {
                power--;
                powerLabel.setText("Current Power: " + power + "%");
                updatePowerLabelColor();
            } else {
                powerDecreaseTimer.stop();
                JOptionPane.showMessageDialog(frame, "Your power has been depleted!");
            }
        });
        powerDecreaseTimer.start();
    }

    private void increasePower() {
        if (power < 100 && resources > 0 && !isPowerIncreasing) {
            isPowerIncreasing = true;
            power += 5; 
            power = Math.min(100, power);
            resources -= 1; 
            powerLabel.setText("Current Power: " + power + "%");
            resourcesLabel.setText("Resources: " + resources);
            updatePowerLabelColor();

           
            new Timer(1000, e -> isPowerIncreasing = false).start();
        } else if (resources <= 0) {
            JOptionPane.showMessageDialog(frame, "Not enough resources to increase power!");
        }
    }

    private void increaseResource() {
        if (resources < 100 && power > 4) {
            agi++;
            count++;
            power -= count; 
            power = Math.max(0, power); 

            powerLabel.setText("Current Power: " + power + "%");
            resourcesLabel.setText("Resources: " + resources);
            updatePowerLabelColor();
        } else if (power <= 4) {
            JOptionPane.showMessageDialog(frame, "Not enough power to increase Resource!");
        }
    }

    private void goBack() {
        powerDecreaseTimer.stop();
        frame.getContentPane().removeAll();
        gameUI.setPower(power, resources, agi);
        frame.add(gameUI);
        frame.revalidate();
        frame.repaint();
    }

    private void updatePowerLabelColor() {
        powerLabel.setForeground(power <= 20 ? Color.RED : Color.WHITE);
    }

    public void setOnReturnToGame(Runnable callback) {
        this.onReturnToGame = callback;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgImage != null) {
            g.drawImage(backgImage, 0, 0, getWidth(), getHeight(), this); 
        }

        Graphics2D g2d = (Graphics2D) g;

      
        g2d.setColor(hoverIncreasePower ? new Color(0, 200, 0, 150) : (isPowerIncreasing ? new Color(0, 255, 0, 100) : new Color(255, 0, 0, 100)));
        g2d.fill(increasePowerArea);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("VT323", Font.BOLD, 24));
        g2d.drawString("Increase Power by 5% (Cost: 1 Resource)", increasePowerArea.x + 10, increasePowerArea.y + 30);
        
        g2d.setColor(hoverIncreaseResource ? new Color(0, 200, 0, 150) : (resources > 0 ? new Color(0, 255, 0, 100) : new Color(255, 0, 0, 100)));
        g2d.fill(increaseResourceArea);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Increase Resource by 1 (Cost: 5 Power)", increaseResourceArea.x + 10, increaseResourceArea.y + 30);
        
      
        g2d.setColor(hoverBackButton ? new Color(200, 150, 0, 150) : new Color(200, 100, 0, 100));
        g2d.fill(backButtonArea);
        g2d.setColor(Color.WHITE);
        g2d.drawString("Back", backButtonArea.x + 10, backButtonArea.y + 30);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600); 
    }
}
