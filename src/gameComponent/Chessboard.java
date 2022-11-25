package gameComponent;

import chessComponent.*;
import model.ChessStep;
import model.TeamColor;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Chessboard extends JComponent {
    ChessComponent[][] chessComponents = new ChessComponent[8][4];
    SideBox leftSide, rightSide; // 展示被吃的棋子 Left == Red
    ChessComponent first; // = null : 未选中 != null : 选中一个未被eaten的棋子
    public static final int CHESS_WIDTH = 70;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;
    private static final int SIDEBOX_WIDTH = 110;
    public static final int TOP_SPACING_LENGTH = 20;
    PlayerStatus playerStatus;
    OptionalBox optionalBox;
    public void setOptionalBox(OptionalBox optionalBox) {
        this.optionalBox = optionalBox;
    }

    //    UndoButton undo;
//    RedoButton redo;
    public Chessboard(PlayerStatus playerStatus) {
        setLayout(null);
        setSize(WIDTH, HEIGHT);
        this.playerStatus = playerStatus;
        current_time = -1;
        chessSteps = new ArrayList<>();
        initChessOnBoard();
        putChessOnBoard();
        initSideBoxs();
    }
    public void initSideBoxs() {
        leftSide = new SideBox(SIDEBOX_WIDTH, HEIGHT, TeamColor.RED);
        leftSide.setLocation(0, 0);
        rightSide = new SideBox(SIDEBOX_WIDTH, HEIGHT, TeamColor.BLACK);
        rightSide.setLocation(SIDEBOX_WIDTH + 4 * CHESS_WIDTH, CHESS_WIDTH);
        add(leftSide);
        add(rightSide);
    }
    public void initChessOnBoard() {
        // todo : 应该会把这个变成接口，读档/新开局两不误
        ArrayList<ChessComponent> chess = new ArrayList<>(); // 生成棋子列表
        for (TeamColor color : TeamColor.values()) {
            chess.add(new GeneralChessComponent(color, CHESS_WIDTH));
            for (int i = 0; i < 2; i++) {
                chess.add(new AdvisorChessComponent(color, CHESS_WIDTH));
                chess.add(new MinisterChessComponent(color, CHESS_WIDTH));
                chess.add(new HorseChessComponent(color, CHESS_WIDTH));
                chess.add(new ChariotChessComponent(color, CHESS_WIDTH));
                chess.add(new CannonChessComponent(color, CHESS_WIDTH));
            }
            for (int i = 0; i < 5; i++)
                chess.add(new SoldierChessComponent(color, CHESS_WIDTH));
        }
        Collections.shuffle(chess); // 随机打乱棋子
        for (int i = 0; i < chess.size(); i++) { // 放到棋盘上
            int x = i % 8, y = i / 8;
            ChessComponent chessComponent = chess.get(i);
            chessComponent.setX(x);
            chessComponent.setY(y);
            chessComponent.setLocation(SIDEBOX_WIDTH + y * CHESS_WIDTH, TOP_SPACING_LENGTH + x * CHESS_WIDTH);
            chessComponent.addMouseListener(new MouseAdapter() {
                boolean isPressed = false;
                boolean isOnChess(int X, int Y) {
                    return 2 * Math.sqrt((X - CHESS_WIDTH / 2.0) * (X - CHESS_WIDTH / 2.0) + (Y - CHESS_WIDTH / 2.0) * (Y - CHESS_WIDTH / 2.0)) <= CHESS_WIDTH;
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (isPressed && isOnChess(e.getX(), e.getY()))
                        onClick(chessComponent);
                    isPressed = false;
                }
    
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isOnChess(e.getX(), e.getY()))
                        isPressed = true;
                }
            });
            chessComponents[x][y] = chessComponent;
        }
    }
    
    public void putChessOnBoard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 4; j++)
                add(chessComponents[i][j]);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        // todo : 绘制一个棋盘
        
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, WIDTH, HEIGHT);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        for (int i = 1; i < 8; i++)
            g2.drawLine(SIDEBOX_WIDTH, TOP_SPACING_LENGTH + i * CHESS_WIDTH, SIDEBOX_WIDTH + 4 * CHESS_WIDTH, TOP_SPACING_LENGTH + i * CHESS_WIDTH);
        for (int i = 1; i < 4; i++)
            g2.drawLine(SIDEBOX_WIDTH + i * CHESS_WIDTH, TOP_SPACING_LENGTH, SIDEBOX_WIDTH + i * CHESS_WIDTH, TOP_SPACING_LENGTH + 8 * CHESS_WIDTH);
        
        g2.setStroke(new BasicStroke(2f));
        g2.drawRect(SIDEBOX_WIDTH, TOP_SPACING_LENGTH, 4 * CHESS_WIDTH, 8 * CHESS_WIDTH);
    }
    
    /*
    boolean checkMoveTo(boolean isCannon, ChessComponent chess) {
        return chess.isEaten() || (isCannon || !chess.isReversal()) && chess.getTeamColor() != playerStatus.currentColor;
    }
    */
    boolean checkMoveTo(ChessComponent from, ChessComponent to) {
        if (to.isEaten()) return true;
        if (to.isReversal()) return false;
        if (from.getTeamColor() == to.getTeamColor()) return false;
        if (from instanceof SoldierChessComponent) {
            return (to instanceof SoldierChessComponent) || (to instanceof GeneralChessComponent);
        } else if (to instanceof CannonChessComponent) {
            return true;
        } else {
            return from.getID() <= to.getID();
        }
    }
    
    private boolean validXY(int x, int y) {
        return 0 <= x && x < 8 && 0 <= y && y < 4;
    }
    
    void setReachableChess(ChessComponent chess, boolean status) { // 将 chess 可到达的棋子设置成 status, 并且重新绘制
        ArrayList<ChessComponent> chessArr = new ArrayList<>();
        int X = chess.X(), Y = chess.Y();
        final int[] dx = {1, 0, -1, 0};
        final int[] dy = {0, 1, 0, -1};
        if (chess instanceof CannonChessComponent) {
            for (int k = 0; k < 4; k++) {
                int x = X, y = Y, cnt = 0;
                while (true) {
                    x += dx[k];
                    y += dy[k];
                    if (!validXY(x, y))
                        break;
                    if (!chessComponents[x][y].isEaten())
                        cnt++;
                    if (cnt == 2) {
                        chessArr.add(chessComponents[x][y]);
                        break;
                    }
                }
            }
        } else {
            for (int k = 0; k < 4; k++) {
                int x = X + dx[k], y = Y + dy[k];
                if (validXY(x, y) && checkMoveTo(chess, chessComponents[x][y])) {
                    chessArr.add(chessComponents[x][y]);
                }
            }
        }
        for (ChessComponent item : chessArr) {
            item.setReachable(status);
            item.repaint();
        }
    }
    
    boolean checkFirst(ChessComponent chess) { // 判断 chess 是否可被选中
        return !chess.isEaten() && chess.getTeamColor() == playerStatus.getCurrentColor();
    }
    void checkWin() {
        if (playerStatus.red_score >= 60) {
            JOptionPane.showMessageDialog(null, "Red Win!");
        } else if (playerStatus.black_score >= 60) {
            JOptionPane.showMessageDialog(null, "Black Win!");
        }
    }
    
    void onClick(ChessComponent chess) {
        if (first == null) {
            if (chess.isReversal()) {
                flip(chess);
                chess.repaint(); //!!!!!!
            } else if (checkFirst(chess)) {
                setReachableChess(chess, true);
                first = chess;
                first.setSelected(true);
                first.repaint();
            }
        } else {
            if (first == chess) {
                setReachableChess(first, false);
                first.setSelected(false);
                first.repaint();
                first = null;
            } else if (chess.isReachable()) {
                setReachableChess(first, false);
                first.setSelected(false);
                if (chess.isEaten()) move(first, chess);
                else capture(first, chess);
                first.repaint();
                chess.repaint();
                first = null;
            } else if (chess.isReversal()) {
                setReachableChess(first, false);
                first.setSelected(false);
                flip(chess);
                first.repaint();
                chess.repaint();
                first = null;
            } else if (checkFirst(chess)) {
                setReachableChess(first, false);
                first.setSelected(false);
                setReachableChess(chess, true);
                chess.setSelected(true);
                first.repaint();
                chess.repaint();
                first = chess;
            }
        }
    }
    
    void exchangePlayer() {
        playerStatus.setCurrentColor(playerStatus.getCurrentColor() == TeamColor.RED ? TeamColor.BLACK : TeamColor.RED);
        playerStatus.repaint();
    }
    
    final private ArrayList<ChessStep> chessSteps;
    int current_time;
    
    void modifySteps(ChessStep step) {
        if (current_time == chessSteps.size()) {
            chessSteps.add(step);
        } else {
            chessSteps.set(current_time, step);
            while (current_time < chessSteps.size() - 1)
                chessSteps.remove(chessSteps.size() - 1);
        }
    }
    
    void undo() { //
        if (first != null) {
            first.setSelected(false);
            first.repaint();
            setReachableChess(first, false);
        }
        /*
        if (current_time == -1)
            return;
        */
        current_time--;
        ChessStep opt = chessSteps.get(current_time + 1);
        if (opt.getType() == 1) {
            flipBuiltin(opt.getChess1());
        } else if (opt.getType() == 2) {
            moveBuiltin(opt.getChess1(), opt.getChess2());
        } else {
            captureBuiltin(opt.getChess1(), opt.getChess2());
        }
        opt.getChess1().repaint();
        if (opt.getType() != 1)
            opt.getChess2().repaint();
        // 判断按钮是否启用或禁用
        if (current_time == -1)
            optionalBox.undoButton.setWorking(false);
        optionalBox.redoButton.setWorking(true);
    }
    
    void redo() {
        if (first != null) {
            first.setSelected(false);
            first.repaint();
            setReachableChess(first, false);
        }
        /*
        if (current_time == chessSteps.size() - 1)
            return;
        */
        current_time++;
        ChessStep opt = chessSteps.get(current_time);
        if (opt.getType() == 1) {
            flipBuiltin(opt.getChess1());
        } else if (opt.getType() == 2) {
            moveBuiltin(opt.getChess1(), opt.getChess2());
        } else {
            captureBuiltin(opt.getChess1(), opt.getChess2());
        }
        opt.getChess1().repaint();
        if (opt.getType() != 1)
            opt.getChess2().repaint();
        // 判断按钮是否启用或禁用
        if (current_time == chessSteps.size() - 1)
            optionalBox.redoButton.setWorking(false);
        optionalBox.undoButton.setWorking(true);
    }
    
    //Builtin 是内置函数，用于撤销和重做
    //如果是玩家操作，由对应操作调用 Builtin
    void moveBuiltin(ChessComponent chess1, ChessComponent chess2) {
        int x1 = chess1.X(), y1 = chess1.Y();
        int x2 = chess2.X(), y2 = chess2.Y();
        chessComponents[x1][y1] = chess2;
        chessComponents[x2][y2] = chess1;
        int t = chess1.X();
        chess1.setX(chess2.X());
        chess2.setX(t);
        t = chess1.Y();
        chess1.setY(chess2.Y());
        chess2.setY(t);
        Point tt = chess1.getLocation();
        chess1.setLocation(chess2.getLocation());
        chess2.setLocation(tt);
        exchangePlayer();
    }
    
    void move(ChessComponent chess1, ChessComponent chess2) {
        current_time++;
        moveBuiltin(chess1, chess2);
        modifySteps(new ChessStep(2, chess1, chess2));
        // 设置按钮启用或禁用
        optionalBox.undoButton.setWorking(true);
        optionalBox.redoButton.setWorking(false);
    }
    
    void captureBuiltin(ChessComponent chess1, ChessComponent chess2) {
        if (chess2.isEaten()) {
            if (chess2.getTeamColor() == TeamColor.RED) {
                playerStatus.black_score -= chess2.getScore();
                leftSide.removeEatenChess(chess2);
            }
            else {
                playerStatus.red_score -= chess2.getScore();
                rightSide.removeEatenChess(chess2);
            }
            chess2.setEaten(false);
            moveBuiltin(chess1, chess2);
        }
        else {
            if (chess2.getTeamColor() == TeamColor.RED) {
                playerStatus.black_score += chess2.getScore();
                leftSide.addEatenChess(chess2);
            }
            else {
                playerStatus.red_score += chess2.getScore();
                rightSide.addEatenChess(chess2);
            }
            chess2.setEaten(true);
            moveBuiltin(chess1, chess2);
            checkWin();
        }
    }
    
    void capture(ChessComponent chess1, ChessComponent chess2) {
        current_time++;
        modifySteps(new ChessStep(3, chess1, chess2));
        captureBuiltin(chess1, chess2);
        // 设置按钮启用或禁用
        optionalBox.undoButton.setWorking(true);
        optionalBox.redoButton.setWorking(false);
    }
    
    void flipBuiltin(ChessComponent chess) {
        if (chess.isReversal()) {
            chess.setReversal(false);
            if (playerStatus.getCurrentColor() == null) // 第一次翻转
                playerStatus.setCurrentColor(chess.getTeamColor());
            exchangePlayer();
        } else {
            chess.setReversal(true);
            if (current_time == -1) {
                playerStatus.setCurrentColor(null);
                playerStatus.repaint();
            } else {
                exchangePlayer();
            }
        }
    }
    
    void flip(ChessComponent chess) {
        current_time++;
        modifySteps(new ChessStep(1, chess));
        flipBuiltin(chess);
        // 设置按钮启用或禁用
        optionalBox.undoButton.setWorking(true);
        optionalBox.redoButton.setWorking(false);
    }
    void switchCheating() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                chessComponents[i][j].switchCheating();
                if (chessComponents[i][j].isReversal() && !chessComponents[i][j].isEaten())
                    chessComponents[i][j].repaint();
            }
        }
    }
    
    //暂时存这里
    void saveChess() {
        JFileChooser file = new JFileChooser();
        file.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".darkchess");
            }
            @Override
            public String getDescription() {
                return ".darkchess 文件";
            }
        });
        int fres = file.showSaveDialog(null);
        if(fres == JFileChooser.APPROVE_OPTION) {
            System.out.println("OK!");
            System.out.println(file.getSelectedFile());
            File newFile;
            if(file.getSelectedFile().getName().toLowerCase().endsWith(".darkchess")) {
                newFile = file.getSelectedFile();
            } else {
                newFile = new File(file.getSelectedFile() + ".darkchess");
            }
            try (FileWriter out = new FileWriter(newFile)) {
                out.write(1234);
                JOptionPane.showMessageDialog(null, "保存成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else if(fres == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "保存失败！");
            return ;
        }
    }
    void loadChess() {
        JFileChooser file = new JFileChooser();
        file.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".darkchess");
            }
            @Override
            public String getDescription() {
                return ".darkchess 文件";
            }
        });
        int fres = file.showOpenDialog(null);
        if(fres == JFileChooser.APPROVE_OPTION) {
            System.out.println(file.getSelectedFile());
            File newFile;
            if(file.getSelectedFile().getName().toLowerCase().endsWith(".darkchess")) {
                newFile = file.getSelectedFile();
            } else {
                JOptionPane.showMessageDialog(null, "读档失败。错误编码 101：文件后缀名错误。");
                return ;
            }
            try (FileReader in = new FileReader(newFile)) {
            
//                JOptionPane.showMessageDialog(null, "成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else if(fres == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "读档失败！");
            return ;
        }
    }
}
