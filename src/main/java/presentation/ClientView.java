package presentation;

import model.Client;
import service.ClientService;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
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
    private JTextField idTextField;
    private JTextField nameTextField;
    private JTextField emailTextField;
    private JButton clearFieldsButton;

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
        clientsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(clientsPanel);
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

    public void addClientsTableListener(ListSelectionListener listSelectionListener) {
        clientsTable.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public String getIdTextField() {
        return idTextField.getText();
    }

    public String getNameTextField() {
        return nameTextField.getText();
    }

    public String getEmailTextField() {
        return emailTextField.getText();
    }

    public JTable getClientsTable() {
        return clientsTable;
    }

    public void setIdTextField(String id) {
        idTextField.setText(id);
    }

    public void setNameTextField(String name) {
        nameTextField.setText(name);
    }

    public void setEmailTextField(String email) {
        emailTextField.setText(email);
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
