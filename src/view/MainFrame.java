package view;

import model.PanelType;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final int WIDTH;
    private final int HEIGHT;
    public Container pane; // 显示的 panel，用于切换多个页面
    JPanel mainPanel, playGamePanel; // 对应的panel
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
        mainPanel = new MainPanel(this);
        playGamePanel = new PlayGamePanel();
        // 加入 panel
        pane.add(PanelType.MAIN_PANEL, mainPanel);
        pane.add(PanelType.PLAY_GAME_PANEL, playGamePanel); // 名称 - panel
        // 展示当前 panel
        showPanel(PanelType.MAIN_PANEL);

        pack(); // 自动调整大小
        setLocationRelativeTo(null); // 窗口居中
    }
    public void showPanel(String panelType) {
        ((CardLayout) pane.getLayout()).show(pane, panelType);
    }
}
