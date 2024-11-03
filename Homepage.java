import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class Homepage extends JPanel {
    JButton startbutton;
    JButton settingbutton = new JButton("Setting");
    JButton exitbutton = new JButton("Exit");
    private Image backgroundImage;
    private Clip clip;
    private JFrame frame; 

    Homepage(JFrame frame) {
        this.frame = frame; 
        ImageIcon startIcon = new ImageIcon("Start button1.png");
        ImageIcon bghomeIcon = new ImageIcon("bgghome.gif");
        backgroundImage = bghomeIcon.getImage();

        startbutton = new JButton(startIcon);
        startbutton.setBorderPainted(false);
        startbutton.setContentAreaFilled(false);
        startbutton.setFocusPainted(false);
        startbutton.setMargin(null);

        startbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextScreen();
            }
        });

        settingbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSetting();
            }
        });

        exitbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Window window = SwingUtilities.getWindowAncestor(Homepage.this);
                if (window != null) {
                    window.dispose();
                }
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints gid = new GridBagConstraints();
        gid.gridx = 0;
        gid.gridy = GridBagConstraints.RELATIVE;
        gid.insets = new Insets(10, 0, 10, 0);
        gid.anchor = GridBagConstraints.CENTER;
        gid.fill = GridBagConstraints.HORIZONTAL;

        add(startbutton, gid);
        add(settingbutton, gid);
        add(exitbutton, gid);
        playMusic("FREEG.wav");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void showNextScreen() {
        JOptionPane.showMessageDialog(this, "Moving to the next screen...");
    }

    private void showSetting() {
        SettingsPanel settingsPanel = new SettingsPanel(this, frame);
        JOptionPane.showMessageDialog(this, settingsPanel, "Settings", JOptionPane.PLAIN_MESSAGE);
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
        JFrame frame = new JFrame("The Last Refuge");
        Homepage home = new Homepage(frame);
        frame.add(home);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true); 
        frame.setVisible(true);
    }
}

class SettingsPanel extends JPanel {
    private Homepage homepage;
    private JButton muteButton;
    private JComboBox<String> sizeDropdown;
    private boolean isMuted = false;

    public SettingsPanel(Homepage homepage, JFrame frame) {
        this.homepage = homepage;

        muteButton = new JButton("Mute Music");
        muteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleMusic();
            }
        });


        String[] sizes = {"800x600", "1024x768", "1280x800", "1600x900", "1920x1080"};
        sizeDropdown = new JComboBox<>(sizes);
        sizeDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resizeWindow(frame);
            }
        });

        setLayout(new GridLayout(3, 1));
        add(muteButton);
        add(sizeDropdown);
    }

    private void toggleMusic() {
        if (isMuted) {
            homepage.resumeMusic();
            muteButton.setText("Mute Music");
        } else {
            homepage.stopMusic();
            muteButton.setText("Unmute Music");
        }
        isMuted = !isMuted;
    }

    private void resizeWindow(JFrame frame) {
        String selectedSize = (String) sizeDropdown.getSelectedItem();
        if (selectedSize != null) {
            String[] dimensions = selectedSize.split("x");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
        }
    }
}
