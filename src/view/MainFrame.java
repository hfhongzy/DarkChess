package view;

import model.PanelType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainFrame extends JFrame {
    public Container pane; // 显示的 panel，用于切换多个页面
    JPanel mainPanel, playGamePanel, doublePlayerPanel; // 对应的panel
    public MainFrame(int width, int height) {
        setTitle("ChessGame - Demo");
//        setBackground(Color.GREEN); // 背景颜色
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE); //设置程序关闭按键，如果点击右上方的叉就游戏全部关闭了
        setResizable(false); // 不可调大小
        setLayout(new CardLayout()); // 设置层展示

        pane = getContentPane();
        pane.setPreferredSize(new Dimension(width, height));

        // 初始化 panel
        mainPanel = new MainPanel(this);
        doublePlayerPanel = new DoublePlayerPanel(this);
        playGamePanel = new PlayGamePanel(this);
        // 加入 panel
        pane.add(PanelType.MAIN_PANEL, mainPanel);
        pane.add(PanelType.DOUBLE_PLAYER_PANEL, doublePlayerPanel);
        pane.add(PanelType.PLAY_GAME_PANEL, playGamePanel); // 名称 - panel
        // 展示当前 panel
        showPanel(PanelType.MAIN_PANEL);

        pack(); // 自动调整大小
        setLocationRelativeTo(null); // 窗口居中
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (JOptionPane.showConfirmDialog(null, "你真的要退出吗？", null, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
    }
    public void showPanel(String panelType) {
        ((CardLayout) pane.getLayout()).show(pane, panelType);
    }
}
