import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Frame;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JPasswordField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;


public class Login extends JFrame {
    protected static final String ActionEvent = null;
    // 创建一个“登录”类，并继承JFrame
    // 声明窗体中的组件
    private JPanel contentPane;
    private JTextField tName;
    private JPasswordField passwordField;
    private JLabel lblBanner;
    private Container LoginMenu;
    private Container menubar;
    private Component HelpMenu;
    private Frame frame;
    private Container content;

    /**
     * 主方法
     */
    public static void main(String[] args) {
        Login frame = new Login(); // 创建Login对象
        frame.setVisible(true); // 使frame可视
    }
    /**
     * 创建JFrame窗体
     */
    public Login() { // Login的构造方法
        setResizable(false); // 不可改变窗体大小

        setTitle("学生成绩管理系统V1.0"); // 设置窗体题目
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体关闭的方式
        setBounds(100, 100, 406, 189); // 设置窗体大小
        /**
         * 创建JPanel面板contentPane置于JFrame窗体中，并设置面板的边距和布局
         */
        contentPane = new JPanel();
        contentPane.setBackground(Color.WHITE);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        /**
         * 创建标签lblName置于面板contentPane中，设置标签的大小以及标签中字体的内容、样式
         */
        JLabel lblName = new JLabel("用户名：");
        lblName.setFont(new Font("黑体", Font.PLAIN, 16));
        lblName.setBounds(15, 14, 64, 23);
        contentPane.add(lblName);
        /**
         * 创建文本框tName置于面板contentPane中，设置文本框的大小
         */
        tName = new JTextField();
        tName.setBounds(80, 14, 156, 23);
        contentPane.add(tName);
        tName.setColumns(10);

        /**
         * 创建标签lblPwd置于面板contentPane中，设置标签的大小以及标签中字体的内容、样式
         */
        JLabel lb = new JLabel("用户登陆系统");
        lb.setHorizontalAlignment(SwingConstants.RIGHT);
        lb.setFont(new Font("楷体", Font.ITALIC, 25));
        lb.setBounds(15, 95, 160, 23);
        contentPane.add(lb);
        /**
         * 创建标签lblPwd置于面板contentPane中，设置标签的大小以及标签中字体的内容、样式
         */
        JLabel lblPwd = new JLabel("密 码：");
        lblPwd.setHorizontalAlignment(SwingConstants.RIGHT);
        lblPwd.setFont(new Font("黑体", Font.PLAIN, 16));
        lblPwd.setBounds(15, 40, 64, 23);
        contentPane.add(lblPwd);
        /**
         * 创建密码框置于面板contentPane中，设置密码框的大小
         */
        passwordField = new JPasswordField();
        passwordField.setBounds(80,40, 156, 23);
        contentPane.add(passwordField);
        /**
         * 创建按钮btnLogin置于面板contentPane中，设置按钮的大小以及按钮中字体的内容、样式
         */
        JButton btnLogin = new JButton("登　录");
        btnLogin.addActionListener(new ActionListener() { // 添加动作监听的事件
            @Override
            public void actionPerformed(ActionEvent e) { // 发生操作时
                if (tName.getText().equals("lili") && passwordField.getText().equals("123456")) { // “登陆成功”的条件
                    new qqq();     //调用登陆成功后的窗口
                    //JOptionPane.showMessageDialog(null, "登录成功！", "Success", JOptionPane.INFORMATION_MESSAGE); // 弹出框：“登陆成功！”
                } else if (tName.getText().equals("") || passwordField.getText().equals("")) { // 文本框为空时
                    JOptionPane.showMessageDialog(null, "用户名或密码不能为空！", "Warning", JOptionPane.WARNING_MESSAGE); // 弹出框：“用户名或密码不能为空！”
                } else { // 以上条件都不满足的时候
                    JOptionPane.showMessageDialog(null, "用户名或密码错误！", "Error", JOptionPane.ERROR_MESSAGE); // 弹出框：“用户名或密码错误！”
                }
            }
            public void init(JFrame frame, int width, int height) {
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(width, height);
                frame.setLocation(200, 200);
                frame.setVisible(true);
            }
        });
        btnLogin.setFont(new Font("黑体", Font.PLAIN, 16));//字体设置
        btnLogin.setBounds(240, 14, 100, 23);
        contentPane.add(btnLogin);

        /**
         * 创建标签bt置于面板contentPane中，设置标签的大小并向标签中添加图标
         */
        JButton bt = new JButton("重　置");
        bt.setFont(new Font("黑体", Font.PLAIN, 16));
        bt.setBounds(240, 40, 100, 23);
        contentPane.add(bt);
    }
    public class qqq {              //创建类，登录后弹出的窗口

        JFrame frame = new JFrame();// 创建JFrame对象
        Container content = frame.getContentPane();
        JMenuBar menubar = new JMenuBar();
        JMenu LoginMenu = new JMenu("操作菜单");
        JMenu HelpMenu = new JMenu("关于");
        public qqq(){
            JMenuItem userLoginMenu= new JMenuItem("录入学生成绩");
            JMenuItem userLoginMenu1= new JMenuItem("删除学生成绩");
            JMenuItem userLoginMenu2= new JMenuItem("修改学生成绩");
            JMenuItem userLoginMenu3= new JMenuItem("查询学生成绩");

            JMenuItem user= new JMenuItem("关于本软件");
            JMenuItem user1= new JMenuItem("帮助");
            JMenuItem exitLoginMenu= new JMenuItem("退出");

            LoginMenu.add(userLoginMenu);
            LoginMenu.add(userLoginMenu1);
            LoginMenu.add(userLoginMenu2);
            LoginMenu.add(userLoginMenu3);
            HelpMenu.add(user);
            HelpMenu.add(user1);
            LoginMenu.add(exitLoginMenu);
            menubar.add(LoginMenu);
            menubar.add(HelpMenu);

            contentPane = new JPanel();
            contentPane.setBackground(Color.WHITE);
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            setContentPane(contentPane);
            contentPane.setLayout(null);

            frame.setTitle("学生成绩管理系统V1.0");
            content.add(menubar,BorderLayout.NORTH);
            frame.setBounds(450, 200, 400, 200);
            frame.setVisible(true);//使frame可视
        }
    }
}