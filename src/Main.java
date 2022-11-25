import view.MainFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        //Orz
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(800,600);
            mainFrame.setVisible(true);
        });
    }
}
