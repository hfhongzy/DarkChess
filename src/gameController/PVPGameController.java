package gameController;

import chessComponent.ChessComponent;
import gameComponent.Chessboard;
import model.ChessStep;
import model.TeamColor;
import network.Message;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class PVPGameController extends GameController {
    public PVPGameController() {
        super();
    }
    @Override
    public void loadGame() {
        if (chessList == null)
            chessList = Chessboard.getRandomChess();
        chessboard.putChessOnBoard(new ArrayList<>(chessList));
        for(String step : chessSteps)
            if(!chessboard.moveChess(step)) {
                chessboard.undoall();
                Message.show("读档失败。错误编码 105：行棋步骤错误。");
                return ;
            }
    }
    @Override
    public void onClick(ChessComponent chess) {
        if(chessboard.isEnded()) return;
        chessboard.click(chess);
    }
    @Override
    public void restart() {
        chessList = null;
    }
    
    @Override
    public void save() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setApproveButtonMnemonic(0);
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".darkchess");
            }
            @Override
            public String getDescription() {
                return ".darkchess 文件";
            }
        });
        int fres = fileChooser.showSaveDialog(null);
        if(fres == JFileChooser.APPROVE_OPTION) {
            System.out.println("OK!");
            System.out.println(fileChooser.getSelectedFile());
            File newFile;
            if(fileChooser.getSelectedFile().getName().toLowerCase().endsWith(".darkchess")) {
                newFile = fileChooser.getSelectedFile();
            } else {
                newFile = new File(fileChooser.getSelectedFile() + ".darkchess");
            }
            try (FileWriter out = new FileWriter(newFile)) {
                int pos = 0;
                for(int i = 0; i < 8; i ++) {
                    for (int j = 0; j < 4; j++) {
                        ChessComponent cur = chessList.get(pos++);
                        out.write((cur.getTeamColor() == TeamColor.BLACK ? "B" : "R") + cur.getID());
                        out.write(' ');
                    }
                    out.write('\n');
                }
                
                out.write(chessboard.playerStatus.getCurrentColor() == TeamColor.BLACK ? "B" : "R");
                out.write('\n');
                out.write(chessboard.chessSteps.size() + "\n");
                for(ChessStep step : chessboard.chessSteps) {
                    out.write(step.toString());
                    out.write('\n');
                }
                out.flush();
                JOptionPane.showMessageDialog(null, "保存成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        } else if(fres == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "保存失败！");
            return ;
        }
    }
    
    @Override
    public boolean load() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".darkchess");
            }
            @Override
            public String getDescription() {
                return ".darkchess 文件";
            }
        });
        int fres = fileChooser.showOpenDialog(null);
        if(fres == JFileChooser.APPROVE_OPTION) {
            System.out.println(fileChooser.getSelectedFile());
            File newFile;
            if(fileChooser.getSelectedFile().getName().toLowerCase().endsWith(".darkchess")) {
                newFile = fileChooser.getSelectedFile();
            } else {
                JOptionPane.showMessageDialog(null, "读档失败。错误编码 101：文件后缀名错误。");
                return false;
            }
            try (FileReader in = new FileReader(newFile)) {
                BufferedReader buffIn = new BufferedReader(in);
                chessList.clear();
                Chessboard.addChessRowInit();
                for(int x = 0; x < 8; x ++) {
                    String s = buffIn.readLine();
                    if(s == null) break;
                    if(!Chessboard.addChessRow(s, chessList))
                        return false;
                }
                String s = buffIn.readLine();
                if(s == null || s.length() == 0 || (s.charAt(0) != 'B' && s.charAt(0) != 'R')) {
                    JOptionPane.showMessageDialog(null, "读档失败。错误编码 104：缺少行棋方。");
                    return false;
                }
                int n = Integer.parseInt(buffIn.readLine());
                if(n < 0) return false;
                chessSteps.clear();
                for(int i = 0; i < n; i ++) {
                    s = buffIn.readLine();
                    if(s == null) break;
                    chessSteps.add(s);
                }
                System.out.println("wow!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        } else if(fres == JFileChooser.CANCEL_OPTION) {
//            JOptionPane.showMessageDialog(null, "读档失败！");
            return false;
        }
        return true;
    }
}
