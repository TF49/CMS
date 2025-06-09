package edu.cdtu.componstyle;

import edu.cdtu.utils.ColorUtils;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class ComponentStyle {

    // JLabel 样式设置
    public static void setFormStyle(JLabel label, int fontSize, Color foreground, int x, int y, int width, int height, Container container) {
        label.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        label.setForeground(foreground);
        label.setBackground(ColorUtils.BACKGROUND_COLOR);
        label.setOpaque(true);
        label.setBounds(x, y, width, height);
        container.add(label);
    }

    // JTextField 样式设置
    public static void setFormStyle(JTextField textField, int fontSize, Color foreground, int x, int y, int width, int height, Container container) {
        textField.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        textField.setForeground(foreground);
        textField.setBackground(Color.WHITE);
        textField.setBounds(x, y, width, height);
        container.add(textField);
    }

    // JButton 样式设置（带前景色和背景色）
    public static void setFormStyle(JButton button, int fontSize, Color foreground, Color background, int x, int y, int width, int height, Container container) {
        button.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setBounds(x, y, width, height);
        container.add(button);
    }

    // JButton 简化样式（默认字体大小20，白色文字）
    public static void setButtonStyle(JButton button, Color background, int x, int y, int width, int height, Container container) {
        setFormStyle(button, 20, Color.WHITE, background, x, y, width, height, container);
    }

    // 设置 JPanel 样式
    public static void setPanelStyle(JPanel panel, Color background, int x, int y, int width, int height, Container container) {
        panel.setBackground(background);
        panel.setBounds(x, y, width, height);
        container.add(panel);
    }

    // 带边框的文本框样式
    public static void setFormStyleWithBorder(JTextField textField, int fontSize, Color foreground, Color borderColor,
                                              int x, int y, int width, int height, Container container) {
        textField.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        textField.setForeground(foreground);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createLineBorder(borderColor));
        textField.setBounds(x, y, width, height);
        container.add(textField);
    }

    // JComboBox 样式设置
    public static void setFormStyle(JComboBox<?> comboBox, int fontSize, Color foreground, int x, int y, int width, int height, Container container) {
        comboBox.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        comboBox.setForeground(foreground);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBounds(x, y, width, height);
        container.add(comboBox);
    }

    // JSpinner 样式设置
    public static void setFormStyle(JSpinner spinner, int fontSize, Color foreground, int x, int y, int width, int height, Container container) {
        spinner.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        spinner.setForeground(foreground);
        spinner.setBounds(x, y, width, height);
        container.add(spinner);
    }

    // JScrollPane 样式设置（包裹 JTextArea）
    public static void setScrollPaneStyle(JScrollPane scrollPane, int x, int y, int width, int height, Container container) {
        scrollPane.setBounds(x, y, width, height);
        container.add(scrollPane);
    }

    // JTextArea 样式设置（未包裹在 JScrollPane 中）
    public static void setFormStyle(JTextArea textArea, int fontSize, Color foreground, int x, int y, int width, int height, Container container) {
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, fontSize));
        textArea.setForeground(foreground);
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBounds(x, y, width, height);
        container.add(textArea);
    }

    // JMenuItem 菜单项样式
    public static void setMenuItemStyle(JMenuItem menuItem) {
        menuItem.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        menuItem.setForeground(ColorUtils.MENU_TEXT_COLOR);
        menuItem.setBackground(ColorUtils.MENU_BACKGROUND_COLOR);
    }

    // JMenuBar 菜单栏样式
    public static void setMenuStyle(JMenuBar menuBar) {
        menuBar.setBackground(ColorUtils.MENU_BACKGROUND_COLOR);
        menuBar.setForeground(ColorUtils.MENU_TEXT_COLOR);
        menuBar.setFont(new Font("微软雅黑", Font.BOLD, 16));
    }

    // JTabbedPane 选项卡面板样式
    public static void setTabbedPaneStyle(JTabbedPane tabbedPane) {
        tabbedPane.setBackground(ColorUtils.BACKGROUND_COLOR);
        tabbedPane.setForeground(ColorUtils.TEXT_COLOR);
        tabbedPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    }

    // 默认按钮样式（使用常量颜色）
    public static void setDefaultButtonStyle(JButton button, int x, int y, int width, int height, Container container) {
        setButtonStyle(button, ColorUtils.BUTTON_BACKGROUND_COLOR, x, y, width, height, container);
    }

    // 更灵活的组件样式设置方法（可自定义字体名称、风格）
    public static void setFormStyle(JComponent component, String fontName, int fontStyle, int fontSize,
                                    Color foreground, Color background, int x, int y, int width, int height, Container container) {
        component.setFont(new Font(fontName, fontStyle, fontSize));
        component.setForeground(foreground);
        component.setBackground(background);
        component.setBounds(x, y, width, height);
        if (component instanceof JTextComponent) {
            ((JTextComponent) component).setCaretColor(Color.BLACK); // 光标颜色
        }
        container.add(component);
    }
}