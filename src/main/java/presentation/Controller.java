package presentation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private DashboardView dashboardView;
    private ClientView clientView;
    private ProductView productView;
    private OrderView orderView;

    public Controller() {
        this.dashboardView = new DashboardView();
        this.dashboardView.addClientsButtonListener(new ClientsButtonListener());
        this.dashboardView.addProductsButtonListener(new ProductsButtonListener());
        this.dashboardView.addOrdersButtonListener(new OrdersButtonListener());

        this.dashboardView.setVisible(true);
    }

    class ClientsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            clientView = new ClientView();
            clientView.addBackButtonListener(new BackButtonListener());
            dashboardView.setVisible(false);
            clientView.setVisible(true);
        }
    }

    class ProductsButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            productView = new ProductView();
            productView.addBackButtonListener(new BackButtonListener());
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
}
