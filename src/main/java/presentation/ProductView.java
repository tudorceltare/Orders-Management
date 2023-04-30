package presentation;

import dao.ProductDAO;
import model.Product;
import service.ProductService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
        add(productsPanel);
    }

    public void addBackButtonListener(ActionListener actionListener) {
        backButton.addActionListener(actionListener);
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
