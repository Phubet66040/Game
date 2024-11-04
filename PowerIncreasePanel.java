import java.awt.*;
import javax.swing.*;

public class PowerIncreasePanel extends JPanel {
    private JFrame frame;
    private int power;
    private int resources; 
    private JLabel powerLabel;
    private JLabel resourcesLabel;
    private InitGameUI gameUI;
    private Runnable onReturnToGame;
    private boolean isPowerIncreasing;

    public PowerIncreasePanel(JFrame frame, int currentPower, int currentResources, InitGameUI gameUI) {
        this.frame = frame;
        this.power = currentPower;
        this.resources = currentResources;
        this.gameUI = gameUI;
        this.isPowerIncreasing = false;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Increase Power", SwingConstants.CENTER);
        titleLabel.setFont(new Font("VT323", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

      
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        powerLabel = new JLabel("Current Power: " + power + "%", SwingConstants.CENTER);
        powerLabel.setFont(new Font("VT323", Font.PLAIN, 24));
        powerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(powerLabel);

        resourcesLabel = new JLabel("Resources: " + resources, SwingConstants.CENTER);
        resourcesLabel.setFont(new Font("VT323", Font.PLAIN, 24));
        resourcesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(resourcesLabel);

        add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton increasePowerButton = new JButton("Increase Power by 5% (Cost: 1 Resource)");
        increasePowerButton.setFont(new Font("VT323", Font.PLAIN, 20));
        increasePowerButton.addActionListener(e -> increasePower());
        buttonPanel.add(increasePowerButton);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("VT323", Font.PLAIN, 20));
        backButton.addActionListener(e -> goBack());
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);
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
        gameUI.setPower(power, resources); 
        frame.add(gameUI);
        frame.revalidate();
        frame.repaint();
    }

    public void setOnReturnToGame(Runnable callback) {
        this.onReturnToGame = callback;
    }
}