import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class TFrame extends JFrame {
    JPanel pane1;
    JPanel pane2;
    public TFrame() {
        setVisible(true);
        setLocationRelativeTo(null);
        pane1 = new JPanel();
        pane2 = new JPanel();
        JButton button1 = new JButton("Pane1");
        JButton button2 = new JButton("Pane2");
        button1.addActionListener(this::clickButton1);
        button2.addActionListener(this::clickButton2);
        pane1.add(button1);
        pane2.add(button2);
        Container container = getContentPane();
        container = new JPanel(new CardLayout());
        container.add(pane1, "1");
        container.add(pane2, "2");
    }
    void clickButton1(ActionEvent e) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(pane2, "2");
    }
    void clickButton2(ActionEvent e) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(pane1, "1");
    }
}
public class Test1 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TFrame mainFrame = new TFrame();
            mainFrame.setVisible(true);
        });
    }
}
