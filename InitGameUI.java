import java.awt.*;
import javax.swing.*;

public class InitGameUI extends JPanel {
    private JLabel gameMessage;
    private Image background;
    private JButton checkCameraButton;
    private JButton closeDoorButton;

    public InitGameUI(JFrame frame) {
        initUI(frame);
    }

    private void initUI(JFrame frame) {
        ImageIcon bg1Icon = new ImageIcon("gamecctv.jpg"); 
        background = bg1Icon.getImage();

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
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }


    public void updateGameMessage(String message) {
        gameMessage.setText(message);
    }
}
