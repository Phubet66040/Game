import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
public class Homepage extends JPanel {
    JButton startbutton = new JButton("Start");
    JButton settingbutton = new JButton("Setting");
    JButton exitbutton = new JButton("Exit");
    Homepage(){
        setLayout(new GridBagLayout());
        GridBagConstraints gid = new GridBagConstraints();
        gid.gridx = 0;
        gid.gridy = GridBagConstraints.RELATIVE;
        gid.insets = new Insets(10, 0, 10, 0);
        gid.anchor = GridBagConstraints.CENTER;
        gid.fill = GridBagConstraints.HORIZONTAL;


        add(startbutton,gid);
        add(settingbutton,gid);
        add(exitbutton,gid);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("The Last Refuge");
        Homepage home = new Homepage();
        frame.add(home);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}