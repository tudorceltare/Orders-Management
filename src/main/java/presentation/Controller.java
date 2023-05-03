package presentation;

import model.Client;
import model.Order;
import model.Product;
import service.ClientService;
import service.OrderService;
import service.ProductService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

    private OrderView initOrderView() {
        OrderView orderView = new OrderView();
        orderView.addBackButtonListener(new BackButtonListener());
        orderView.addDeleteButtonListener(new OrdersDeleteButtonListener());
        orderView.addOrdersTableListener(new OrdersTableListener());
        orderView.addClientsTableListener(new OrdersClientsTableListener());
        orderView.addAllProductsTableListener(new OrdersAllProductsTableListener());
        orderView.addAddProductButtonListener(new OrdersAddProductButtonListener());
        orderView.addNewOrderProductsTableListener(new OrdersNewOrderProductsTableListener());
        orderView.addRemoveProductButtonListener(new OrdersRemoveProductButtonListener());
        orderView.addCreateOrderButtonListener(new OrdersCreateOrderButtonListener());
        return orderView;
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
            orderView = initOrderView();
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

    class OrdersDeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String id = orderView.getGroupIdField();
            if (id.equals("")) {
                JOptionPane.showMessageDialog(null, "Please select an order!");
            } else {
                try {
                    orderService.deleteById(Integer.parseInt(id));
                    JOptionPane.showMessageDialog(null, "Order deleted successfully!");
                    orderView.setVisible(false);
                    orderView = initOrderView();
                    orderView.setVisible(true);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid id!");
                }
            }
        }
    }

    class OrdersTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()) {
                int row = orderView.getOrdersTable().getSelectedRow();
                orderView.setGroupIdField(orderView.getOrdersTable().getValueAt(row, 0).toString());
                Order selectedOrder = orderService.findById(Integer.parseInt(orderView.getGroupIdField()));
                orderView.setSelectedOrder(selectedOrder);
                orderView.setOrderProductsTableModel(new OrderView.OrderProductsTableModel(selectedOrder.getProducts()));
            }
        }
    }

    class OrdersClientsTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()) {
                int row = orderView.getClientsTable().getSelectedRow();
                orderView.setSelectedClient(clientService.findById(Integer.parseInt(orderView.getClientsTable().getValueAt(row, 0).toString())));
                orderView.setClientNameTextField(orderView.getClientsTable().getValueAt(row, 1).toString());
            }
        }
    }

    class OrdersAllProductsTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()) {
                int row = orderView.getAllProductsTable().getSelectedRow();
                Product selectedProduct = productService.findById(Integer.parseInt(orderView.getAllProductsTable().getValueAt(row, 0).toString()));
                orderView.setSelectedNewProduct(selectedProduct);
                orderView.setProductIdTextField(String.valueOf(selectedProduct.getId()));
                orderView.setProductNameTextField(selectedProduct.getName());
                orderView.setPriceTextField(String.valueOf(selectedProduct.getPrice()));
            }
        }
    }

    class OrdersAddProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Product selectedProduct = orderView.getSelectedNewProduct();
            String quantity = orderView.getQuantityTextField();
            if (quantity.equals("")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields!");
            } else if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Please select a product!");
            } else {
                try {
                    if (orderService.checkIfValidQuantityOfProduct(selectedProduct, Integer.parseInt(quantity))) {
                        selectedProduct.setQuantity(Integer.parseInt(quantity));
                        if (orderService.checkIfNewOrderAlreadyContainsSelectedProduct(orderView.getNewOrder(), selectedProduct)) {
                            for(Product product : orderView.getNewOrder().getProducts()) {
                                if(product.getId() == selectedProduct.getId()) {
                                    product.setQuantity(selectedProduct.getQuantity());
                                    break;
                                }
                            }
                        } else {
                            orderView.getNewOrder().getProducts().add(selectedProduct);
                        }
                        this.updateOrderTotalPrice(orderView.getNewOrder());
                        orderView.setNewOrderProductsTableModel(new OrderView.OrderProductsTableModel(orderView.getNewOrder().getProducts()));
                        orderView.setTotalTextField(String.valueOf(orderView.getNewOrder().getTotalPrice()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Not enough products in stock!");
                    }
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        }

        private void updateOrderTotalPrice(Order order) {
            order.setTotalPrice(0);
            for(Product product : order.getProducts()) {
                order.setTotalPrice(order.getTotalPrice() + product.getPrice() * product.getQuantity());
            }
        }
    }

    class OrdersNewOrderProductsTableListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if(e.getValueIsAdjusting()) {
                int row = orderView.getNewOrderProductsTable().getSelectedRow();
                int columnNumber = orderView.getNewOrderProductsTable().getColumnCount();
                Product selectedProductInNewOrder = null;
                if(row >= 0) {
                    selectedProductInNewOrder = Product.builder()
                            .id(Integer.parseInt(orderView.getNewOrderProductsTable().getValueAt(row, 0).toString()))
                            .name(orderView.getNewOrderProductsTable().getValueAt(row, 1).toString())
                            .price(Float.parseFloat(orderView.getNewOrderProductsTable().getValueAt(row, 2).toString()))
                            .quantity(Integer.parseInt(orderView.getNewOrderProductsTable().getValueAt(row, 3).toString()))
                            .build();
                }

                orderView.setSelectedProductInOrder(selectedProductInNewOrder);
            }
        }
    }

    class OrdersRemoveProductButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Product selectedProduct = orderView.getSelectedProductInOrder();
            if (selectedProduct == null) {
                JOptionPane.showMessageDialog(null, "Please select a product!");
            } else {
                try {
                    this.removeProductFromOrder(selectedProduct);
                    this.updateOrderTotalPrice(orderView.getNewOrder());
                    orderView.setNewOrderProductsTableModel(new OrderView.OrderProductsTableModel(orderView.getNewOrder().getProducts()));
                    orderView.setTotalTextField(String.valueOf(orderView.getNewOrder().getTotalPrice()));
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        }

        private void removeProductFromOrder(Product product) {
            for(Product product1 : orderView.getNewOrder().getProducts()) {
                if(product1.getId() == product.getId()) {
                    orderView.getNewOrder().getProducts().remove(product1);
                    break;
                }
            }
        }

        private void updateOrderTotalPrice(Order order) {
            order.setTotalPrice(0);
            for(Product product : order.getProducts()) {
                order.setTotalPrice(order.getTotalPrice() + product.getPrice() * product.getQuantity());
            }
        }
    }

    class OrdersCreateOrderButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Client selectedClient = orderView.getSelectedClient();
            List<Product> selectedProducts = orderView.getNewOrder().getProducts();
            if(selectedProducts.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please add at least one product in the order!");
            } else if (selectedClient == null) {
                JOptionPane.showMessageDialog(null, "Please select a client!");
            } else {
                try {
                    Order order = orderView.getNewOrder();
                    order.setClient(selectedClient);
                    orderService.create(order);
                    JOptionPane.showMessageDialog(null, "Order created successfully!");
                    orderView.setVisible(false);
                    orderView = initOrderView();
                    orderView.setVisible(true);
                } catch (IllegalArgumentException exception) {
                    JOptionPane.showMessageDialog(null, exception.getMessage());
                }
            }
        }
    }
}
