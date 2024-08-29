package bot.service;

import bot.data.Exchange;
import bot.data.entity.Client;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientService {
    Client findByIdAndExchange(String id, Exchange exchange);
    void save(Client client);
    void saveAll(List<Client> client);
    boolean deleteByExchangeAndId(Exchange exchange, String id);
    Client findById(String id);
    List<Client> findAll();
    void deleteAll();
    List<Client> findAllSorted();
    List<Client> findAllByExchange(Exchange exchange);
    List<Client> findAllByTimeToDelete(LocalDateTime localDateTime);
    List<Client> findAllByTimeToDeleteBefore(LocalDateTime before);

    void deleteByExchange(Exchange exchange);
}
