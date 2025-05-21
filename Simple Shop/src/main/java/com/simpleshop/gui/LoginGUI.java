package com.simpleshop.gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.simpleshop.dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class LoginGUI extends JFrame {
    private UserDAO userDAO;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static final Color MAIN_PANEL_BG = new Color(0x1E1E1E);
    private static final Color BUTTON_PANEL_BG = new Color(0x2A2A2A);
    private static final Color LABEL_FG = new Color(0xFFFFFF);
    private static final Color INPUT_FG = new Color(0xE0E0E0);
    private static final Color BUTTON_BG = new Color(0x0288D1);
    private static final Color BUTTON_HOVER_BG = new Color(0x4FC3F7);

    public LoginGUI() {
        userDAO = new UserDAO();
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

        setTitle("Đăng nhập - Simple Shop Manager");
        setSize(900, 600);
        getContentPane().setBackground(Color.BLUE);
     
        setMinimumSize(new Dimension(300, 200));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set custom font
        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Main panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(MAIN_PANEL_BG);

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

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        JButton loginButton = createStyledButton("Đăng nhập");
        JButton registerButton = createStyledButton("Đăng ký");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add components to frame
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> openRegisterGUI());
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

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDAO.User user = userDAO.authenticate(username, password);
        if (user != null) {
            SwingUtilities.invokeLater(() -> {
                new ProductGUI(user.getRole()).setVisible(true);
            });
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Tài khoản hoặc mật khẩu không đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegisterGUI() {
        SwingUtilities.invokeLater(() -> {
            new RegisterGUI(userDAO).setVisible(true);
        });
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
    }
}