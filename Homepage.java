import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Homepage extends JPanel {
    JButton startbutton;
    JButton settingbutton = new JButton("Setting");
    JButton exitbutton = new JButton("Exit");
    private Image backgroundImage;

    Homepage() {
    
        ImageIcon startIcon = new ImageIcon("Start button.png");
        ImageIcon bghomeIcon = new ImageIcon("bgghome.gif");
        backgroundImage = bghomeIcon.getImage();
        

        startbutton = new JButton(startIcon);
        startbutton.setBorderPainted(false);
        startbutton.setContentAreaFilled(false); 

       
        startbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNextScreen();
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
        
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }

    private void showNextScreen() {
        JOptionPane.showMessageDialog(this, "Moving  screen...");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Last Refuge");
        Homepage home = new Homepage();
        frame.add(home);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
