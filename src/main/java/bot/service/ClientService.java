package bot.service;

import bot.data.Exchange;
import bot.data.entity.Client;

import java.util.List;

public interface ClientService {
    void save(Client client);
    void saveAll(List<Client> client);
    boolean deleteByExchangeAndId(Exchange exchange, String id);
    Client findById(String id);
    List<Client> findAll();
}
