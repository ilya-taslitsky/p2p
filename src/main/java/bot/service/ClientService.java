package bot.service;

import bot.data.entity.Client;

import java.util.List;

public interface ClientService {
    void save(Client client);
    void saveAll(List<Client> client);
    void deleteById(String id);
    Client findById(String id);
    List<Client> findAll();
}
