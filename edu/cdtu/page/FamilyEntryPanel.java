package edu.cdtu.page;

import edu.cdtu.componstyle.ComponentStyle;
import edu.cdtu.dao.FamilyInfoDao;
import edu.cdtu.entity.FamilyMember;
import edu.cdtu.utils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FamilyEntryPanel extends JPanel implements ActionListener{

    private JTextField familyIdField;
    private JTextField memberNameField;
    private JComboBox<String> relationComboBox;
    private JTextField idCardField;
    private JSpinner ageSpinner;
    private JTextArea remarksArea;
    private JButton addButton;
    private JButton submitButton;
    private JTextField phoneField;       // 联系电话
    private JTextField addressField;     // 家庭住址

    // 模拟临时存储当前家庭成员列表（用于显示）
    private DefaultListModel<String> familyListModel;
    private JList<String> familyMemberList;

    // 存储实际数据对象，等待提交
    private List<FamilyMember> pendingMembers = new ArrayList<>();

    // DAO 对象
    private FamilyInfoDao familyInfoDao = new FamilyInfoDao();

    public FamilyEntryPanel() {
        setLayout(new BorderLayout(10, 10));

        // 初始化组件
        initializeComponents();

        // 添加组件到面板
        addComponentsToPanel();
    }

    private void initializeComponents() {
        // 创建表单面板（左侧），使用绝对布局（null）
        JPanel formPanel = new JPanel(null); // 手动设置布局为 null，方便定位
        ComponentStyle.setPanelStyle(formPanel, ColorUtils.PANEL_BACKGROUND_COLOR, 0, 0, 500, 400, this);

        int labelX = 50;   // 标签起始 X 坐标
        int fieldX = 180;  // 输入框起始 X 坐标
        int startY = 30;   // 起始 Y 坐标
        int rowHeight = 30; // 每行高度
        int fieldWidth = 250; // 输入框宽度

        // 家庭编号
        JLabel familyIdLabel = new JLabel("家庭编号:");
        ComponentStyle.setFormStyle(familyIdLabel, "微软雅黑", Font.PLAIN, 16, ColorUtils.LABEL_TEXT_COLOR, ColorUtils.PANEL_BACKGROUND_COLOR, labelX, startY + 0 * rowHeight, 100, rowHeight, formPanel);
        familyIdField = new JTextField(20);
        ComponentStyle.setFormStyleWithBorder(familyIdField, 10, Color.BLACK, ColorUtils.BORDER_COLOR, fieldX, startY + 0 * rowHeight, fieldWidth, rowHeight, formPanel);

        // 成员姓名
        JLabel memberNameLabel = new JLabel("成员姓名:");
        ComponentStyle.setFormStyle(memberNameLabel, 16, ColorUtils.LABEL_TEXT_COLOR, labelX, startY + 2 * rowHeight, 100, rowHeight, formPanel);
        memberNameField = new JTextField(20);
        ComponentStyle.setFormStyleWithBorder(memberNameField, 16, Color.BLACK, ColorUtils.BORDER_COLOR, fieldX, startY + 2 * rowHeight, fieldWidth, rowHeight, formPanel);

        // 与户主关系
        JLabel relationLabel = new JLabel("与户主关系:");
        ComponentStyle.setFormStyle(relationLabel, 16, ColorUtils.LABEL_TEXT_COLOR, labelX, startY + 4 * rowHeight, 100, rowHeight, formPanel);
        relationComboBox = new JComboBox<>(new String[]{"户主", "配偶", "子女", "父母", "兄弟姐妹", "其他"});
        ComponentStyle.setFormStyle(relationComboBox, 16, Color.BLACK, fieldX, startY + 4 * rowHeight, fieldWidth, rowHeight, formPanel);

        // 身份证号
        JLabel idCardLabel = new JLabel("身份证号:");
        ComponentStyle.setFormStyle(idCardLabel, 16, ColorUtils.LABEL_TEXT_COLOR, labelX, startY + 6 * rowHeight, 100, rowHeight, formPanel);
        idCardField = new JTextField(20);
        ComponentStyle.setFormStyleWithBorder(idCardField, 16, Color.BLACK, ColorUtils.BORDER_COLOR, fieldX, startY + 6 * rowHeight, fieldWidth, rowHeight, formPanel);

        // 年龄
        JLabel ageLabel = new JLabel("年      龄:");
        ComponentStyle.setFormStyle(ageLabel, 16, ColorUtils.LABEL_TEXT_COLOR, labelX, startY + 8 * rowHeight, 100, rowHeight, formPanel);
        ageSpinner = new JSpinner(new SpinnerNumberModel(30, 0, 120, 1));
        ComponentStyle.setFormStyle(ageSpinner, 16, Color.BLACK, fieldX, startY + 8 * rowHeight, fieldWidth, rowHeight, formPanel);

        // 联系电话
        JLabel phoneLabel = new JLabel("联系电话:");
        ComponentStyle.setFormStyle(phoneLabel, 16, ColorUtils.LABEL_TEXT_COLOR, labelX, startY + 10 * rowHeight, 100, rowHeight, formPanel);
        phoneField = new JTextField(20);
        ComponentStyle.setFormStyleWithBorder(phoneField, 16, Color.BLACK, ColorUtils.BORDER_COLOR, fieldX, startY + 10 * rowHeight, fieldWidth, rowHeight, formPanel);

        // 家庭住址
        JLabel addressLabel = new JLabel("家庭住址:");
        ComponentStyle.setFormStyle(addressLabel, 16, ColorUtils.LABEL_TEXT_COLOR, labelX, startY + 12 * rowHeight, 100, rowHeight, formPanel);
        addressField = new JTextField(20);
        ComponentStyle.setFormStyleWithBorder(addressField, 16, Color.BLACK, ColorUtils.BORDER_COLOR, fieldX, startY + 12 * rowHeight, fieldWidth, rowHeight, formPanel);


        // 备注
        JLabel remarksLabel = new JLabel("备      注:");
        ComponentStyle.setFormStyle(remarksLabel, 16, ColorUtils.LABEL_TEXT_COLOR, labelX, startY + 14 * rowHeight, 100, rowHeight, formPanel);
        remarksArea = new JTextArea(3, 40);
        ComponentStyle.setFormStyle(remarksArea, 16, Color.BLACK, fieldX, startY + 14 * rowHeight, fieldWidth, 80, formPanel);


        // 按钮
        addButton = new JButton("添加");
        ComponentStyle.setDefaultButtonStyle(addButton, fieldX, startY + 18 * rowHeight, 90, 40, formPanel);

        submitButton = new JButton("提交");
        ComponentStyle.setDefaultButtonStyle(submitButton, fieldX + 110, startY + 18* rowHeight, 90, 40, formPanel);

        // 成员列表（右侧）
        familyListModel = new DefaultListModel<>();
        familyMemberList = new JList<>(familyListModel);
        JScrollPane listScrollPane = new JScrollPane(familyMemberList);
        ComponentStyle.setScrollPaneStyle(listScrollPane, 0, 0, 400, 400, this);

        // 左右布局：左侧缩小，右侧扩大
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formPanel, listScrollPane);
        splitPane.setDividerLocation(500);
        add(splitPane, BorderLayout.CENTER);

        // 添加按钮事件
        addButton.addActionListener(e -> addFamilyMember());
        submitButton.addActionListener(e -> submitFamilyData());
    }

    private void addComponentsToPanel() {
        // 已在初始化时添加
    }

    private void addFamilyMember() {
        String name = memberNameField.getText().trim();
        String idCard = idCardField.getText().trim();
        String relation = (String) relationComboBox.getSelectedItem();
        int age = (Integer) ageSpinner.getValue();
        String remarks = remarksArea.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty() || idCard.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写姓名和身份证号！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!idCard.matches("^\\d{17}[\\dXx]$")) {
            JOptionPane.showMessageDialog(this, "请输入有效的18位身份证号码！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (familyInfoDao.isIdCardExists(idCard)) {
            JOptionPane.showMessageDialog(this, "该身份证号已存在，请检查输入！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String familyIdStr = familyIdField.getText().trim();
        if (familyIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先输入家庭编号（户籍ID）！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int censusId;
        try {
            censusId = Integer.parseInt(familyIdStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "家庭编号必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        FamilyMember member = new FamilyMember();
        member.setCensusId(censusId);
        member.setName(name);
        member.setRelation(relation);
        member.setIdCard(idCard);
        member.setGender("男");
        member.setAge(age);
        member.setPhone("");
        member.setRemarks(remarks);
        member.setPhone(phone);
        member.setAddress(address);

        boolean success = familyInfoDao.addFamilyMember(member);
        if (success) {
            String entry = String.format("姓名：%s | 关系：%s | 身份证：%s | 年龄：%d | 电话：%s | 地址：%s | 备注：%s", name, relation, idCard, age, phone.isEmpty() ? "-" : phone, address.isEmpty() ? "-" : address, remarks.isEmpty() ? "-" : remarks);
            familyListModel.addElement(entry);
            pendingMembers.add(member);

            memberNameField.setText("");
            idCardField.setText("");
            ageSpinner.setValue(18);
            remarksArea.setText("");
            relationComboBox.setSelectedItem("其他");

            JOptionPane.showMessageDialog(this, "已添加家庭成员：" + name, "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "添加失败，请重试！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void submitFamilyData() {
        if (pendingMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "没有可提交的家庭成员！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "家庭信息已全部提交成功！", "完成", JOptionPane.INFORMATION_MESSAGE);
        clearAllFields();
    }

    private void clearAllFields() {
        familyIdField.setText("");
        familyListModel.clear();
        pendingMembers.clear();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("家庭信息录入测试");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.add(new FamilyEntryPanel());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}