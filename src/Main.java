import archiveManager.ArchiveManager;
import network.Message;
import view.MainFrame;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class Main {
    
    public static void main(String[] args) {
        System.out.println(args.length);
        //Orz System.getProperty()
        
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(800,600);
//            ArchiveManager.init(mainFrame);
//            System.out.println(ArchiveManager.getSavePath());
            mainFrame.setVisible(true);
        });
//        System.out.println("LAK!!!");
    }
}
