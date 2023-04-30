package presentation;

import model.Order;
import service.OrderService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderView extends JFrame {
    private OrderService orderService;
    private JTable ordersTable;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton addButton;
    private JLabel orderLabel;
    private JButton backButton;
    private JPanel ordersPanel;

    public OrderView() {
        super.setTitle("Orders");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        orderService = new OrderService();
        List<Order> orders = orderService.findAll();
        OrderTableModel orderTableModel = new OrderTableModel(orders);
        ordersTable.setModel(orderTableModel);
        ordersTable.setAutoCreateRowSorter(true);
        add(ordersPanel);
    }

    public void addBackButtonListener(ActionListener actionListener) {
        backButton.addActionListener(actionListener);
    }

    public static class OrderTableModel extends AbstractTableModel {
        private final String[] columnNames = {"ID", "Client ID", "Total Price"};
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
                    return order.getClientId();
                case 2:
                    return order.getTotalPrice();
                default:
                    return null;
            }
        }
    }
}
