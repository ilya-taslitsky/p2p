package bot.dao;

import bot.data.Exchange;
import bot.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByExchangeAndId(Exchange exchange, String id);
}