package service;

import dao.LogDAO;
import dao.OrderDAO;
import model.Bill;
import model.Order;
import model.Product;

import java.util.List;

public class OrderService {
    private OrderDAO orderDAO;
    private LogDAO logDAO;
    private ProductService productService;
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.logDAO = new LogDAO();
        this.productService = new ProductService();
    }

    public List<Order> findAll() {
        try {
            return orderDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order findById(int id) {
        try {
            return orderDAO.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Order order) {
        // decrement quantity of products from stock
        try {
            this.checkIfOrderIsValid(order);
            for (Product product : order.getProducts()) {
                Product productFromStock = productService.findById(product.getId());
                productFromStock.setQuantity(productFromStock.getQuantity() - product.getQuantity());
                productService.updateById(productFromStock);
            }
            orderDAO.create(order);
            String message = this.generateBill(order);
            Bill bill = new Bill(0, message, null);
            this.saveBill(bill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateById(Order order) {
        try {
            orderDAO.updateById(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            orderDAO.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfValidQuantityOfProduct(Product selectedProduct, int quantity) {
        Product product = productService.findById(selectedProduct.getId());
        if (product.getQuantity() == 0 || quantity <= 0) {
            return false;
        }
        return product.getQuantity() >= quantity;
    }

    public String generateBill(Order order) {
        StringBuilder sb = new StringBuilder();
        sb.append("=========================================\n");
        sb.append("                 BILL                    \n");
        sb.append("=========================================\n");
        sb.append("CLIENT NAME: ").append(order.getClient().getName()).append("\n\n");
        sb.append("PRODUCTS:\n");
        sb.append(String.format("%-20s %-10s %-10s %-10s\n", "Name", "Price", "Quantity", "Subtotal"));
        sb.append("--------------------------------------------------\n");
        for (Product product : order.getProducts()) {
            sb.append(String.format("%-20s $%-9.2f %-10d $%-9.2f\n", product.getName(), product.getPrice(), product.getQuantity(), product.getPrice() * product.getQuantity()));
        }
        sb.append("\n");
        sb.append(String.format("%-40s $%-9.2f", "Total:", order.getTotalPrice()));
        return sb.toString();
    }

    public void saveBill(Bill bill) {
        this.checkIfBillIsValid(bill);
        try {
            logDAO.create(bill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfNewOrderAlreadyContainsSelectedProduct(Order order, Product selectedProduct) {
        for (Product product : order.getProducts()) {
            if (product.getId() == selectedProduct.getId()) {
                return true;
            }
        }
        return false;
    }

    private void checkIfOrderIsValid(Order order) throws IllegalArgumentException {
        if (order.getClient() == null) {
            throw new IllegalArgumentException("Client cannot be null");
        }
        if (order.getProducts() == null || order.getProducts().size() == 0) {
            throw new IllegalArgumentException("Products cannot be empty");
        }
        if (order.getTotalPrice() < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
    }

    private void checkIfBillIsValid(Bill bill) throws IllegalArgumentException {
        if (bill.message() == null || bill.message().isEmpty()) {
            throw new IllegalArgumentException("Message cannot be empty");
        }
    }
}
