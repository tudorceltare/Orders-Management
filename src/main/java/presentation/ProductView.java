package presentation;

import model.Product;
import service.ProductService;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductView extends JFrame {
    private JLabel productsLabel;
    private JTable productsTable;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton addButton;
    private JButton backButton;
    private JPanel productsPanel;
    private JScrollPane tableScrollPane;
    private JTextField nameTextField;
    private JTextField priceTextField;
    private JTextField quantityTextField;
    private JTextField idTextField;
    private JButton clearFieldsButton;

    private ProductService productService;
    public ProductView() {
        super.setTitle("Inventory");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        productService = new ProductService();
        List<Product> products = productService.findAll();
        ProductTableModel productTableModel = new ProductTableModel(products);
        productsTable.setModel(productTableModel);
        productsTable.setAutoCreateRowSorter(true);
        productsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        idTextField.setEnabled(false);
        add(productsPanel);
    }

    public void addBackButtonListener(ActionListener actionListener) {
        backButton.addActionListener(actionListener);
    }

    public void addAddButtonListener(ActionListener actionListener) {
        addButton.addActionListener(actionListener);
    }

    public void addUpdateButtonListener(ActionListener actionListener) {
        updateButton.addActionListener(actionListener);
    }

    public void addDeleteButtonListener(ActionListener actionListener) {
        deleteButton.addActionListener(actionListener);
    }

    public void addClearFieldsButtonListener(ActionListener actionListener) {
        clearFieldsButton.addActionListener(actionListener);
    }

    public void addProductsTableListener(ListSelectionListener listSelectionListener) {
        productsTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public String getIdTextField() {
        return idTextField.getText();
    }

    public String getNameTextField() {
        return nameTextField.getText();
    }

    public String getPriceTextField() {
        return priceTextField.getText();
    }

    public String getQuantityTextField() {
        return quantityTextField.getText();
    }

    public JTable getProductsTable() {
        return productsTable;
    }

    public void setIdTextField(String id) {
        idTextField.setText(id);
    }

    public void setNameTextField(String name) {
        nameTextField.setText(name);
    }

    public void setPriceTextField(String price) {
        priceTextField.setText(price);
    }

    public void setQuantityTextField(String quantity) {
        quantityTextField.setText(quantity);
    }


    private static class ProductTableModel extends AbstractTableModel {
        private final List<Product> products;
        private final String[] columnNames = {"ID", "Name", "Price", "Quantity"};

        public ProductTableModel(List<Product> products) {
            this.products = products;
        }

        @Override
        public int getRowCount() {
            return products.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Product product = products.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return product.getId();
                case 1:
                    return product.getName();
                case 2:
                    return product.getPrice();
                case 3:
                    return product.getQuantity();
                default:
                    return null;
            }
        }
    }
}
