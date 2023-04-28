package dao;

import model.Order;

public class OrderDAO extends AbstractDAO<Order> {

    @Override
    public void create(Order order) {
        // TODO: implement this. When adding an order into the database, you should also add the order's products
        //  into the order_product table.
    }

    @Override
    public void updateById(Order order) {
        // TODO: implement this. When updating an order, you should also update the order's products in the
        //  order_product table.
    }
}
