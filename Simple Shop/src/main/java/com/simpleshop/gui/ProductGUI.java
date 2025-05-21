package com.simpleshop.gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.simpleshop.dao.ProductDao;
import com.simpleshop.model.Product;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProductGUI extends JFrame {
    private ProductDao productDAO;
    private JTextField idField, nameField, priceField, quantityField, searchField;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private String userRole;

    public ProductGUI(String userRole) {
        this.userRole = userRole;
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
        productDAO = new ProductDao();
        initComponents();
    }

    private void initComponents() {
        setTitle("Simple Shop Manager - " + userRole);
        setSize(800, 600);
        setMinimumSize(new Dimension(600, 400));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Font labelFont = new Font("Segoe UI", Font.BOLD, 14);
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        inputPanel.setBackground(new Color(30, 30, 30));

        JLabel idLabel = new JLabel("Mã sản phẩm:");
        idLabel.setFont(labelFont);
        idLabel.setForeground(Color.WHITE);
        idField = new JTextField();
        idField.setFont(inputFont);
        inputPanel.add(idLabel);
        inputPanel.add(idField);

        JLabel nameLabel = new JLabel("Tên sản phẩm:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(Color.WHITE);
        nameField = new JTextField();
        nameField.setFont(inputFont);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        JLabel priceLabel = new JLabel("Giá cả:");
        priceLabel.setFont(labelFont);
        priceLabel.setForeground(Color.WHITE);
        priceField = new JTextField();
        priceField.setFont(inputFont);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);

        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityLabel.setFont(labelFont);
        quantityLabel.setForeground(Color.WHITE);
        quantityField = new JTextField();
        quantityField.setFont(inputFont);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);

        JLabel searchLabel = new JLabel("Tìm kiếm (Mã/Tên sản phẩm):");
        searchLabel.setFont(labelFont);
        searchLabel.setForeground(Color.WHITE);
        searchField = new JTextField();
        searchField.setFont(inputFont);
        inputPanel.add(searchLabel);
        inputPanel.add(searchField);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(new Color(20, 20, 20));

        JButton addButton = createStyledButton("Thêm sản phẩm");
        JButton updateButton = createStyledButton("Cập nhật");
        JButton deleteButton = createStyledButton("Xóa sản phẩm");
        JButton clearButton = createStyledButton("Dọn sạch");
        JButton searchButton = createStyledButton("Tìm kiếm");
        JButton sortByNameButton = createStyledButton("Sắp xếp theo tên");
        JButton sortByPriceButton = createStyledButton("Sắp xếp theo giá cả");
        JButton logoutButton = createStyledButton("Đăng xuất");
        logoutButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new LoginGUI().setVisible(true));
        });
        buttonPanel.add(logoutButton);

        // Role-based access control
        if (userRole.equals("Admin")) {
            buttonPanel.add(addButton);
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(sortByNameButton);
            buttonPanel.add(sortByPriceButton);
        } else {
            idField.setEditable(false);
            nameField.setEditable(false);
            priceField.setEditable(false);
            quantityField.setEditable(false);
        }
        buttonPanel.add(clearButton);
        buttonPanel.add(searchButton);

        // Table Panel
        String[] columns = {"Mã", "Tên sản phẩm", "Giá cả", "Số lượng"};
        tableModel = new DefaultTableModel(columns, 0);
        productTable = new JTable(tableModel);
        productTable.setRowHeight(30);
        productTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        productTable.setGridColor(new Color(50, 40, 60));
        productTable.setShowGrid(true);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to frame
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        if (userRole.equals("Admin")) {
            addButton.addActionListener(e -> addProduct());
            updateButton.addActionListener(e -> updateProduct());
            deleteButton.addActionListener(e -> deleteProduct());
            sortByNameButton.addActionListener(e -> sortByName());
            sortByPriceButton.addActionListener(e -> sortByPrice());
        }
        searchButton.addActionListener(e -> searchProducts());
        clearButton.addActionListener(e -> clearFields());

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && productTable.getSelectedRow() >= 0) {
                String id = (String) tableModel.getValueAt(productTable.getSelectedRow(), 0);
                Product product = productDAO.getProductById(id);
                if (product != null) {
                    idField.setText(product.getId());
                    nameField.setText(product.getName());
                    priceField.setText(String.valueOf(product.getPrice()));
                    quantityField.setText(String.valueOf(product.getQuantity()));
                }
            }
        });

        updateTable();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        

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

    private void addProduct() {
        if (!validateInput()) return;

        String id = idField.getText();
        if (productDAO.isIdExists(id)) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm đã tồn tại!", "Lỗi!", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Giá cả và số lượng sản phẩm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = new Product(id, name, price, quantity);
            productDAO.addProduct(product);
            updateTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá cả và số lượng sản phẩm không hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        if (!validateInput()) return;

        try {
            String id = idField.getText();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            int quantity = Integer.parseInt(quantityField.getText());

            if (price < 0 || quantity < 0) {
                JOptionPane.showMessageDialog(this, "Giá cả và số lượng hợp lệ!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Product product = new Product(id, name, price, quantity);
            productDAO.updateProduct(product);
            updateTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Giá cả và số lượng hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteProduct() {
        String id = idField.getText();
        if (productDAO.getProductById(id) != null) {
            productDAO.deleteProduct(id);
            updateTable();
            clearFields();
            JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchProducts() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);
        java.util.List<Product> results = keyword.isEmpty() ? productDAO.getAllProducts() : productDAO.searchProducts(keyword);
        for (Product product : results) {
            Object[] row = {product.getId(), product.getName(), product.getPrice(), product.getQuantity()};
            tableModel.addRow(row);
        }
    }

    private void sortByName() {
        productDAO.sortByName();
        updateTable();
    }

    private void sortByPrice() {
        productDAO.sortByPrice();
        updateTable();
    }

    private boolean validateInput() {
        if (idField.getText().isEmpty() || nameField.getText().isEmpty() ||
            priceField.getText().isEmpty() || quantityField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền thông tin!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        searchField.setText("");
        updateTable();
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (Product product : productDAO.getAllProducts()) {
            Object[] row = {product.getId(), product.getName(), product.getPrice(), product.getQuantity()};
            tableModel.addRow(row);
        }
    }
}