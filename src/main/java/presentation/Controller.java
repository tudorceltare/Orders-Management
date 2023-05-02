package presentation;

import model.Client;
import model.Product;
import service.ClientService;
import service.OrderService;
import service.ProductService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private ProductService productService;
    private ClientService clientService;
    private OrderService orderService;
    private DashboardView dashboardView;
    private ClientView clientView;
    private ProductView productView;
    private OrderView orderView;

    public Controller() {
        this.dashboardView = initDashboardView();
        this.dashboardView.setVisible(true);

        this.productService = new ProductService();
        this.clientService = new ClientService();
        this.orderService = new OrderService();
    }

    private DashboardView initDashboardView() {
        DashboardView dashboardView = new DashboardView();
        dashboardView.addClientsButtonListener(new ClientsButtonListener());
        dashboardView.addProductsButtonListener(new ProductsButtonListener());
        dashboardView.addOrdersButtonListener(new OrdersButtonListener());
        return dashboardView;
    }

    private ClientView initClientView() {
        ClientView clientView = new ClientView();
        clientView.addBackButtonListener(new BackButtonListener());
        clientView.addAddButtonListener(new ClientsAddButtonListener());
        clientView.addUpdateButtonListener(new ClientsUpdateButtonListener());
        clientView.addDeleteButtonListener(new ClientsDeleteButtonListener());
        clientView.addClearFieldsButtonListener(new ClientsClearFieldsButtonListener());
        clientView.addClientsTableListener(new ClientsTableListener());
        return clientView;
    }

    private ProductView initProductView() {
        ProductView productView = new ProductView();
        productView.addBackButtonListener(new BackButtonListener());
        productView.addAddButtonListener(new ProductsAddButtonListener());
        productView.addUpdateButtonListener(new ProductsUpdateButtonListener());
        productView.addDeleteButtonListener(new ProductsDeleteButtonListener());
        productView.addClearFieldsButtonListener(new ProductsClearFieldsButtonListener());
        productView.addProductsTableListener(new ProductsTableListener());
        return productView;
    }

    class ClientsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientView = initClientView();
            dashboardView.setVisible(false);
            clientView.setVisible(true);
        }
    }

    class ProductsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            productView = initProductView();
            dashboardView.setVisible(false);
            productView.setVisible(true);
        }
    }

    class OrdersButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            orderView = new OrderView();
            orderView.addBackButtonListener(new BackButtonListener());
            dashboardView.setVisible(false);
            orderView.setVisible(true);
        }
    }

    class BackButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dashboardView.setVisible(true);
            if (clientView != null) {
                clientView.setVisible(false);
            }
            if (productView != null) {
                productView.setVisible(false);
            }
            if (orderView != null) {
                orderView.setVisible(false);
            }
        }
    }

    class ProductsAddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = productView.getNameTextField();
            String price = productView.getPriceTextField();
            String quantity = productView.getQuantityTextField();
            if (name.equals("") || price.equals("") || quantity.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields!");
            } else {
                try {
                    Product product = Product.builder()
                            .name(name)
                            .price(Float.parseFloat(price))
                            .quantity(Integer.parseInt(quantity))
                            .build();
                    productService.create(product);
                    JOptionPane.showMessageDialog(null, "Product added successfully!");
                    productView.setVisible(false);
                    productView = initProductView();
                    productView.setVisible(true);
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        }
    }

    class ProductsUpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = productView.getNameTextField();
            String price = productView.getPriceTextField();
            String quantity = productView.getQuantityTextField();
            String id = productView.getIdTextField();
            if (name.equals("") || price.equals("") || quantity.equals("") || id.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields!");
            } else {
                try {
                    Product product = Product.builder()
                            .id(Integer.parseInt(id))
                            .name(name)
                            .price(Float.parseFloat(price))
                            .quantity(Integer.parseInt(quantity))
                            .build();
                    productService.updateById(product);
                    JOptionPane.showMessageDialog(null, "Product updated successfully!");
                    productView.setVisible(false);
                    productView = initProductView();
                    productView.setVisible(true);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid price, quantity and id!");
                }
            }
        }
    }

    class ProductsDeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = productView.getIdTextField();
            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "Please select a product!");
            } else {
                try {
                    productService.deleteById(Integer.parseInt(id));
                    JOptionPane.showMessageDialog(null, "Product deleted successfully!");
                    productView.setVisible(false);
                    productView = initProductView();
                    productView.setVisible(true);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid id!");
                }
            }
        }
    }

    class ProductsClearFieldsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            productView.setIdTextField(null);
            productView.setNameTextField(null);
            productView.setPriceTextField(null);
            productView.setQuantityTextField(null);
        }
    }

    class ProductsTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()) {
                int row = productView.getProductsTable().getSelectedRow();
                productView.setIdTextField(productView.getProductsTable().getValueAt(row, 0).toString());
                productView.setNameTextField(productView.getProductsTable().getValueAt(row, 1).toString());
                productView.setPriceTextField(productView.getProductsTable().getValueAt(row, 2).toString());
                productView.setQuantityTextField(productView.getProductsTable().getValueAt(row, 3).toString());
            }
        }
    }

    class ClientsAddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = clientView.getNameTextField();
            String email = clientView.getEmailTextField();
            if (name.equals("") || email.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields!");
            } else {
                try {
                    Client client = Client.builder()
                            .name(name)
                            .email(email)
                            .build();
                    clientService.create(client);
                    JOptionPane.showMessageDialog(null, "Client added successfully!");
                    clientView.setVisible(false);
                    clientView = initClientView();
                    clientView.setVisible(true);
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        }
    }

    class ClientsUpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = clientView.getNameTextField();
            String email = clientView.getEmailTextField();
            String id = clientView.getIdTextField();
            if (name.equals("") || email.equals("") || id.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields!");
            } else {
                try {
                    Client client = Client.builder()
                            .id(Integer.parseInt(id))
                            .name(name)
                            .email(email)
                            .build();
                    clientService.updateById(client);
                    JOptionPane.showMessageDialog(null, "Client updated successfully!");
                    clientView.setVisible(false);
                    clientView = initClientView();
                    clientView.setVisible(true);
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        }
    }

    class ClientsDeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = clientView.getIdTextField();
            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "Please select a client!");
            } else {
                try {
                    clientService.deleteById(Integer.parseInt(id));
                    JOptionPane.showMessageDialog(null, "Client deleted successfully!");
                    clientView.setVisible(false);
                    clientView = initClientView();
                    clientView.setVisible(true);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid id!");
                }
            }
        }
    }

    class ClientsClearFieldsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientView.setIdTextField(null);
            clientView.setNameTextField(null);
            clientView.setEmailTextField(null);
        }
    }

    class ClientsTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()) {
                int row = clientView.getClientsTable().getSelectedRow();
                clientView.setIdTextField(clientView.getClientsTable().getValueAt(row, 0).toString());
                clientView.setNameTextField(clientView.getClientsTable().getValueAt(row, 1).toString());
                clientView.setEmailTextField(clientView.getClientsTable().getValueAt(row, 2).toString());
            }
        }
    }
}
