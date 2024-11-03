import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;


public class Homepage extends JPanel {
    private JButton startButton, settingButton, exitButton;
    private Image backgroundImage;
    private Clip clip;
    private JFrame frame;

    public Homepage(JFrame frame) {
        this.frame = frame;
        initUI();
        playMusic("FREEG.wav"); 
    }

    private void initUI() {
        ImageIcon bgIcon = new ImageIcon("bgghome.gif"); 
        backgroundImage = bgIcon.getImage();

        startButton = createImageButton("Start button1.png", e -> startGame()); 
        settingButton = createTextButton("Setting", e -> showSettings());
        exitButton = createTextButton("Exit", e -> exitGame());

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        add(startButton, gbc);
        add(settingButton, gbc);
        add(exitButton, gbc);
    }

    private JButton createImageButton(String iconPath, ActionListener action) {
        ImageIcon icon = new ImageIcon(iconPath);
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setMargin(null);
        button.addActionListener(action);
        return button;
    }

    private JButton createTextButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        button.setFocusPainted(false);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void startGame() {
        frame.getContentPane().removeAll();

    
        InitGameUI initGameUI = new InitGameUI(frame);
        frame.add(initGameUI);

        frame.revalidate();
        frame.repaint();
    }

    private void showSettings() {
        SettingsPanel settingsPanel = new SettingsPanel(this, frame);
        JOptionPane.showMessageDialog(this, settingsPanel, "Settings", JOptionPane.PLAIN_MESSAGE);
    }

    private void exitGame() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window != null) {
            window.dispose();
        }
    }

    private void playMusic(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
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
        JFrame frame = new JFrame("The Last Fugue");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.add(new Homepage(frame));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
