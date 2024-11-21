package bot.service;

import bot.data.ExchangeEnum;
import bot.data.entity.Client;

import java.time.LocalDateTime;
import java.util.List;

public interface ClientService {
    Client findByIdAndExchange(String id, ExchangeEnum exchange);
    void save(Client client);
    void saveAll(List<Client> client);
    boolean deleteByExchangeAndId(ExchangeEnum exchange, String id);
    Client findById(String id);
    List<Client> findAll();
    void deleteAll();
    List<Client> findAllSorted();
    List<Client> findAllByExchange(ExchangeEnum exchange);
    List<Client> findAllByTimeToDelete(LocalDateTime localDateTime);
    List<Client> findAllByTimeToDeleteBefore(LocalDateTime before);

    void deleteByExchange(ExchangeEnum exchange);
}
