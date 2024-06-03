package bot.dao;

import bot.data.Exchange;
import bot.data.entity.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, String> {
    Optional<Client> findByExchangeAndId(Exchange exchange, String id);
}