package edu.cdtu.page;

import edu.cdtu.componstyle.ComponentStyle;
import edu.cdtu.utils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    private String currentUser;

    public MainFrame(String username) {
        this.currentUser = username;

        setTitle("人口普查管理系统 - 欢迎您 " + username);
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示
        setBackground(ColorUtils.BACKGROUND_COLOR);

        // 设置自定义图标
        setIconImage(new ImageIcon("Third/CMS/src/images/众多用户_every-user.png").getImage());

        // 加载并创建 logo 标签
        ImageIcon logoIcon = new ImageIcon("Third/CMS/src/images/众多用户_every-user.png");
        JLabel logoLabel = new JLabel(logoIcon);

        // 如果需要调整图片大小
        Image image = logoIcon.getImage();
        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 调整大小以适应需求
        logoLabel.setIcon(new ImageIcon(scaledImage));

        // 初始化菜单栏并应用样式
        JMenuBar menuBar = createMenuBar();
        ComponentStyle.setMenuStyle(menuBar);
        setJMenuBar(menuBar);

        // 创建选项卡面板并应用样式
        JTabbedPane tabbedPane = new JTabbedPane();
        ComponentStyle.setTabbedPaneStyle(tabbedPane);
        add(tabbedPane, BorderLayout.CENTER);

        // 添加各个功能模块的标签页
        tabbedPane.addTab("数据录入", null, new DataEntryPanel(), "添加人员基本信息");
        tabbedPane.addTab("家庭信息录入", null, new FamilyEntryPanel(), "录入家庭成员关系");
        tabbedPane.addTab("就业与教育信息录入", null, new EmploymentEduPanel(), "录入就业与教育信息");
        tabbedPane.addTab("数据查询", null, new QueryPanel(), "查询已录入的信息");
        tabbedPane.addTab("用户管理", null, new UserManagePanel(), "系统用户管理");
        tabbedPane.addTab("数据管理", null, new ManagePanel(), "查看/编辑已录入数据");

        setVisible(true);
    }

    /**
     * 创建一个简单的占位面板
     */
    private JPanel createDummyPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(ColorUtils.BACKGROUND_COLOR);
        JLabel label = new JLabel(title + " 功能暂未实现");
        label.setFont(new Font("微软雅黑", Font.BOLD, 16));
        label.setForeground(ColorUtils.LABEL_TEXT_COLOR);
        panel.add(label);
        return panel;
    }

    /**
     * 创建菜单栏
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // 文件菜单
        JMenu fileMenu = new JMenu("文件");
        fileMenu.setForeground(ColorUtils.LABEL_TEXT_COLOR);

        JMenuItem logoutItem = new JMenuItem("注销");
        JMenuItem exitItem = new JMenuItem("退出");

        ComponentStyle.setMenuItemStyle(logoutItem);
        ComponentStyle.setMenuItemStyle(exitItem);

        logoutItem.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "确定要注销吗？", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose(); // 关闭当前窗口
                new LoginFrame(); // 回到登录界面
            }
        });

        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        // 帮助菜单
        JMenu helpMenu = new JMenu("帮助");
        helpMenu.setForeground(ColorUtils.TEXT_COLOR);

        JMenuItem aboutItem = new JMenuItem("关于");
        ComponentStyle.setMenuItemStyle(aboutItem);
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    /**
     * 显示“关于”对话框
     */
    private void showAboutDialog() {
        String message = "人口普查管理系统 v1.0\n\n"
                + "开发者：涂家乐 王曾鹏 韩利军\n"
                + "技术栈：Java Swing + MySQL + JDBC\n"
                + "功能模块：\n"
                + "  - 数据录入\n"
                + "  - 家庭信息管理\n"
                + "  - 就业与教育统计\n"
                + "  - 用户权限控制\n"
                + "  - 数据管理生成";
        JOptionPane.showMessageDialog(this, message, "关于系统", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 程序入口点
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 测试用：模拟登录后的用户名
            new MainFrame("admin"); // 可替换为实际登录用户名
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}