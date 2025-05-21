package com.simpleshop.gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.simpleshop.dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGUI extends JFrame {
    private UserDAO userDAO;
    private JTextField usernameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleComboBox;

    public RegisterGUI(UserDAO userDAO) {
        this.userDAO = userDAO;
        initComponents();
    }

    private void initComponents() {
        // Apply FlatLaf Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Không thể tải theme FlatLaf. Sử dụng theme mặc định.", "Lỗi", JOptionPane.WARNING_MESSAGE);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        setTitle("Đăng ký - Simple Shop Manager");
        setSize(400, 350);
        setMinimumSize(new Dimension(300, 250));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom font
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(30, 30, 30));

        JLabel usernameLabel = new JLabel("Tài khoản:");
        usernameLabel.setFont(labelFont);
        usernameLabel.setForeground(Color.WHITE);
        usernameField = new JTextField();
        usernameField.setFont(inputFont);
        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);

        JLabel passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setFont(labelFont);
        passwordLabel.setForeground(Color.WHITE);
        passwordField = new JPasswordField();
        passwordField.setFont(inputFont);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);

        JLabel confirmPasswordLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPasswordLabel.setFont(labelFont);
        confirmPasswordLabel.setForeground(Color.WHITE);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setFont(inputFont);
        mainPanel.add(confirmPasswordLabel);
        mainPanel.add(confirmPasswordField);

        JLabel roleLabel = new JLabel("Quyền:");
        roleLabel.setFont(labelFont);
        roleLabel.setForeground(Color.WHITE);
        roleComboBox = new JComboBox<>(new String[]{"Admin", "User"});
        roleComboBox.setFont(inputFont);
        mainPanel.add(roleLabel);
        mainPanel.add(roleComboBox);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        JButton registerButton = createStyledButton("Đăng ký");
        JButton backButton = createStyledButton("Quay lại đăng nhập");
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // Add components to frame
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        registerButton.addActionListener(e -> register());
        backButton.addActionListener(e -> backToLogin());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(30, 150, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 120, 215));
            }
        });

        return button;
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        String role = (String) roleComboBox.getSelectedItem();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu và xác nhận mật khẩu không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (userDAO.registerUser(username, password, role)) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            backToLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void backToLogin() {
        SwingUtilities.invokeLater(() -> {
            new LoginGUI().setVisible(true);
        });
        dispose();
    }
}