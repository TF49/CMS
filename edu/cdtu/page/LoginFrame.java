package edu.cdtu.page;

import edu.cdtu.dao.UserDao;
import edu.cdtu.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    private UserDao userDao = new UserDao(); // 引入 UserDao

    public LoginFrame() {
        // 设置窗口属性
        setTitle("人口普查管理系统 - 登录");
        setSize(500, 500); // 适当增大窗口高度
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 居中显示

        // 设置自定义图标
        setIconImage(new ImageIcon("Third/CMS/src/images/众多用户_every-user.png").getImage());

        // 主面板：使用 BorderLayout 布局
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 加载并创建 logo 标签
        ImageIcon logoIcon = new ImageIcon("Third/CMS/src/images/众多用户_every-user.png");
        JLabel logoLabel = new JLabel(logoIcon);

        // 如果需要调整图片大小
        Image image = logoIcon.getImage();
        Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH); // 调整大小以适应需求
        logoLabel.setIcon(new ImageIcon(scaledImage));

        // 添加 logo 到主面板的顶部
        mainPanel.add(logoLabel, BorderLayout.NORTH);

        // 创建标题标签并添加到主面板的中部
        JLabel titleLabel = new JLabel("欢迎登录人口普查系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 30)); // 标题字体加大
        mainPanel.add(titleLabel, BorderLayout.CENTER);

        // 表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 30, 10); // 减小行间距，原来是15
        gbc.anchor = GridBagConstraints.WEST;

        // 用户名标签和输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 加大标签字体
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 输入框字体加大

        // 密码标签和输入框
        JLabel passwordLabel = new JLabel("密   码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("微软雅黑", Font.PLAIN, 16));

        JButton loginButton = new JButton("登  录");
        JButton registerButton = new JButton("注  册");
        JButton forgotPasswordButton = new JButton("忘记密码");

        // 按钮字体统一设置
        Font btnFont = new Font("微软雅黑", Font.PLAIN, 14);
        loginButton.setFont(btnFont);
        registerButton.setFont(btnFont);
        forgotPasswordButton.setFont(btnFont);

        // 登录按钮逻辑
        loginButton.addActionListener(e -> handleLogin());

        // 注册按钮逻辑
        registerButton.addActionListener(e -> {
            dispose(); // 关闭当前窗口
            new RegisterFrame(); // 打开注册页面
        });

        // 忘记密码按钮逻辑
        forgotPasswordButton.addActionListener(this::showChangePasswordDialog);

        // 添加用户名行
        addComponent(formPanel, usernameLabel, 0, 0, 1, 1, gbc);
        addComponent(formPanel, usernameField, 1, 0, 1, 1, gbc);

        // 添加密码行
        addComponent(formPanel, passwordLabel, 0, 1, 1, 1, gbc);
        addComponent(formPanel, passwordField, 1, 1, 1, 1, gbc);

        // 创建按钮面板并添加按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5)); // 水平间距为15
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(forgotPasswordButton);

        // 将按钮面板添加到表单面板
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // 跨越两列
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(buttonPanel, gbc);

        // 将表单面板添加到主面板的底部
        mainPanel.add(formPanel, BorderLayout.SOUTH);

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
        container.add(component, gbc);
    }

    /**
     * 处理登录逻辑
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try {
            // 清空密码字段防止泄露
            passwordField.setText("");

            // 输入校验
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "用户名不能为空", "登录失败", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this, "密码不能为空", "登录失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 数据库验证
            User user = userDao.login(username, password);

            if (user != null) {
                dispose(); // 关闭当前窗口
                new MainFrame(user.getUsername()); // 统一跳转到主界面
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "用户名或密码错误", "登录失败", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(LoginFrame.this, "登录过程中发生错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * 显示修改密码对话框
     */
    private void showChangePasswordDialog(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        JTextField usernameField = new JTextField();
        JPasswordField oldPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();

        panel.add(new JLabel("用户名:"));
        panel.add(usernameField);
        panel.add(new JLabel("旧密码:"));
        panel.add(oldPasswordField);
        panel.add(new JLabel("新密码:"));
        panel.add(newPasswordField);

        int option = JOptionPane.showConfirmDialog(this, panel, "修改密码", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());

            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (oldPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "旧密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "新密码不能为空", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = userDao.changePassword(username, oldPassword, newPassword);
            if (success) {
                JOptionPane.showMessageDialog(this, "密码修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "密码修改失败，请检查旧密码是否正确", "失败", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}