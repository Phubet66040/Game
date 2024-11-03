import javax.swing.*;
public class Homepage extends JPanel {
    JButton startbutton = new JButton("Start");
    JButton settingbutton = new JButton("Setting");
    JButton exitbutton = new JButton("Exit");
    Homepage(){
        add(this);
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