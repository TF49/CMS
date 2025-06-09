package edu.cdtu.page;

import edu.cdtu.dao.UserDao;
import edu.cdtu.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterFrame extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<String> roleComboBox; // 角色选择框

    private UserDao userDao = new UserDao(); // 引入 UserDao

    public RegisterFrame() {
        setTitle("人口普查管理系统 - 注册");
        setSize(500, 500); // 适当增大高度以容纳更多内容
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 主面板：使用 BorderLayout 布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 顶部标题
        JLabel titleLabel = new JLabel("欢迎注册人口普查系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20)); // 标题字体加大
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 10, 10); // 控制组件间距
        gbc.anchor = GridBagConstraints.WEST;

        // 设置统一字体
        Font labelFont = new Font("微软雅黑", Font.PLAIN, 16);
        Font fieldFont = new Font("微软雅黑", Font.PLAIN, 16);
        Font btnFont = new Font("微软雅黑", Font.PLAIN, 14);

        // 用户名
        JLabel usernameLabel = new JLabel("用  户  名:");
        usernameLabel.setFont(labelFont);
        usernameField = new JTextField();
        usernameField.setFont(fieldFont);
        usernameField.setPreferredSize(new Dimension(350, 30)); // 固定宽度

        // 密码
        JLabel passwordLabel = new JLabel("密       码:");
        passwordLabel.setFont(labelFont);
        passwordField = new JPasswordField();
        passwordField.setFont(fieldFont);
        passwordField.setPreferredSize(new Dimension(350, 30));

        // 确认密码
        JLabel confirmPasswordLabel = new JLabel("确认密码:");
        confirmPasswordLabel.setFont(labelFont);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(fieldFont);
        confirmPasswordField.setPreferredSize(new Dimension(350, 30));

        // 角色
        JLabel roleLabel = new JLabel("角      色:");
        roleLabel.setFont(labelFont);
        roleComboBox = new JComboBox<>(new String[]{"普通用户", "管理员", "录入员"});
        roleComboBox.setFont(fieldFont);

        // 按钮
        JButton registerButton = new JButton("注  册");
        JButton backButton = new JButton("返  回");

        registerButton.setFont(btnFont);
        backButton.setFont(btnFont);

        // 注册按钮逻辑
        registerButton.addActionListener(e -> handleRegister());

        // 返回按钮逻辑
        backButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(RegisterFrame.this,
                    "是否放弃注册并返回登录界面？", "确认返回",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginFrame();
            }
        });

        // 添加用户名行
        addComponent(formPanel, usernameLabel, 0, 0, 1, 1, gbc);
        addComponent(formPanel, usernameField, 1, 0, 1, 1, gbc);

        // 添加密码行
        addComponent(formPanel, passwordLabel, 0, 1, 1, 1, gbc);
        addComponent(formPanel, passwordField, 1, 1, 1, 1, gbc);

        // 添加确认密码行
        addComponent(formPanel, confirmPasswordLabel, 0, 2, 1, 1, gbc);
        addComponent(formPanel, confirmPasswordField, 1, 2, 1, 1, gbc);

        // 添加角色行
        addComponent(formPanel, roleLabel, 0, 3, 1, 1, gbc);
        addComponent(formPanel, roleComboBox, 1, 3, 1, 1, gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // 组装主面板
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * 辅助方法，简化向容器添加组件的过程
     */
    private void addComponent(Container container, Component component, int gridx, int gridy, int gridwidth, int gridheight, GridBagConstraints gbc) {
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 横向填充
        container.add(component, gbc);
    }

    /**
     * 处理注册逻辑
     */
    private void handleRegister() {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);
        char[] confirmPasswordChars = confirmPasswordField.getPassword();
        String confirmPassword = new String(confirmPasswordChars);

        try {
            // 清空密码字段防止泄露
            passwordField.setText("");
            confirmPasswordField.setText("");

            // 输入验证
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名不能为空", "注册失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "密码不能为空", "注册失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "两次输入的密码不一致", "注册失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (userDao.existsByUsername(username)) {
                JOptionPane.showMessageDialog(this, "用户名已存在，请更换一个", "注册失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 获取角色
            String selectedRole = (String) roleComboBox.getSelectedItem();

            // 创建用户对象
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRole(selectedRole);

            // 注册用户
            boolean success = userDao.register(newUser);
            if (success) {
                JOptionPane.showMessageDialog(this, "注册成功，请登录", "注册成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginFrame();
            } else {
                JOptionPane.showMessageDialog(this, "注册失败，请重试", "注册失败", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "发生未知错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterFrame());
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}