package bot.dao;

import bot.data.Exchange;
import bot.data.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByExchangeAndId(Exchange exchange, String id);
    List<Client> findByTimeToDelete(LocalDateTime timeToDelete);
    List<Client> findByExchange(Exchange exchange);
    List<Client> findByTimeToDeleteBefore(LocalDateTime timeToDelete);
}