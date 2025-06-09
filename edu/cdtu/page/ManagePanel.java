package edu.cdtu.page;

import edu.cdtu.dao.CensusDataDao;
import edu.cdtu.entity.CensusData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManagePanel extends JPanel implements ActionListener {

    private JTable dataTable;
    private DefaultTableModel tableModel;

    private JTextField nameField;
    private JTextField idCardField;
    private JComboBox<String> genderComboBox;
    private JTextField ageField;
    private JTextField addressField;
    private JTextField phoneField;

    private CensusDataDao censusDataDao = new CensusDataDao();

    private JTextField searchField;  // 新增：搜索框

    public ManagePanel() {
        setLayout(new BorderLayout(10, 10));

        initializeComponents();
        loadDatabaseData();
    }

    private void initializeComponents() {
        String[] columns = {"姓名", "身份证号", "性别", "年龄", "地址", "电话"};
        tableModel = new DefaultTableModel(columns, 0);
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);

        JButton addButton = new JButton("添加");
        JButton editButton = new JButton("编辑");
        JButton deleteButton = new JButton("删除");
        JButton refreshButton = new JButton("刷新");

        // 新增：搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("搜索");
        JButton resetButton = new JButton("重置");

        searchPanel.add(new JLabel("姓名搜索:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // 添加组件
        add(searchPanel, BorderLayout.PAGE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 添加事件监听器
        addButton.addActionListener(e -> showAddDataDialog());
        editButton.addActionListener(e -> showEditDataDialog());
        deleteButton.addActionListener(e -> deleteData());
        refreshButton.addActionListener(e -> loadDatabaseData());
        searchButton.addActionListener(e -> searchData());
        resetButton.addActionListener(e -> loadDatabaseData());
    }

    // 新增方法：根据姓名搜索数据
    private void searchData() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入要搜索的姓名关键字！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<CensusData> dataList = censusDataDao.searchCensusDataByName(keyword);
        updateTableData(dataList);
    }

    // 更新表格数据的方法（复用）
    private void updateTableData(List<CensusData> dataList) {
        tableModel.setRowCount(0);  // 清空表格
        for (CensusData data : dataList) {
            tableModel.addRow(new Object[]{
                    data.getName(),
                    data.getIdCard(),
                    data.getGender(),
                    data.getAge(),
                    data.getAddress(),
                    data.getPhone()
            });
        }
    }

    private void loadDatabaseData() {
        List<CensusData> dataList = censusDataDao.getAllCensusData();
        updateTableData(dataList);
    }

    private void showAddDataDialog() {
        JPanel panel = createInputPanel();
        int result = JOptionPane.showConfirmDialog(this, panel, "添加数据", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String idCard = idCardField.getText().trim();
            String gender = (String) genderComboBox.getSelectedItem();
            String ageStr = ageField.getText().trim();
            String address = addressField.getText().trim();
            String phone = phoneField.getText().trim();

            if (name.isEmpty() || idCard.isEmpty() || ageStr.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "必填字段不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入有效的年龄数字！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CensusData data = new CensusData();
            data.setName(name);
            data.setIdCard(idCard);
            data.setGender(gender);
            data.setAge(age);
            data.setAddress(address);
            data.setPhone(phone);

            if (censusDataDao.addCensusData(data)) {
                loadDatabaseData();
                JOptionPane.showMessageDialog(this, "添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showEditDataDialog() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的行！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String currentIdCard = (String) tableModel.getValueAt(selectedRow, 1);

        CensusData originalData = censusDataDao.getCensusDataByIdCard(currentIdCard);
        if (originalData == null) {
            JOptionPane.showMessageDialog(this, "找不到该身份证号的数据！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JPanel panel = createInputPanel();

        nameField.setText(originalData.getName());
        idCardField.setText(originalData.getIdCard());
        genderComboBox.setSelectedItem(originalData.getGender());
        ageField.setText(String.valueOf(originalData.getAge()));
        addressField.setText(originalData.getAddress());
        phoneField.setText(originalData.getPhone());

        int result = JOptionPane.showConfirmDialog(this, panel, "编辑数据", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newIdCard = idCardField.getText().trim();
            String newGender = (String) genderComboBox.getSelectedItem();
            String newAgeStr = ageField.getText().trim();
            String newAddress = addressField.getText().trim();
            String newPhone = phoneField.getText().trim();

            if (newName.isEmpty() || newIdCard.isEmpty() || newAgeStr.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(this, "必填字段不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int newAge;
            try {
                newAge = Integer.parseInt(newAgeStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "请输入有效的年龄数字！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            CensusData newData = new CensusData();
            newData.setName(newName);
            newData.setIdCard(newIdCard);
            newData.setGender(newGender);
            newData.setAge(newAge);
            newData.setAddress(newAddress);
            newData.setPhone(newPhone);

            if (censusDataDao.updateCensusData(newData)) {
                loadDatabaseData();
                JOptionPane.showMessageDialog(this, "更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "更新失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteData() {
        int selectedRow = dataTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的行！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idCard = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这条记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (censusDataDao.deleteCensusDataByIdCard(idCard)) {
                loadDatabaseData();
                JOptionPane.showMessageDialog(this, "删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));

        panel.removeAll();

        panel.add(new JLabel("姓名:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("身份证号:"));
        idCardField = new JTextField();
        panel.add(idCardField);

        panel.add(new JLabel("性别:"));
        genderComboBox = new JComboBox<>(new String[]{"男", "女"});
        panel.add(genderComboBox);

        panel.add(new JLabel("年龄:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("地址:"));
        addressField = new JTextField();
        panel.add(addressField);

        panel.add(new JLabel("电话:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.revalidate();
        panel.repaint();

        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 空实现
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("人口数据管理系统");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.add(new ManagePanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}