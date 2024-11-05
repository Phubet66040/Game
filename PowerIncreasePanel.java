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

    public PowerIncreasePanel(JFrame frame, int currentPower, int currentResources, int agi, InitGameUI gameUI) {
        this.frame = frame;
        this.power = currentPower;
        this.resources = currentResources;
        this.gameUI = gameUI;
        this.agi = agi;
        this.isPowerIncreasing = false;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        ImageIcon bIcon = new ImageIcon("assets\\background\\Untitled (6).jpg");
        backgImage = bIcon.getImage();
        setOpaque(false);
        
        JLabel titleLabel = new JLabel("Increase Power", SwingConstants.CENTER);
        titleLabel.setFont(new Font("VT323", Font.BOLD, (int)(30 * ((double)getHeight() / 600.0)))); // ปรับขนาดฟอนต์
        add(titleLabel, BorderLayout.NORTH);
    
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false); 
    
        powerLabel = new JLabel("Current Power: " + power + "%", SwingConstants.CENTER);
        powerLabel.setFont(new Font("VT323", Font.PLAIN, (int)(24 * ((double)getHeight() / 600.0)))); 
        infoPanel.add(powerLabel);
    
        resourcesLabel = new JLabel("Resources: " + resources, SwingConstants.CENTER);
        resourcesLabel.setFont(new Font("VT323", Font.PLAIN, (int)(24 * ((double)getHeight() / 600.0)))); 
        infoPanel.add(resourcesLabel);
    
        add(infoPanel, BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setOpaque(false); 

        JButton increasePowerButton = new JButton("Increase Power by 5% (Cost: 1 Resource)");
        increasePowerButton.setFont(new Font("VT323", Font.PLAIN, (int)(20 * ((double)getHeight() / 600.0)))); 
        increasePowerButton.addActionListener(e -> increasePower());
        buttonPanel.add(increasePowerButton);
    
        JButton increaseResourcesButton = new JButton("Increase Resource by 1% (Cost: 5 Power)");
        increaseResourcesButton.setFont(new Font("VT323", Font.PLAIN, (int)(20 * ((double)getHeight() / 600.0)))); 
        increaseResourcesButton.addActionListener(e -> increasere());
        buttonPanel.add(increaseResourcesButton);
    
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("VT323", Font.PLAIN, (int)(20 * ((double)getHeight() / 600.0)))); 
        backButton.addActionListener(e -> goBack());
        buttonPanel.add(backButton);
    
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void increasere() {
        if (power > 4) {
            agi++;
            count++;
            power -= count;
            power = Math.min(100, power); 
            powerLabel.setText("Current Power: " + power + "%");
            resourcesLabel.setText("Resources: " + resources);
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
            new Timer(1000, e -> isPowerIncreasing = false).start();
        } else if (resources <= 0) {
            JOptionPane.showMessageDialog(frame, "Not enough resources to increase power!");
        }
    }

    private void goBack() {
        frame.getContentPane().removeAll();
        gameUI.setPower(power, resources, agi); 
        frame.add(gameUI);
        frame.revalidate();
        frame.repaint();
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
