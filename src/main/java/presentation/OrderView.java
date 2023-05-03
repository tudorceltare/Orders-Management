package presentation;

import model.Client;
import model.Order;
import model.Product;
import service.ClientService;
import service.OrderService;
import service.ProductService;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class OrderView extends JFrame {
    private OrderService orderService;
    private ClientService clientService;
    private ProductService productService;
    private JTable ordersTable;
    private JButton backButton;
    private JPanel ordersPanel;
    private JTabbedPane tabbedPane;
    private JTable orderProductsTable;
    private JButton deleteButton;
    private JTextArea billTextArea;
    private JButton generateBillButton;
    private JPanel viewPanel;
    private JPanel addPanel;
    private JTextField groupIdField;
    private JTable clientsTable;
    private JTable allProductsTable;
    private JTextField clientNameTextField;
    private JTable newOrderProductsTable;
    private JTextField productIdTextField;
    private JTextField priceTextField;
    private JTextField quantityTextField;
    private JButton addProductButton;
    private JButton createOrderButton;
    private JTextField productNameTextField;
    private JTextField totalTextField;
    private JButton removeProductButton;

    private Order selectedOrder;
    private Order newOrder;
    private Product selectedNewProduct;
    private Product selectedProductInOrder;
    private Client selectedClient;

    public OrderView() {
        super.setTitle("Orders");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);

        orderService = new OrderService();
        clientService = new ClientService();
        productService = new ProductService();

        this.selectedClient = null;
        this.selectedOrder = null;
        this.newOrder = Order.builder()
                .totalPrice(0.0)
                .products(new ArrayList<Product>())
                .build();
        this.selectedNewProduct = null;
        this.selectedProductInOrder = null;
        List<Order> orders = orderService.findAll();
        OrderTableModel orderTableModel = new OrderTableModel(orders);
        ordersTable.setModel(orderTableModel);
        ordersTable.setAutoCreateRowSorter(true);
        ordersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        List<Client> clients = clientService.findAll();
        ClientsTableModel clientsTableModel = new ClientsTableModel(clients);
        clientsTable.setModel(clientsTableModel);
        clientsTable.setAutoCreateRowSorter(true);
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        List<Product> products = productService.findAll();
        OrderProductsTableModel orderProductsTableModel = new OrderProductsTableModel(products);
        allProductsTable.setModel(orderProductsTableModel);
        allProductsTable.setAutoCreateRowSorter(true);
        allProductsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(ordersPanel);
    }

    public void setSelectedProductInOrder(Product product) {
        this.selectedProductInOrder = product;
    }

    public void addBackButtonListener(ActionListener actionListener) {
        backButton.addActionListener(actionListener);
    }

    public void addDeleteButtonListener(ActionListener actionListener) {
        deleteButton.addActionListener(actionListener);
    }

    public void addGenerateBillButtonListener(ActionListener actionListener) {
        generateBillButton.addActionListener(actionListener);
    }

    public void addOrdersTableListener(ListSelectionListener listSelectionListener) {
        ordersTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void setBillTextArea(String text) {
        billTextArea.setText(text);
    }

    public void setGroupIdField(String text) {
        groupIdField.setText(text);
    }

    public String getGroupIdField() {
        return groupIdField.getText();
    }

    public JTable getOrdersTable() {
        return ordersTable;
    }

    public void setOrderProductsTableModel(OrderProductsTableModel orderProductsTableModel) {
        orderProductsTable.setModel(orderProductsTableModel);
    }

    public void setSelectedOrder(Order selectedOrder) {
        this.selectedOrder = selectedOrder;
    }

    public JTable getClientsTable() {
        return clientsTable;
    }

    public void addClientsTableListener(ListSelectionListener listSelectionListener) {
        clientsTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public JTable getAllProductsTable() {
        return allProductsTable;
    }

    public void addAllProductsTableListener(ListSelectionListener listSelectionListener) {
        allProductsTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public JTable getNewOrderProductsTable() {
        return newOrderProductsTable;
    }

    public void setNewOrderProductsTableModel(OrderProductsTableModel orderProductsTableModel) {
        newOrderProductsTable.setModel(orderProductsTableModel);
    }

    public void addNewOrderProductsTableListener(ListSelectionListener listSelectionListener) {
        newOrderProductsTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void addAddProductButtonListener(ActionListener actionListener) {
        addProductButton.addActionListener(actionListener);
    }

    public void setClientNameTextField(String text) {
        clientNameTextField.setText(text);
    }

    public Product getSelectedNewProduct() {
        return selectedNewProduct;
    }

    public void setSelectedNewProduct(Product selectedNewProduct) {
        this.selectedNewProduct = selectedNewProduct;
    }

    public void setProductIdTextField(String text) {
        productIdTextField.setText(text);
    }

    public void setProductNameTextField(String text) {
        productNameTextField.setText(text);
    }

    public void setPriceTextField(String text) {
        priceTextField.setText(text);
    }

    public String getQuantityTextField() {
        return quantityTextField.getText();
    }

    public void setTotalTextField(String text) {
        totalTextField.setText(text);
    }

    public Order getNewOrder() {
        return newOrder;
    }

    public void addCreateOrderButtonListener(ActionListener actionListener) {
        createOrderButton.addActionListener(actionListener);
    }

    public void addRemoveProductButtonListener(ActionListener actionListener) {
        removeProductButton.addActionListener(actionListener);
    }

    public Product getSelectedProductInOrder() {
        return selectedProductInOrder;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public static class OrderTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Client ID", "Client Name", "Total Price"};
        private final List<Order> orders;

        public OrderTableModel(List<Order> orders) {
            this.orders = orders;
        }

        @Override
        public int getRowCount() {
            return orders.size();
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
            Order order = orders.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return order.getId();
                case 1:
                    return order.getClient().getId();
                case 2:
                    return order.getClient().getName();
                case 3:
                    return order.getTotalPrice();
                default:
                    return null;
            }
        }
    }

    public static class OrderProductsTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Name", "Price", "Quantity"};
        private final List<Product> products;

        public OrderProductsTableModel(List<Product> products) {
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

    public static class ClientsTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Name", "Email"};
        private final List<Client> clients;

        public ClientsTableModel(List<Client> clients) {
            this.clients = clients;
        }

        @Override
        public int getRowCount() {
            return clients.size();
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
            Client client = clients.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return client.getId();
                case 1:
                    return client.getName();
                case 2:
                    return client.getEmail();
                default:
                    return null;
            }
        }
    }
}
