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
    private static final String TITLE = "Night Security Guard";

    public Homepage(JFrame frame) {
        this.frame = frame;
        initUI();
        playMusic("background_music.wav");
    }

    private void initUI() {
        // Set animated background
        try {
            ImageIcon bgIcon = new ImageIcon("resources/background_animated.gif");
            backgroundImage = bgIcon.getImage();
        } catch (Exception e) {
            System.out.println("Background image loading failed");
        }

        // Create custom styled buttons
        startButton = createCustomButton("Start Night Shift", "resources/start_button.png");
        settingButton = createCustomButton("Settings", "resources/settings_button.png");
        exitButton = createCustomButton("Exit", "resources/exit_button.png");

        // Layout setup
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(20, 0, 20, 0);
        
        // Add title
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(new Font("Creepster", Font.BOLD, 48));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, gbc);

        // Add buttons
        add(startButton, gbc);
        add(settingButton, gbc);
        add(exitButton, gbc);
    }

    private JButton createCustomButton(String text, String iconPath) {
        JButton button = new JButton(text);
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            button.setIcon(icon);
        } catch (Exception e) {
            // Fallback style if image not found
            button.setBackground(new Color(40, 40, 40));
            button.setForeground(Color.WHITE);
            button.setFont(new Font("Arial", Font.BOLD, 20));
        }
        
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addMouseListener(new ButtonHoverEffect());
        button.setPreferredSize(new Dimension(250, 60));
        
        // Add action listeners
        if (text.startsWith("Start")) {
            button.addActionListener(e -> startGame());
        } else if (text.startsWith("Settings")) {
            button.addActionListener(e -> showSettings());
        } else {
            button.addActionListener(e -> exitGame());
        }
        
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Fallback gradient background
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gp = new GradientPaint(
                0, 0, new Color(20, 20, 20),
                0, getHeight(), new Color(60, 60, 60)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Button hover effect inner class
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

    // Game control methods
    private void startGame() {
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

    // Music control methods
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
}