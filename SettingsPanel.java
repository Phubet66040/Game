import java.awt.*;
import javax.swing.*;

class SettingsPanel extends JPanel {
    private Homepage homepage;
    private JButton muteButton;
    private JComboBox<String> sizeDropdown;
    private boolean isMuted = false;

    public SettingsPanel(Homepage homepage, JFrame frame) {
        this.homepage = homepage;
        initUI(frame);
    }

    private void initUI(JFrame frame) {
        muteButton = new JButton("Mute Music");
        muteButton.addActionListener(e -> toggleMusic());

        String[] sizes = {"800x600", "1024x768", "1280x800", "1600x900", "1920x1080"};
        sizeDropdown = new JComboBox<>(sizes);
        sizeDropdown.addActionListener(e -> resizeWindow(frame));

        setLayout(new GridLayout(2, 1));
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
