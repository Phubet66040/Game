import java.awt.*;
import javax.swing.*;

class SettingsPanel extends JPanel {
    private Homepage homepage;
    private JButton muteButton;
    private JComboBox<String> sizeDropdown;
    private JComboBox<String> difficultyDropdown;
    private boolean isMuted = false;
    

    private static final int EASY_SPAWN_CHANCE = 20;    
    private static final int MEDIUM_SPAWN_CHANCE = 35;  
    private static final int HARD_SPAWN_CHANCE = 50;    
    
    private static final int EASY_ATTACK_TIME = 4000;   
    private static final int MEDIUM_ATTACK_TIME = 2500;  
    private static final int HARD_ATTACK_TIME = 1500;    
    
    private static final int EASY_WARNING_TIME = 2000;    
    private static final int MEDIUM_WARNING_TIME = 1500; 
    private static final int HARD_WARNING_TIME = 1000;   

    public SettingsPanel(Homepage homepage, JFrame frame) {
        this.homepage = homepage;
        initUI(frame);
    }

    private void initUI(JFrame frame) {
        setLayout(new GridLayout(4, 1, 10, 10)); 
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      
        muteButton = new JButton("Mute Music");
        muteButton.addActionListener(e -> toggleMusic());

      
        JPanel sizePanel = new JPanel(new BorderLayout(5, 0));
        JLabel sizeLabel = new JLabel("Screen Size: ");
        String[] sizes = {"800x600", "1024x768", "1280x800", "1600x900", "1920x1080"};
        sizeDropdown = new JComboBox<>(sizes);
        sizeDropdown.addActionListener(e -> resizeWindow(frame));
        sizePanel.add(sizeLabel, BorderLayout.WEST);
        sizePanel.add(sizeDropdown, BorderLayout.CENTER);

       
        JPanel difficultyPanel = new JPanel(new BorderLayout(5, 0));
        JLabel difficultyLabel = new JLabel("Difficulty: ");
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyDropdown = new JComboBox<>(difficulties);
        difficultyDropdown.addActionListener(e -> updateDifficulty());
        difficultyPanel.add(difficultyLabel, BorderLayout.WEST);
        difficultyPanel.add(difficultyDropdown, BorderLayout.CENTER);

      
        add(muteButton);
        add(sizePanel);
        add(difficultyPanel);
        
      
        JButton helpButton = new JButton("Game Rules");
        helpButton.addActionListener(e -> showGameRules());
        add(helpButton);
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

    private void updateDifficulty() {
        String difficulty = (String) difficultyDropdown.getSelectedItem();
        GameSettings settings = new GameSettings();
        
        switch (difficulty) {
            case "Easy":
                settings.setSpawnChance(EASY_SPAWN_CHANCE);
                settings.setAttackTime(EASY_ATTACK_TIME);
                settings.setWarningTime(EASY_WARNING_TIME);
                break;
            case "Medium":
                settings.setSpawnChance(MEDIUM_SPAWN_CHANCE);
                settings.setAttackTime(MEDIUM_ATTACK_TIME);
                settings.setWarningTime(MEDIUM_WARNING_TIME);
                break;
            case "Hard":
                settings.setSpawnChance(HARD_SPAWN_CHANCE);
                settings.setAttackTime(HARD_ATTACK_TIME);
                settings.setWarningTime(HARD_WARNING_TIME);
                break;
        }
        
        // homepage.updateGameSettings(settings);
    }

    private void showGameRules() {
        String rules = """
            Game Rules:
            
            1. Monster Movement:
               - Monsters can appear in different locations
               - You'll hear a warning sound before they move
               - The heartbeat gets louder as they get closer
            
            2. Difficulty Levels:
               Easy:
               - 20% monster spawn chance
               - 4 seconds to react
               - 2 seconds warning time
               
               Medium:
               - 35% monster spawn chance
               - 2.5 seconds to react
               - 1.5 seconds warning time
               
               Hard:
               - 50% monster spawn chance
               - 1.5 seconds to react
               - 1 second warning time
            
            3. Controls:
               - Listen for audio cues
               - React quickly when monsters are near
               - Keep the door locked when necessary
            """;
            
        JOptionPane.showMessageDialog(this, 
            rules,
            "Game Rules", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}


class GameSettings {
    private int spawnChance;
    private int attackTime;
    private int warningTime;

    public void setSpawnChance(int chance) {
        this.spawnChance = chance;
    }

    public void setAttackTime(int time) {
        this.attackTime = time;
    }

    public void setWarningTime(int time) {
        this.warningTime = time;
    }

    public int getSpawnChance() {
        return spawnChance;
    }

    public int getAttackTime() {
        return attackTime;
    }

    public int getWarningTime() {
        return warningTime;
    }
}