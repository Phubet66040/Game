import java.awt.*;
import javax.swing.*;

public class PowerIncreasePanel extends JPanel {
    private JFrame frame;
    private int power;
    private JLabel powerLabel;
    private InitGameUI gameUI; 
    private Runnable onReturnToGame;

    public PowerIncreasePanel(JFrame frame, int currentPower, InitGameUI gameUI) { 
        this.frame = frame;
        this.power = currentPower;
        this.gameUI = gameUI; 
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Increase Power", SwingConstants.CENTER);
        titleLabel.setFont(new Font("VT323", Font.BOLD, 30));
        add(titleLabel, BorderLayout.NORTH);

        powerLabel = new JLabel("Current Power: " + power + "%", SwingConstants.CENTER);
        powerLabel.setFont(new Font("VT323", Font.PLAIN, 24));
        add(powerLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton increasePowerButton = new JButton("Increase Power by 10%");
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
        if (power < 100) {
            power = Math.min(100, power + 10);
            powerLabel.setText("Current Power: " + power + "%");
        }
    }

    private void goBack() {
        frame.getContentPane().removeAll();
        gameUI.setPower(power);  
        frame.add(gameUI);
        frame.revalidate();
        frame.repaint();
    }

    public void setOnReturnToGame(Runnable callback) {
        this.onReturnToGame = callback;
    }
}