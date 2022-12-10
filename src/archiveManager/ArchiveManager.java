package archiveManager;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

public class ArchiveManager {
    static FileDialog readFileDialog;
    static FileDialog saveFileDialog;
    public static void init(Frame frame) {
        /*
        readFileDialog = new FileDialog(frame, "选择需要读取的存档", FileDialog.LOAD);
        saveFileDialog = new FileDialog(frame, "保存你的存档", FileDialog.SAVE);
        String userDir = System.getProperty("user.dir");
        readFileDialog.setDirectory(userDir);
        saveFileDialog.setDirectory(userDir);
        readFileDialog.setFilenameFilter((dir, name) -> name.endsWith(".darkchess"));
        saveFileDialog.setFilenameFilter((dir, name) -> name.endsWith(".darkchess"));
      
         */
    }
    public static String getReadPath() {
        readFileDialog.setVisible(true);
        return readFileDialog.getDirectory() + readFileDialog.getFile();
    }
    public static String getSavePath() {
        saveFileDialog.setVisible(true);
        return saveFileDialog.getDirectory() + saveFileDialog.getFile();
    }
}
