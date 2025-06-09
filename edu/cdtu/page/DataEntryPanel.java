package edu.cdtu.page;

import edu.cdtu.componstyle.ComponentStyle;
import edu.cdtu.dao.CensusDataDao;
import edu.cdtu.entity.CensusData;
import edu.cdtu.utils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataEntryPanel extends JPanel implements ActionListener {

    private JTextField nameField;
    private JTextField idCardField;
    private JComboBox<String> genderComboBox;
    private JSpinner ageSpinner;
    private JTextArea addressArea;
    private JTextField phoneField;
    private JButton submitButton;

    public DataEntryPanel() {
        setLayout(null); // 使用绝对布局
        setBackground(ColorUtils.PANEL_BACKGROUND_COLOR);
        initializeComponents();
    }

    private void initializeComponents() {
        int labelWidth = 120;     // 标签宽度
        int fieldWidth = 200;     // 输入框宽度
        int componentHeight = 40; // 组件高度
        int startX = 200;         // 起始 X 坐标
        int startY = 100;         // 起始 Y 坐标
        int gapX = 20;            // 水平间距
        int gapY = 70;            // 垂直间距

        // 姓名
        JLabel nameLabel = new JLabel("姓       名:");
        ComponentStyle.setFormStyle(nameLabel, 20, Color.BLACK, startX, startY, labelWidth, componentHeight, this);
        nameField = new JTextField();
        ComponentStyle.setFormStyle(nameField, 20, Color.BLACK, startX + labelWidth + gapX, startY, fieldWidth * 2, componentHeight, this);

        // 身份证号
        JLabel idCardLabel = new JLabel("身份证号:");
        ComponentStyle.setFormStyle(idCardLabel, 20, Color.BLACK, startX, startY + gapY, labelWidth, componentHeight, this);
        idCardField = new JTextField();
        ComponentStyle.setFormStyle(idCardField, 20, Color.BLACK, startX + labelWidth + gapX, startY + gapY, fieldWidth * 2, componentHeight, this);

        // 性别
        JLabel genderLabel = new JLabel("性      别:");
        ComponentStyle.setFormStyle(genderLabel, 20, Color.BLACK, startX, startY + gapY * 2, labelWidth, componentHeight, this);
        genderComboBox = new JComboBox<>(new String[]{"男", "女"});
        ComponentStyle.setFormStyle(genderComboBox, 20, Color.BLACK, startX + labelWidth + gapX, startY + gapY * 2, fieldWidth / 2, componentHeight, this);

        // 年龄（紧接在性别后面）
        JLabel ageLabel = new JLabel("年      龄:");
        ComponentStyle.setFormStyle(ageLabel, 20, Color.BLACK, startX + labelWidth + gapX + fieldWidth / 2 + gapX, startY + gapY * 2, labelWidth, componentHeight, this);
        ageSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 120, 1));
        ComponentStyle.setFormStyle(ageSpinner, 20, Color.BLACK, startX + labelWidth + gapX + fieldWidth / 2 + gapX + labelWidth, startY + gapY * 2, fieldWidth / 2, componentHeight, this);

        // 地址
        JLabel addressLabel = new JLabel("地      址:");
        ComponentStyle.setFormStyle(addressLabel, 20, Color.BLACK, startX, startY + gapY * 3, labelWidth, componentHeight, this);
        addressArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(addressArea);
        ComponentStyle.setScrollPaneStyle(scrollPane, startX + labelWidth + gapX, startY + gapY * 3, fieldWidth * 2, componentHeight * 2, this);

        // 电话号码
        JLabel phoneLabel = new JLabel("电   话:");
        ComponentStyle.setFormStyle(phoneLabel, 20, Color.BLACK, startX, startY + gapY * 5, labelWidth, componentHeight, this);
        phoneField = new JTextField();
        ComponentStyle.setFormStyle(phoneField, 20, Color.BLACK, startX + labelWidth + gapX, startY + gapY * 5, fieldWidth * 2, componentHeight, this);

        // 提交按钮
        submitButton = new JButton("提交");
        ComponentStyle.setButtonStyle(submitButton, ColorUtils.BUTTON_BACKGROUND_COLOR, startX + labelWidth + gapX, startY + gapY * 6, fieldWidth * 2, componentHeight + 10, this); // 增加按钮高度
        submitButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            submitData();
        }
    }

    private void submitData() {
        String name = nameField.getText().trim();
        String idCard = idCardField.getText().trim();
        String gender = (String) genderComboBox.getSelectedItem();
        int age = (Integer) ageSpinner.getValue();
        String address = addressArea.getText().trim();
        String phone = phoneField.getText().trim();

        if (name.isEmpty() || idCard.isEmpty() || address.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!idCard.matches("^\\d{17}[\\dXx]$")) {
            JOptionPane.showMessageDialog(this, "请输入有效的18位身份证号码！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CensusData data = new CensusData();
        data.setName(name);
        data.setIdCard(idCard);
        data.setGender(gender);
        data.setAge(age);
        data.setAddress(address);
        data.setPhone(phone);
        data.setRegistrationDate(new java.util.Date());

        CensusDataDao dao = new CensusDataDao();

        boolean success = dao.addCensusData(data);
        if (success) {
            JOptionPane.showMessageDialog(this, "数据已成功保存！", "成功", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "保存失败,已存在该用户！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        idCardField.setText("");
        genderComboBox.setSelectedIndex(0);
        ageSpinner.setValue(18);
        addressArea.setText("");
        phoneField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("人口普查数据录入系统");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.add(new DataEntryPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}