package panel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    final static String PLAY_GAME_PANEL = "PLAYGAMEPANEL";
    private final int WIDTH;
    private final int HEIGHT;
    Container pane; // 显示的 panel，用于切换多个页面
    public MainFrame(int width, int height) {
        WIDTH = width;
        HEIGHT = height;

        setTitle("ChessGame - Demo");
//        setSize(width, height);
//        setBackground(Color.GREEN); // 背景颜色
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setResizable(false); // 不可调大小
        setLayout(new CardLayout()); // 设置层展示

        pane = getContentPane();
        pane.setPreferredSize(new Dimension(width, height));

        JPanel playGamePanel = new PlayGamePanel();
        pane.add(PLAY_GAME_PANEL, playGamePanel); // 名称 - panel

        // card "pj"
        JPanel pj = new JPanel();
        JButton j = new JButton("testj");
        pj.add(j);
        pane.add("pj", pj);
        
        
        ((CardLayout) pane.getLayout()).show(pane, PLAY_GAME_PANEL); // 展示 card
//        getContentPane();
        pack();
        setLocationRelativeTo(null); // 窗口居中
    }
}
