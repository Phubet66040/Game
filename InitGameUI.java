import java.awt.*;
import javax.swing.*;

public class InitGameUI extends JPanel {
    private JLabel gameMessage;
    private JButton checkCameraButton;
    private JButton closeDoorButton;

    public InitGameUI(JFrame frame) {
        initUI(frame);
    }

    private void initUI(JFrame frame) {
        setLayout(new GridLayout(3, 1));

        gameMessage = new JLabel("Survive the night!", SwingConstants.CENTER);
        gameMessage.setFont(new Font("Serif", Font.BOLD, 24));
        gameMessage.setForeground(Color.WHITE);

        checkCameraButton = new JButton("Check Camera");
        checkCameraButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "You checked the camera!"));

        closeDoorButton = new JButton("Close Door");
        closeDoorButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Door closed!"));

        add(gameMessage);
        add(checkCameraButton);
        add(closeDoorButton);
        setBackground(Color.BLACK);
    }

    public void updateGameMessage(String message) {
        gameMessage.setText(message);
    }
}
