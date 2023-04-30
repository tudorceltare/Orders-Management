package presentation;

import model.Client;
import service.ClientService;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientView extends JFrame {
    private ClientService clientService;
    private JPanel clientsPanel;
    private JLabel clientTitle;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
    private JButton backButton;
    private JTable clientsTable;
    private JScrollPane tableScrollPane;

    public ClientView() {
        super.setTitle("Clients");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        clientService = new ClientService();
        List<Client> clients = clientService.findAll();
        ClientTableModel clientTableModel = new ClientTableModel(clients);
        clientsTable.setModel(clientTableModel);
        clientsTable.setAutoCreateRowSorter(true);
        add(clientsPanel);
    }

    public void addBackButtonListener(ActionListener actionListener) {
        backButton.addActionListener(actionListener);
    }

    private static class ClientTableModel extends AbstractTableModel {
        private final List<Client> clients;
        private final String[] columnNames = {"ID", "Name", "Email"};

        public ClientTableModel(List<Client> clients) {
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
