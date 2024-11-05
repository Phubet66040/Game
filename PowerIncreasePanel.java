import java.awt.*;
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
    private Timer powerDecreaseTimer; // Timer สำหรับลดพลังงาน

    public PowerIncreasePanel(JFrame frame, int currentPower, int currentResources, int agi, InitGameUI gameUI) {
        this.frame = frame;
        this.power = currentPower;
        this.resources = currentResources;
        this.gameUI = gameUI;
        this.agi = agi;
        this.isPowerIncreasing = false;
        initUI();
        startPowerDecreaseTimer(); // เริ่มต้น Timer เพื่อลดพลังงาน
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        ImageIcon bIcon = new ImageIcon("assets\\background\\Untitled (6).jpg");
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
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setOpaque(false); 

        JButton increasePowerButton = new JButton("Increase Power by 5% (Cost: 1 Resource)");
        increasePowerButton.setFont(new Font("VT323", Font.PLAIN, 20));
        increasePowerButton.addActionListener(e -> increasePower());
        buttonPanel.add(increasePowerButton);
    
        JButton increaseResourcesButton = new JButton("Increase Resource by 1% (Cost: 5 Power)");
        increaseResourcesButton.setFont(new Font("VT323", Font.PLAIN, 20));
        increaseResourcesButton.addActionListener(e -> increaseResource());
        buttonPanel.add(increaseResourcesButton);
    
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("VT323", Font.PLAIN, 20));
        backButton.addActionListener(e -> goBack());
        buttonPanel.add(backButton);
    
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void increaseResource() {
        if (power > 4) {
            agi++;
            count++;
            power -= count;
            power = Math.min(100, power); 
            powerLabel.setText("Current Power: " + power + "%");
            resourcesLabel.setText("Resources: " + resources);
            updatePowerLabelColor(); // อัปเดตสีของ powerLabel
        } else if (resources <= 0) {
            JOptionPane.showMessageDialog(frame, "Not enough power to increase Resource!");
        }
    }

    private void increasePower() {
        if (power < 100 && resources > 0 && !isPowerIncreasing) {
            isPowerIncreasing = true;
            power += 10; 
            power = Math.min(100, power); 
            resources -= 5; 
            powerLabel.setText("Current Power: " + power + "%");
            resourcesLabel.setText("Resources: " + resources);
            updatePowerLabelColor(); // อัปเดตสีของ powerLabel
            new Timer(1000, e -> isPowerIncreasing = false).start();
        } else if (resources <= 0) {
            JOptionPane.showMessageDialog(frame, "Not enough resources to increase power!");
        }
    }

    private void startPowerDecreaseTimer() {
        powerDecreaseTimer = new Timer(1000, e -> {
            if (power > 0) {
                power--; 
                powerLabel.setText("Current Power: " + power + "%");
                updatePowerLabelColor(); // อัปเดตสีของ powerLabel
            } else {
                powerDecreaseTimer.stop(); 
        
            }
        });
        powerDecreaseTimer.start(); 
    }

    private void goBack() {
        powerDecreaseTimer.stop(); // หยุด Timer ก่อนกลับ
        frame.getContentPane().removeAll();
        gameUI.setPower(power, resources, agi); 
        frame.add(gameUI);
        frame.revalidate();
        frame.repaint();
    }

    private void updatePowerLabelColor() {
        if (power <= 20) {
            powerLabel.setForeground(Color.RED); // เปลี่ยนเป็นสีแดงเมื่อพลังงาน <= 20%
        } else {
            powerLabel.setForeground(Color.WHITE); // เปลี่ยนกลับเป็นสีขาวเมื่อพลังงานมากกว่า 20%
        }
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
    }
}
