package view;

import model.PanelType;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final int WIDTH;
    private final int HEIGHT;
    public Container pane; // 显示的 panel，用于切换多个页面
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

        // 初始化 panel
        JPanel mainPanel = new MainPanel(this);
        JPanel playGamePanel = new PlayGamePanel();

        pane.add(PanelType.MAIN_PANEL, mainPanel);
        pane.add(PanelType.PLAY_GAME_PANEL, playGamePanel); // 名称 - panel

        showPanel(PanelType.MAIN_PANEL);
//        ((CardLayout) pane.getLayout()).show(pane, PanelType.MAIN_PANEL); // 展示 card

        pack();
        setLocationRelativeTo(null); // 窗口居中
    }
    public void showPanel(String panelType) {
        ((CardLayout) pane.getLayout()).show(pane, panelType);
    }
}
