package service;

import dao.ClientDAO;
import model.Client;

import java.util.List;

public class ClientService {
    private ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public List<Client> findAll() {
        try {
            return clientDAO.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Client findById(int id) {
        try {
            return clientDAO.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void create(Client client) {
        try {
            clientDAO.create(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateById(Client client) {
        try {
            clientDAO.updateById(client);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteById(int id) {
        try {
            clientDAO.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
