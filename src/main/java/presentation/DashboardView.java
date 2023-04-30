package presentation;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardView extends JFrame {
    private JButton clientsButton;
    private JButton productsButton;
    private JButton ordersButton;
    private JLabel title;
    private JPanel dashboardPanel;

    public DashboardView() {
        super.setTitle("Dashboard");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        add(dashboardPanel);
    }
    public void addClientsButtonListener(ActionListener actionListener) {
        clientsButton.addActionListener(actionListener);
    }

    public void addProductsButtonListener(ActionListener actionListener) {
        productsButton.addActionListener(actionListener);
    }

    public void addOrdersButtonListener(ActionListener actionListener) {
        ordersButton.addActionListener(actionListener);
    }

    public void displayErrorMessage(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage);
    }

}
