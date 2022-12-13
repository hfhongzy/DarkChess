package archiveManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

public class ArchiveManager {
    static JFileChooser fileChooser;
    public static void init(Frame frame) {
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        String dir = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(dir));
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getPath().toLowerCase().endsWith(".darkchess");
            }

            @Override
            public String getDescription() {
                return ".darkchess 文件";
            }
        });
    }
    public static String getReadPath() {
//        JOptionPane.showOp
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getPath();
        return null;
    }
    public static String getSavePath() {
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile().getPath();
        return null;
    }
}
