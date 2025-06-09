package edu.cdtu.page;

import edu.cdtu.dao.UserDao;
import edu.cdtu.entity.User;
import edu.cdtu.utils.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class UserManagePanel extends JPanel implements ActionListener {

    private JTable userTable;
    private DefaultTableModel tableModel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    private UserDao userDao = new UserDao(); // 数据访问对象

    public UserManagePanel() {
        System.out.println("Initializing UserManagePanel...");
        setLayout(new BorderLayout(10, 10));

        // 初始化组件
        initializeComponents();

        // 添加组件到面板
        addComponentsToPanel();

        // 从数据库加载用户数据
        loadUsersFromDatabase();
    }

    private void initializeComponents() {
        // 表格模型与表格
        String[] columns = {"用户名", "角色"};
        tableModel = new DefaultTableModel(columns, 0);
        userTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (c instanceof JLabel) {
                    ((JLabel)c).setFont(new Font("宋体", Font.PLAIN, 16)); // 设置字体大小为16
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER); // 文本居中
                }
                return c;
            }
        };
        JScrollPane scrollPane = new JScrollPane(userTable);

        // 按钮面板
        JButton addButton = new JButton("添加");
        JButton editButton = new JButton("编辑");
        JButton deleteButton = new JButton("删除");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        // 表格 + 按钮组合
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 添加按钮事件
        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> showEditUserDialog());
        deleteButton.addActionListener(e -> deleteUser());

        // 将中心面板添加到 UserManagePanel
        add(centerPanel, BorderLayout.CENTER);
    }

    private void addComponentsToPanel() {
        // 已在初始化时添加
    }

    /**
     * 从数据库加载用户列表
     */
    private void loadUsersFromDatabase() {
        tableModel.setRowCount(0); // 清空原有数据
        List<User> users = userDao.getAllUsers();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getUsername(), user.getRole()});
        }
    }

    /**
     * 显示添加用户对话框
     */
    private void showAddUserDialog() {
        JPanel panel = createInputPanel();
        int result = JOptionPane.showConfirmDialog(this, panel, "添加用户", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password); // 注意：实际应加密存储
            user.setRole(role);

            if (userDao.register(user)) {
                JOptionPane.showMessageDialog(this, "用户添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadUsersFromDatabase();  // 刷新表格
            } else {
                JOptionPane.showMessageDialog(this, "添加用户失败，请检查输入或重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 显示编辑用户对话框
     */
    private void showEditUserDialog() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的用户！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentUsername = (String) tableModel.getValueAt(selectedRow, 0);
        String currentRole = (String) tableModel.getValueAt(selectedRow, 1);

        JPanel panel = createInputPanel();
        usernameField.setText(currentUsername);
        roleComboBox.setSelectedItem(currentRole);

        int result = JOptionPane.showConfirmDialog(this, panel, "编辑用户", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newUsername = usernameField.getText().trim();
            String newPassword = new String(passwordField.getPassword()).trim();
            String newRole = (String) roleComboBox.getSelectedItem();

            if (newUsername.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 获取原用户信息
            User user = userDao.getUserById(getUserIdByUsername(currentUsername));
            if (user == null) {
                JOptionPane.showMessageDialog(this, "用户不存在！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 更新信息
            user.setUsername(newUsername);
            user.setPassword(newPassword); // 注意：生产中应加密
            user.setRole(newRole);

            // 只使用 UserDao 提供的方法（username 和 newRole）
            if (userDao.updateUser(user)) {
                JOptionPane.showMessageDialog(this, "用户信息已更新！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadUsersFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "更新失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 删除选中的用户
     */
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的用户！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String username = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除选中的用户吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (userDao.deleteUser(username)) {
                JOptionPane.showMessageDialog(this, "用户删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadUsersFromDatabase();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 创建输入面板（用户名、密码、角色）
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        panel.removeAll(); // 避免重复添加

        panel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("角色:"));
        roleComboBox = new JComboBox<>(new String[]{"管理员", "录入员", "普通用户"});
        panel.add(roleComboBox);

        return panel;
    }

    /**
     * 根据用户名获取用户ID（用于更新/删除）
     */
    private int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM user WHERE username = ?";
        try (Connection conn = DbUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 空实现
    }

    /**
     * 主方法用于测试界面显示
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("用户管理测试");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.add(new UserManagePanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}