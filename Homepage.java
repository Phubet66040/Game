import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Homepage extends JPanel {
    JButton startbutton;
    JButton settingbutton = new JButton("Setting");
    JButton exitbutton = new JButton("Exit");
    private Image backgroundImage;

    Homepage() {
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
        JOptionPane.showMessageDialog(this, "Setting Options");
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
