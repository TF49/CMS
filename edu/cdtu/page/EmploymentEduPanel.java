package edu.cdtu.page;

import edu.cdtu.componstyle.ComponentStyle;
import edu.cdtu.dao.EmploymentEduDao;
import edu.cdtu.entity.EmploymentEdu;
import edu.cdtu.utils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmploymentEduPanel extends JPanel implements ActionListener {

    // 输入组件声明
    private JTextField nameField;
    private JTextField idCardField;
    private JTextField occupationField;
    private JComboBox<String> employmentStatusComboBox;
    private JComboBox<String> educationLevelComboBox;
    private JTextField schoolField;
    private JTextField majorField;
    private JSpinner enrollmentYearSpinner;
    private JSpinner graduationYearSpinner;
    private JTextArea remarksArea;
    private JButton submitButton;
    private JButton resetButton;

    // Dao 实例
    private final EmploymentEduDao employmentEduDao = new EmploymentEduDao();

    public EmploymentEduPanel() {
        setLayout(null); // 使用绝对布局
        setBackground(ColorUtils.PANEL_BACKGROUND_COLOR);
        initializeComponents();
    }

    private void initializeComponents() {
        int labelWidth = 100;     // 标签宽度
        int fieldWidth = 200;     // 输入框宽度
        int componentHeight = 35; // 组件高度
        int startX = 150;         // 起始 X 坐标
        int startY = 100;         // 起始 Y 坐标
        int gapY = 70;            // 垂直间距
        int gapX = 20;            // 水平间距

        // 初始化所有输入组件和标签
        initLabelAndTextField("姓    名:", startX, startY, labelWidth, fieldWidth, componentHeight, gapX, nameField = new JTextField());
        initLabelAndTextField("身份证号:", startX + labelWidth + gapX + fieldWidth + gapX, startY, labelWidth, fieldWidth, componentHeight, gapX, idCardField = new JTextField());

        initLabelAndTextField("当前职业:", startX, startY + gapY, labelWidth, fieldWidth, componentHeight, gapX, occupationField = new JTextField());
        initLabelAndComboBox("就业状态:", startX + labelWidth + gapX + fieldWidth + gapX, startY + gapY, labelWidth, fieldWidth, componentHeight, gapX, employmentStatusComboBox = new JComboBox<>(new String[]{"在职", "失业", "退休", "学生", "自由职业", "其他"}));

        initLabelAndComboBox("最高学历:", startX, startY + gapY * 2, labelWidth, fieldWidth, componentHeight, gapX, educationLevelComboBox = new JComboBox<>(new String[]{"小学", "初中", "高中", "中专", "大专", "本科", "硕士", "博士"}));
        initLabelAndTextField("毕业院校:", startX + labelWidth + gapX + fieldWidth + gapX, startY + gapY * 2, labelWidth, fieldWidth, componentHeight, gapX, schoolField = new JTextField());

        initLabelAndTextField("所学专业:", startX, startY + gapY * 3, labelWidth, fieldWidth, componentHeight, gapX, majorField = new JTextField());
        initLabelAndSpinner("入学年份:", startX + labelWidth + gapX + fieldWidth + gapX, startY + gapY * 3, labelWidth, fieldWidth, componentHeight, gapX, enrollmentYearSpinner = new JSpinner(new SpinnerNumberModel(2000, 1950, 2100, 1)));

        initLabelAndSpinner("毕业年份:", startX, startY + gapY * 4, labelWidth, fieldWidth, componentHeight, gapX, graduationYearSpinner = new JSpinner(new SpinnerNumberModel(2004, 1950, 2100, 1)));
        initLabelAndTextArea("备   注:", startX + labelWidth + gapX + fieldWidth + gapX, startY + gapY * 4, labelWidth, fieldWidth, componentHeight * 2, gapX);

        // 提交 & 重置按钮
        submitButton = new JButton("提交");
        ComponentStyle.setButtonStyle(submitButton, ColorUtils.BUTTON_BACKGROUND_COLOR, startX + labelWidth + gapX, startY + gapY * 7, fieldWidth, componentHeight + 10, this);
        submitButton.addActionListener(this);

        resetButton = new JButton("重置");
        ComponentStyle.setButtonStyle(resetButton, ColorUtils.BUTTON_BACKGROUND_COLOR, startX + labelWidth + gapX + fieldWidth + 30, startY + gapY * 7, fieldWidth, componentHeight + 10, this);
        resetButton.addActionListener(e -> clearForm());

        add(submitButton);
        add(resetButton);
    }

    private void initLabelAndTextField(String labelText, int xLabel, int yLabel, int labelWidth, int fieldWidth, int height, int gapX, JTextField textField) {
        JLabel label = new JLabel(labelText);
        ComponentStyle.setFormStyle(label, 16, Color.BLACK, xLabel, yLabel, labelWidth, height, this);
        ComponentStyle.setFormStyle(textField, 16, Color.BLACK, xLabel + labelWidth + gapX, yLabel, fieldWidth, height, this);
        add(label);
        add(textField);
    }

    private void initLabelAndComboBox(String labelText, int xLabel, int yLabel, int labelWidth, int fieldWidth, int height, int gapX, JComboBox<String> comboBox) {
        JLabel label = new JLabel(labelText);
        ComponentStyle.setFormStyle(label, 16, Color.BLACK, xLabel, yLabel, labelWidth, height, this);
        ComponentStyle.setFormStyle(comboBox, 16, Color.BLACK, xLabel + labelWidth + gapX, yLabel, fieldWidth, height, this);
        add(label);
        add(comboBox);
    }

    private void initLabelAndSpinner(String labelText, int xLabel, int yLabel, int labelWidth, int fieldWidth, int height, int gapX, JSpinner spinner) {
        JLabel label = new JLabel(labelText);
        ComponentStyle.setFormStyle(label, 16, Color.BLACK, xLabel, yLabel, labelWidth, height, this);
        ComponentStyle.setFormStyle(spinner, 16, Color.BLACK, xLabel + labelWidth + gapX, yLabel, fieldWidth, height, this);
        add(label);
        add(spinner);
    }

    private void initLabelAndTextArea(String labelText, int xLabel, int yLabel, int labelWidth, int fieldWidth, int height, int gapX) {
        JLabel label = new JLabel(labelText);
        ComponentStyle.setFormStyle(label, 16, Color.BLACK, xLabel, yLabel, labelWidth, height, this);
        remarksArea = new JTextArea(3, 20);
        JScrollPane scrollPane = new JScrollPane(remarksArea);
        ComponentStyle.setScrollPaneStyle(scrollPane, xLabel + labelWidth + gapX, yLabel, fieldWidth, height * 2, this);
        add(label);
        add(scrollPane);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            submitData();
        }
    }

    private void submitData() {
        if (!validateForm()) return;

        String name = nameField.getText().trim();
        String idCard = idCardField.getText().trim();
        String occupation = occupationField.getText().trim();
        String employmentStatus = (String) employmentStatusComboBox.getSelectedItem();
        String educationLevel = (String) educationLevelComboBox.getSelectedItem();
        String school = schoolField.getText().trim();
        String major = majorField.getText().trim();
        int enrollmentYear = (Integer) enrollmentYearSpinner.getValue();
        int graduationYear = (Integer) graduationYearSpinner.getValue();
        String remarks = remarksArea.getText().trim();

        EmploymentEdu info = new EmploymentEdu();
        info.setName(name);
        info.setIdCard(idCard);
        info.setHighestEducation(educationLevel);
        info.setMajor(major);
        info.setSchool(school);
        info.setEmploymentStatus(employmentStatus);
        info.setJobTitle(occupation);
        info.setCompany(""); // 可选填
        info.setEnrollmentYear(enrollmentYear);
        info.setGraduationYear(graduationYear);
        info.setRemarks(remarks);

        boolean result;
        if (employmentEduDao.isExistsByIdCard(idCard)) {
            result = employmentEduDao.updateEmploymentEduInfo(info);
            JOptionPane.showMessageDialog(this, result ? "更新成功！" : "更新失败，请重试。", "提示", result ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        } else {
            result = employmentEduDao.addEmploymentEduInfo(info);
            JOptionPane.showMessageDialog(this, result ? "添加成功！" : "添加失败，请重试。", "提示", result ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        }

        if (result) clearForm();
    }

    private boolean validateForm() {
        String idCard = idCardField.getText().trim();
        if (idCard.isEmpty()) {
            JOptionPane.showMessageDialog(this, "身份证号不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!idCard.matches("\\d{17}[\\dXx]")) {
            JOptionPane.showMessageDialog(this, "请输入正确的身份证号码！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int enroll = (Integer) enrollmentYearSpinner.getValue();
        int graduate = (Integer) graduationYearSpinner.getValue();
        if (graduate < enroll) {
            JOptionPane.showMessageDialog(this, "毕业年份不能早于入学年份！", "错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    public void fillData(String idCard) {
        EmploymentEdu info = employmentEduDao.getEmploymentEduByIdCard(idCard); // 假设 employmentEduDao 是已初始化的 DAO 对象

        if (info != null) {
            nameField.setText(info.getName());
            idCardField.setText(info.getIdCard());
            occupationField.setText(info.getJobTitle());
            employmentStatusComboBox.setSelectedItem(info.getEmploymentStatus());

            educationLevelComboBox.setSelectedItem(info.getHighestEducation());
            schoolField.setText(info.getSchool());
            majorField.setText(info.getMajor());

            enrollmentYearSpinner.setValue(info.getEnrollmentYear());
            graduationYearSpinner.setValue(info.getGraduationYear());

            remarksArea.setText(info.getRemarks());
        } else {
            JOptionPane.showMessageDialog(this, "未找到相关就业与教育信息！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        idCardField.setText("");
        occupationField.setText("");
        schoolField.setText("");
        majorField.setText("");
        remarksArea.setText("");
        employmentStatusComboBox.setSelectedIndex(0);
        educationLevelComboBox.setSelectedIndex(0);
        enrollmentYearSpinner.setValue(2000);
        graduationYearSpinner.setValue(2004);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("就业与教育信息录入系统");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 800);
            frame.add(new EmploymentEduPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}