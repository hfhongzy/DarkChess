package gameComponent;

import chessComponent.*;
import model.ChessStep;
import model.PanelType;
import model.TeamColor;
import network.Client;
import network.Server;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
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
    private boolean isEnded;
    private int mode;
    PlayerStatus playerStatus;
    OptionalBox optionalBox;
    
    public void setMode(int mode) {
        this.mode = mode;
        isServer = mode == 1;
        nowServer = true;
    }
    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }
    public int getMode() {
        return mode;
    }
    public void setOptionalBox(OptionalBox optionalBox) {
        this.optionalBox = optionalBox;
    }

    //    UndoButton undo;
//    RedoButton redo;
    public Chessboard(PlayerStatus playerStatus) {
        mode = 0;
        setLayout(null);
        setSize(WIDTH, HEIGHT);
        this.playerStatus = playerStatus;
        current_time = -1;
        chessSteps = new ArrayList<>();
        fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        initSideBoxs();
        initChessOnBoard();
    }
    public void initSideBoxs() {
        leftSide = new SideBox(SIDEBOX_WIDTH, HEIGHT, TeamColor.RED);
        leftSide.setLocation(0, 0);
        rightSide = new SideBox(SIDEBOX_WIDTH, HEIGHT, TeamColor.BLACK);
        rightSide.setLocation(SIDEBOX_WIDTH + 4 * CHESS_WIDTH, CHESS_WIDTH);
        add(leftSide);
        add(rightSide);
    }
    /**
     * 将一个 Arraylist 中的棋子放到棋盘上，并删除之前的棋盘、清空 SideBox。
     */
    public void rebuild(ArrayList<ChessComponent> chess) {
        current_time = -1;
//        optionalBox.clear();
        chessSteps.clear();
        leftSide.clear();
        rightSide.clear();
        playerStatus.clear();
        putChessAwayBoard();
        for (int i = 0; i < chess.size(); i++) { // 放到棋盘上
            int x = i / 4, y = i % 4;
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
        putChessOnBoard();
    }
    Client client;
    Server server;
    
    public void setClient(Client client) {
        this.client = client;
    }
    public void setServer(Server server) {
        this.server = server;
    }
    public void serverStart() {
        restart();
        for(int i = 0; i < 8; i ++) {
            String s = new String();
            for (int j = 0; j < 4; j++) {
                s += (chessComponents[i][j].getTeamColor() == TeamColor.BLACK ? "B" : "R") + chessComponents[i][j].getID();
                if(j < 3)
                    s += " ";
            }
            server.send(s);
        }
    }
    private void addChessRow(String s, ArrayList<ChessComponent> chessList) {
        s = s.trim();
        String [] s0 = s.split(" ");
        if(s0.length != 4) {
            JOptionPane.showMessageDialog(null, "读档失败。错误编码 102：棋盘错误。");
            return ;
        }
        for(int y = 0; y < 4; y ++) {
            if(s0[y].length() != 2 || !Character.isUpperCase(s0[y].charAt(0)) || !Character.isDigit(s0[y].charAt(1))) {
                JOptionPane.showMessageDialog(null, "读档失败。错误编码 103：棋子错误。");
                return ;
            }
            char c = s0[y].charAt(0);
            int id = s0[y].charAt(1) - '0';
            if(id == 0)
                chessList.add(new GeneralChessComponent(c == 'B' ? TeamColor.BLACK : TeamColor.RED, CHESS_WIDTH));
            if(id == 1)
                chessList.add(new AdvisorChessComponent(c == 'B' ? TeamColor.BLACK : TeamColor.RED, CHESS_WIDTH));
            if(id == 2)
                chessList.add(new MinisterChessComponent(c == 'B' ? TeamColor.BLACK : TeamColor.RED, CHESS_WIDTH));
            if(id == 3)
                chessList.add(new ChariotChessComponent(c == 'B' ? TeamColor.BLACK : TeamColor.RED, CHESS_WIDTH));
            if(id == 4)
                chessList.add(new HorseChessComponent(c == 'B' ? TeamColor.BLACK : TeamColor.RED, CHESS_WIDTH));
            if(id == 5)
                chessList.add(new SoldierChessComponent(c == 'B' ? TeamColor.BLACK : TeamColor.RED, CHESS_WIDTH));
            if(id == 6)
                chessList.add(new CannonChessComponent(c == 'B' ? TeamColor.BLACK : TeamColor.RED, CHESS_WIDTH));
        }
    }
    public void clientStart() {
        ArrayList<ChessComponent> chessList = new ArrayList<>();
        for(int x = 0; x < 8; x ++) {
            String s = client.read();
            if(s == null) break;
            addChessRow(s, chessList);
        }
        rebuild(chessList);
        String s = client.read();
        if(s.equals("quit")) {
        
        } else {
            moveChess(s);
            nowServer = !nowServer;
        }
    }
    public void initChessOnBoard() {
        isEnded = false;
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
        rebuild(chess);
    }
    
    public void putChessAwayBoard() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 4; j++)
                if(chessComponents[i][j] != null) {
                    chessComponents[i][j].setVisible(false);
                    chessComponents[i][j].repaint();
                    remove(chessComponents[i][j]);
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
            isEnded = true;
        } else if (playerStatus.black_score >= 60) {
            JOptionPane.showMessageDialog(null, "Black Win!");
            isEnded = true;
        } else {
            isEnded = false;
        }
    }
    boolean isServer, nowServer;
    void sendMyMove() { //向对方发送我的走棋，从 ArrayList 最后一个元素得到
        if(chessSteps.size() == 0) {
            System.out.println("Error, no moves.");
            return ;
        }
        ChessStep cur = chessSteps.get(chessSteps.size() - 1);
        if(isServer) {
            server.send(cur.toString());
            String s = server.read();
            if(s.equals("quit")) {
            
            } else {
                moveChess(s);
                nowServer = !nowServer;
            }
        } else {
            client.send(cur.toString());
            String s = client.read();
            if(s.equals("quit")) {
            
            } else {
                moveChess(s);
                nowServer = !nowServer;
            }
        }
    }
    void onClickOnline(ChessComponent chess) {
        if(nowServer != isServer)
            return ;
        if (first == null) {
            if (chess.isReversal()) {
                flip(chess);
                chess.repaint(); //!!!!!!
                sendMyMove();
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
                sendMyMove(); //new
            } else if (chess.isReversal()) {
                setReachableChess(first, false);
                first.setSelected(false);
                flip(chess);
                
                first.repaint();
                chess.repaint();
                first = null;
                sendMyMove(); //new
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
    void onClick(ChessComponent chess) {
        if(isEnded) return ;
        if(mode == 2) {
            onClickOnline(chess);
            return ;
        }
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
        
        if (current_time == -1)
            return;
        
        current_time--;
        ChessStep opt = chessSteps.get(current_time + 1);
        if (opt.getType() == 1) {
            flipBuiltin(chessComponents[opt.x1][opt.y1]);
        } else if (opt.getType() == 2) {
            moveBuiltin(chessComponents[opt.x2][opt.y2], chessComponents[opt.x1][opt.y1]);
        } else {
            captureBuiltin(chessComponents[opt.x2][opt.y2], chessComponents[opt.x1][opt.y1]);
        }
        chessComponents[opt.x1][opt.y1].repaint();
        if (opt.getType() != 1)
            chessComponents[opt.x2][opt.y2].repaint();
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
        
        if (current_time == chessSteps.size() - 1)
            return;
        
        current_time++;
        ChessStep opt = chessSteps.get(current_time);
        if (opt.getType() == 1) {
            flipBuiltin(chessComponents[opt.x1][opt.y1]);
        } else if (opt.getType() == 2) {
            moveBuiltin(chessComponents[opt.x1][opt.y1], chessComponents[opt.x2][opt.y2]);
        } else {
            captureBuiltin(chessComponents[opt.x1][opt.y1], chessComponents[opt.x2][opt.y2]);
        }
        chessComponents[opt.x1][opt.y1].repaint();
        if (opt.getType() != 1)
            chessComponents[opt.x2][opt.y2].repaint();
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
        modifySteps(new ChessStep(2, chess1.X(), chess1.Y(), chess2.X(), chess2.Y()));
        moveBuiltin(chess1, chess2);
        
        // 设置按钮启用或禁用
        optionalBox.undoButton.setWorking(true);
        optionalBox.redoButton.setWorking(false);
        chess1.repaint();
        chess2.repaint();
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
        }
        checkWin();
    }
    
    void capture(ChessComponent chess1, ChessComponent chess2) {
        current_time++;
        modifySteps(new ChessStep(3, chess1.X(), chess1.Y(), chess2.X(), chess2.Y()));
        captureBuiltin(chess1, chess2);
        // 设置按钮启用或禁用
        optionalBox.undoButton.setWorking(true);
        optionalBox.redoButton.setWorking(false);
        chess1.repaint();
        chess2.repaint();
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
        modifySteps(new ChessStep(1, chess.X(), chess.Y()));
        flipBuiltin(chess);
        // 设置按钮启用或禁用
        optionalBox.undoButton.setWorking(true);
        optionalBox.redoButton.setWorking(false);
        chess.repaint();
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
    JFileChooser fileChooser;
    void saveChess() {
//        FileDialog fileDialog = new FileDialog();
//        fileDialog.setVisible(true);
//        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        fileChooser.setCurrentDirectory(new File(currentPath));
//        fileChooser.set
        fileChooser.setApproveButtonMnemonic(0);
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
                //格式
                // 8*4 B0~B6 R0~R6 初始棋盘
                // Black/Red
                // n
                // 1 x y
                // 2 a b x y
                //tql
                for(int i = 0; i < chessSteps.size(); i ++)
                    undo();
                for(int i = 0; i < 8; i ++) {
                    for (int j = 0; j < 4; j++) {
                        out.write((chessComponents[i][j].getTeamColor() == TeamColor.BLACK ? "B" : "R") + chessComponents[i][j].getID());
                        out.write(' ');
                    }
                    out.write('\n');
                }
                for(int i = 0; i < chessSteps.size(); i ++)
                    redo();
                out.write(playerStatus.getCurrentColor() == TeamColor.BLACK ? "B" : "R");
                out.write('\n');
                out.write(chessSteps.size() + "\n");
                for(ChessStep step : chessSteps) {
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
    void moveChess(String s) {
        s = s.trim();
        String [] s0 = s.split(" ");
        if(s0.length != 5) {
            JOptionPane.showMessageDialog(null, "读档失败。错误编码 105：行棋步骤错误。");
            return ;
        }
        for(int j = 0; j < 5; j ++)
            if(s0[j].length() != 1 || !Character.isDigit(s0[j].charAt(0))) {
                JOptionPane.showMessageDialog(null, "读档失败。错误编码 105：行棋步骤错误。");
                return ;
            }
        int x1 = s0[1].charAt(0) - '0';
        int y1 = s0[2].charAt(0) - '0';
        int x2 = s0[3].charAt(0) - '0';
        int y2 = s0[4].charAt(0) - '0';
    
        if(s0[0].charAt(0) == '1') {
            flip(chessComponents[x1][y1]);
        } else if(s0[0].charAt(0) == '2') {
            move(chessComponents[x1][y1], chessComponents[x2][y2]);
        } else {
            capture(chessComponents[x1][y1], chessComponents[x2][y2]);
        }
    }
    void loadChess() {
//        JFileChooser fileChooser = new JFileChooser();
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
                return ;
            }
            try (FileReader in = new FileReader(newFile)) {
                BufferedReader buffIn = new BufferedReader(in);
                ArrayList<ChessComponent> chessList = new ArrayList<>();
                for(int x = 0; x < 8; x ++) {
                    String s = buffIn.readLine();
                    if(s == null) break;
                    addChessRow(s, chessList);
                }
                rebuild(chessList);
                buffIn.readLine();
                int n = Integer.parseInt(buffIn.readLine());
                for(int i = 0; i < n; i ++) {
                    String s = buffIn.readLine();
                    if(s == null) break;
                    moveChess(s);
                }
                JOptionPane.showMessageDialog(null, "读档成功！");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } else if(fres == JFileChooser.CANCEL_OPTION) {
            JOptionPane.showMessageDialog(null, "读档失败！");
            return ;
        }
        /*
        * 1
        * */
    }
    void restart() {
        initChessOnBoard();
    }
}
