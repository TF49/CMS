package edu.cdtu.page;

import edu.cdtu.componstyle.ComponentStyle;
import edu.cdtu.dao.FamilyInfoDao;
import edu.cdtu.entity.FamilyMember;
import edu.cdtu.utils.ColorUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class QueryPanel extends JPanel implements ActionListener {

    private JTextField searchField;
    private JComboBox<String> searchCriteriaComboBox;
    private JButton searchButton;
    private JTable resultTable;
    private DefaultTableModel tableModel;
    private FamilyMember info;

    // 分页相关组件
    private JTextField pageField;
    private JButton prevButton;
    private JButton nextButton;
    private static final int PAGE_SIZE = 10;
    private int currentPage = 1;
    private int totalPages = 1;

    private FamilyInfoDao familyInfoDao = new FamilyInfoDao(); // 数据访问对象

    public QueryPanel() {
        setLayout(new BorderLayout(10, 10));

        // 初始化组件
        initializeComponents();

        // 添加组件到面板
        addComponentsToPanel();
    }

    private void initializeComponents() {
        // 查询条件选择框
        String[] criteria = {"姓名", "身份证号", "家庭编号"};
        searchCriteriaComboBox = new JComboBox<>(criteria);
        searchField = new JTextField(20);

        // 搜索按钮
        searchButton = new JButton("搜索");
        searchButton.addActionListener(this);

        // 创建表格模型和表格（增加“详情”列）
        tableModel = new DefaultTableModel(new Object[]{"姓名", "身份证号", "性别", "年龄", "地址", "详情"}, 0);
        resultTable = new JTable(tableModel);

        // 设置最后一列为按钮
        resultTable.getColumn("详情").setCellRenderer(new ButtonRenderer());
        resultTable.getColumn("详情").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(resultTable);

        // 分页控件
        pageField = new JTextField("1", 5);
        prevButton = new JButton("上一页");
        nextButton = new JButton("下一页");

        // 添加动作监听器
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                performSearch();
            }
        });

        nextButton.addActionListener(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                performSearch();
            }
        });

        // 设置布局
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(new JLabel("按以下条件查询:"));
        searchPanel.add(searchCriteriaComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        ComponentStyle.setPanelStyle(searchPanel, ColorUtils.PANEL_BACKGROUND_COLOR, 0, 0, 800, 50, this);

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.add(prevButton);
        paginationPanel.add(new JLabel("第"));
        paginationPanel.add(pageField);
        paginationPanel.add(new JLabel("页"));
        paginationPanel.add(nextButton);

        ComponentStyle.setPanelStyle(paginationPanel, ColorUtils.PANEL_BACKGROUND_COLOR, 0, 0, 800, 50, this);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    public Object getCellEditorValue() {
        int selectedRow = resultTable.getSelectedRow();
        if (selectedRow != -1) {
            String idCard = (String) resultTable.getValueAt(selectedRow, 1); // 身份证号所在列索引为1
            info = familyInfoDao.getFamilyMemberByIdCard(idCard);

            if (info != null) {
                JFrame detailFrame = new JFrame("就业与教育信息");
                detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                detailFrame.setSize(1000, 800);
                detailFrame.setLocationRelativeTo(null);

                EmploymentEduPanel employmentEduPanel = new EmploymentEduPanel();
                employmentEduPanel.fillData(info.getIdCard()); // 正确传递身份证号字符串
                detailFrame.add(employmentEduPanel);
                detailFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(QueryPanel.this, "未找到对应的家庭成员信息！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
        return "查看";
    }

    private void addComponentsToPanel() {
        // 已在初始化时添加
    }

    private void performSearch() {
        String searchCriteria = (String) searchCriteriaComboBox.getSelectedItem();
        String searchTerm = searchField.getText().trim();

        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入查询条件！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 清空表格
        tableModel.setRowCount(0);

        List<FamilyMember> results = new ArrayList<>();

        try {
            switch (searchCriteria) {
                case "姓名":
                    results = familyInfoDao.searchFamilyMembersByName(searchTerm);
                    break;
                case "身份证号":
                    FamilyMember memberByIdCard = familyInfoDao.getFamilyMemberByIdCard(searchTerm);
                    if (memberByIdCard != null) {
                        results.add(memberByIdCard);
                    }
                    break;
                case "家庭编号":
                    try {
                        int censusId = Integer.parseInt(searchTerm);
                        results = familyInfoDao.getFamilyMembersByCensusId(censusId);
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "家庭编号必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
            }

            // 计算总页数
            int totalResults = results.size();
            totalPages = (int) Math.ceil((double) totalResults / PAGE_SIZE);

            // 显示当前页的数据
            int startIdx = (currentPage - 1) * PAGE_SIZE;
            int endIdx = Math.min(startIdx + PAGE_SIZE, totalResults);

            for (int i = startIdx; i < endIdx; i++) {
                FamilyMember member = results.get(i);
                tableModel.addRow(new Object[]{
                        member.getName(),
                        member.getIdCard(),
                        member.getGender(),
                        member.getAge(),
                        member.getAddress(),
                        "查看"
                });
            }

            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "未找到匹配记录！", "提示", JOptionPane.INFORMATION_MESSAGE);
            }

            // 更新页面显示
            pageField.setText(String.valueOf(currentPage));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "查询过程中发生错误: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchButton) {
            currentPage = 1; // 重置为第一页
            performSearch();
        }
    }

    // 自定义按钮渲染器
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("查看");
            return this;
        }
    }

    // 自定义按钮编辑器
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setText("查看");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            int selectedRow = resultTable.getSelectedRow();
            if (selectedRow != -1) {
                String idCard = (String) resultTable.getValueAt(selectedRow, 1); // 身份证号所在列索引为1
                FamilyMember info = familyInfoDao.getFamilyMemberByIdCard(idCard);

                if (info != null) {
                    JFrame detailFrame = new JFrame("就业与教育信息");
                    detailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    detailFrame.setSize(1000, 800);
                    detailFrame.setLocationRelativeTo(null);

                    EmploymentEduPanel employmentEduPanel = new EmploymentEduPanel();
                    employmentEduPanel.fillData(info.getIdCard()); // 正确传递身份证号字符串
                    detailFrame.add(employmentEduPanel);
                    detailFrame.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(QueryPanel.this, "未找到对应的家庭成员信息！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
            return "查看";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("数据查询测试");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.add(new QueryPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}