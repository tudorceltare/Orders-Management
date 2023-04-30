package service;

import dao.ProductDAO;
import model.Product;

import java.util.List;

public class ProductService {
    private ProductDAO productDAO;
    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public List<Product> findAll() {
        try {
            return productDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Product findById(int id) {
        try {
            return productDAO.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Product product) {
        try {
            productDAO.create(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateById(Product product) {
        try {
            productDAO.updateById(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            productDAO.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
