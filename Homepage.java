import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class Homepage extends JPanel {
    private JButton startButton, settingButton, exitButton;
    private Image backgroundImage;
    private Clip clip;
    private JFrame frame;
    private static final String TITLE = "The Last Refuge";

    public Homepage(JFrame frame) {
        this.frame = frame;
        initUI();
        playMusic("assets\\sound\\FREEG.wav");
    }

    private void initUI() {
        try {
            ImageIcon bgIcon = new ImageIcon("assets\\git\\bgghome.gif");
            backgroundImage = bgIcon.getImage();
        } catch (Exception e) {
            System.out.println("Background image loading failed");
        }

        startButton = createCustomButton("Start Night Shift");
        settingButton = createCustomButton("Settings");
        exitButton = createCustomButton("Exit");

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0);

        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(new Font("Creepster", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, gbc);

        add(startButton, gbc);
        add(settingButton, gbc);
        add(exitButton, gbc);
    }

    private JButton createCustomButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0, 0, 0, 0)); 
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 20)); 
        button.setFocusPainted(false); 
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        button.setPreferredSize(new Dimension(200, 40)); 

        button.addMouseListener(new ButtonHoverEffect());
        button.addActionListener(e -> {
            if (text.equals("Start Night Shift")) {
                startGame();
            } else if (text.equals("Settings")) {
                showSettings();
            } else {
                exitGame();
            }
        });

        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(20, 20, 20),
                0, getHeight(), new Color(60, 60, 60)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private class ButtonHoverEffect extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            button.setForeground(Color.RED);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            button.setForeground(Color.WHITE);
        }
    }

    private void startGame() {
        stopMusic(); 
        frame.getContentPane().removeAll();
        frame.add(new InitGameUI(frame));
        frame.revalidate();
        frame.repaint();
    }
    

    private void showSettings() {
        SettingsPanel settingsPanel = new SettingsPanel(this, frame);
        JDialog dialog = new JDialog(frame, "Settings", true);
        dialog.add(settingsPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void exitGame() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?",
            "Exit Game",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void playMusic(String filePath) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.out.println("Music playback failed: " + e.getMessage());
        }
    }

    public void stopMusic() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void resumeMusic() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Last Refuge");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.add(new Homepage(frame));
        frame.setSize(1280, 800); 
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }
}
