package gameComponent;

import chessComponent.*;
import gameController.GameController;
import model.ChessStep;
import model.TeamColor;
import network.Client;
import network.Message;
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
    public PlayerStatus playerStatus; //注意！
    OptionalBox optionalBox;
    public void setMode(int mode) {
        this.mode = mode;
    }
    GameController gameController;
    public void clear() {
        current_time = -1;
        chessSteps.clear();
        leftSide.clear();
        rightSide.clear();
    }
    public void start(GameController gameController) {
        this.gameController = gameController;
        // 初始化step和 sideBox
        clear();
        gameController.loadGame();
    }
    public ChessComponent[][] getChessComponents() { return chessComponents; }
    public ChessComponent getChessComponent(int x, int y) {
        return chessComponents[x][y];
    }

    public void setOptionalBox(OptionalBox optionalBox) {
        this.optionalBox = optionalBox;
    }

    public Chessboard(PlayerStatus playerStatus) {
        setLayout(null);
        setSize(WIDTH, HEIGHT);
        this.playerStatus = playerStatus;
        current_time = -1;
        chessSteps = new ArrayList<>();
        //fileChooser = new JFileChooser();
        //fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        initSideBoxes();
//        initChess();
    }
    public void initSideBoxes() {
        leftSide = new SideBox(SIDEBOX_WIDTH, HEIGHT, TeamColor.RED);
        leftSide.setLocation(0, 0);
        rightSide = new SideBox(SIDEBOX_WIDTH, HEIGHT, TeamColor.BLACK);
        rightSide.setLocation(SIDEBOX_WIDTH + 4 * CHESS_WIDTH, CHESS_WIDTH);
        add(leftSide);
        add(rightSide);
    }
    public String getLastStep() { //新
        return chessSteps.get(chessSteps.size() - 1).toString();
    }
    /**
     * 将一个 Arraylist 中的棋子放到棋盘上，并删除之前的棋盘、清空 SideBox。
     */
    /*

     */
        /*
    Client client;
    ServerPanel.java server;
    */
    
    static int []limA, limB;
    static public void addChessRowInit() {
        limA = new int[]{1, 2, 2, 2, 2, 5, 2};
        limB = new int[]{1, 2, 2, 2, 2, 5, 2};
    }
    
    /**
     * 读档：读一行棋盘
     */
    static public boolean addChessRow(String s, ArrayList<ChessComponent> chessList) {
        s = s.trim();
        String [] s0 = s.split(" ");
        if(s0.length != 4) {
            JOptionPane.showMessageDialog(null, "读档失败。错误编码 102：棋盘错误。");
            return false;
        }
    
        
        for(int y = 0; y < 4; y ++) {
            if(s0[y].length() != 2 || !Character.isUpperCase(s0[y].charAt(0)) || !Character.isDigit(s0[y].charAt(1))) {
                JOptionPane.showMessageDialog(null, "读档失败。错误编码 103：棋子错误。");
                return false;
            }
            char c = s0[y].charAt(0);
            int id = s0[y].charAt(1) - '0';
            int z = -- (c == 'B' ? limA : limB) [id];
            System.out.println("z = " + z);
            if(z < 0) {
                JOptionPane.showMessageDialog(null, "读档失败。错误编码 103：棋子错误!");
                return false;
            }
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
        return true;
    }
    public static ArrayList<ChessComponent> getRandomChess() {
//        isEnded = false;
        // todo : 应该会把这个变成接口，读档/新开局两不误
        ArrayList<ChessComponent> chessList = new ArrayList<>(); // 生成棋子列表
        for (TeamColor color : TeamColor.values()) {
            chessList.add(new GeneralChessComponent(color, CHESS_WIDTH));
            for (int i = 0; i < 2; i++) {
                chessList.add(new AdvisorChessComponent(color, CHESS_WIDTH));
                chessList.add(new MinisterChessComponent(color, CHESS_WIDTH));
                chessList.add(new HorseChessComponent(color, CHESS_WIDTH));
                chessList.add(new ChariotChessComponent(color, CHESS_WIDTH));
                chessList.add(new CannonChessComponent(color, CHESS_WIDTH));
            }
            for (int i = 0; i < 5; i++)
                chessList.add(new SoldierChessComponent(color, CHESS_WIDTH));
        }
        Collections.shuffle(chessList); // 随机打乱棋子
        return chessList;
//        rebuild(chess);
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
    public void putChessOnBoard(ArrayList<ChessComponent> chessList) {
        putChessAwayBoard();
        for (int i = 0; i < chessList.size(); i++) { // 放到棋盘上
            int x = i / 4, y = i % 4;
            ChessComponent chessComponent = chessList.get(i);
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
                        gameController.onClick(chessComponent);
                    isPressed = false;
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isOnChess(e.getX(), e.getY()))
                        isPressed = true;
                }
            });
            add(chessComponents[x][y] = chessComponent);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
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
        if(from instanceof CannonChessComponent) return true; //for 读档
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
    
    public void setReachableChess(ChessComponent chess, boolean status) { // 将 chess 可到达的棋子设置成 status, 并且重新绘制
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
    
    public boolean checkFirst(ChessComponent chess) { // 判断 chess 是否可被选中
        return !chess.isEaten() && chess.getTeamColor() == playerStatus.getCurrentColor();
    }

    /**
     * @param chess
     * @return if done an operation
     */
    public boolean click(ChessComponent chess) {
        if (first == null) {
            if (chess.isReversal()) {
                flip(chess);
                chess.repaint();
                return true;
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
                return true;
            } else if (chess.isReversal()) {
                setReachableChess(first, false);
                first.setSelected(false);
                flip(chess);
                first.repaint();
                chess.repaint();
                first = null;
                return true;
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
        return false;
    }

    public boolean isEnded() {
        return playerStatus.red_score >= 60 || playerStatus.black_score >= 60;
    }
    void checkWin() {
//        if(isEnded) return ;
        if (playerStatus.red_score >= 60) {
            Message.show("Red Win!");
            isEnded = true;
        } else if (playerStatus.black_score >= 60) {
            Message.show("Black Win!");
            isEnded = true;
        } else {
            isEnded = false;
        }
    }
    void exchangePlayer() {
        playerStatus.setCurrentColor(playerStatus.getCurrentColor() == TeamColor.RED ? TeamColor.BLACK : TeamColor.RED);
        playerStatus.repaint();
    }
    
    public final ArrayList<ChessStep> chessSteps;
    public int current_time;
    
    void modifySteps(ChessStep step) {
        if (current_time == chessSteps.size()) {
            chessSteps.add(step);
        } else {
            chessSteps.set(current_time, step);
            while (current_time < chessSteps.size() - 1)
                chessSteps.remove(chessSteps.size() - 1);
        }
    }
    public void undoall() {
        while(current_time > -1) {
            undo();
        }
        chessSteps.clear();
    }
    public void undo() {
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
    
    public void redo() {
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
    
    public void move(ChessComponent chess1, ChessComponent chess2) {
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
    
    public void capture(ChessComponent chess1, ChessComponent chess2) {
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
    
    public void flip(ChessComponent chess) {
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
    /**
    * 通过 a x1 y1 x2 y2 的字符串指令行棋
    */
    public boolean moveChess(String s) {
        s = s.trim();
        String [] s0 = s.split(" ");
        if(s0.length != 5)
            return false;
        for(int j = 0; j < 5; j ++)
            if(s0[j].length() != 1 || !Character.isDigit(s0[j].charAt(0)))
                return false;
            
        int x1 = s0[1].charAt(0) - '0';
        int y1 = s0[2].charAt(0) - '0';
        int x2 = s0[3].charAt(0) - '0';
        int y2 = s0[4].charAt(0) - '0';
    
        if(s0[0].charAt(0) == '1') {
            if(!chessComponents[x1][y1].isReversal())
                return false;
            flip(chessComponents[x1][y1]);
        } else if(s0[0].charAt(0) == '2') {
            if(!checkMoveTo(chessComponents[x1][y1], chessComponents[x2][y2]))
                return false;
            move(chessComponents[x1][y1], chessComponents[x2][y2]);
        } else {
            if(!checkMoveTo(chessComponents[x1][y1], chessComponents[x2][y2])) {
                return false;
            }
            capture(chessComponents[x1][y1], chessComponents[x2][y2]);
        }
        return true;
    }
}
