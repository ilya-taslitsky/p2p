package bot.service.impl;

import bot.data.ExchangeEnum;
import bot.data.entity.Client;
import bot.dao.ClientRepository;
import bot.exception.NotFoundException;
import bot.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientDao;

    @Override
    public Client findByIdAndExchange(String id, ExchangeEnum exchange) {
        log.info("Find client by ID: {} and exchange: {}", id, exchange);
        return clientDao.findByExchangeAndId(exchange, id).orElseThrow(() -> {
            log.warn("Client not found by ID: {} and exchange: {}", id, exchange);
            return new NotFoundException("Client not found");
        });
    }

    @Override
    @Transactional
    public void save(Client client) {
        clientDao.save(client);
    }


    @Override
    @Transactional
    public void saveAll(List<Client> client) {
        clientDao.saveAll(client);
    }

    @Override
    @Transactional
    @Modifying
    public boolean deleteByExchangeAndId(ExchangeEnum exchange, String id) {
        log.info("Delete client by exchange: {} and ID: {}", exchange, id);
        Optional<Client> clientOptional = clientDao.findByExchangeAndId(exchange, id);
        if (clientOptional.isEmpty()) {
            log.warn("Client not found by exchange: {} and ID: {}", exchange, id);
            return false;
        }
        clientDao.delete(clientOptional.get());
        log.info("Client deleted by exchange: {} and ID: {}", exchange, id);
        return true;
    }

    @Override
    public Client findById(String userId) {
        log.info("Find client by ID: {}", userId);
        return clientDao.findById(userId).orElseThrow(() -> {
            log.warn("Client not found by ID: {}", userId);
            return new NotFoundException("Client not found");
        });
    }

    @Override
    public List<Client> findAll() {
        log.info("Find all clients");
        return clientDao.findAll();
    }

    @Override
    @Transactional
    @Modifying
    public void deleteAll() {
        log.info("Delete all clients");
        clientDao.deleteAll();
    }

    @Override
    public List<Client> findAllSorted() {
        List<Client> clients = new ArrayList<>(clientDao.findAll());
        clients.sort(Comparator.comparing(Client::getExchange).thenComparing(Client::getCreationTime,
                Comparator.nullsFirst(Comparator.naturalOrder())));
        return clients;
    }

    @Override
    public List<Client> findAllByExchange(ExchangeEnum exchange) {
        List<Client> clients = clientDao.findByExchange(exchange);
        clients.sort(Comparator.comparing(Client::getCreationTime, Comparator.nullsFirst(Comparator.naturalOrder())));
        return clients;
    }

    @Override
    public List<Client> findAllByTimeToDelete(LocalDateTime localDateTime) {
        return clientDao.findByTimeToDelete(localDateTime);
    }

    @Override
    public List<Client> findAllByTimeToDeleteBefore(LocalDateTime before) {
        return clientDao.findByTimeToDeleteBefore(before);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteByExchange(ExchangeEnum exchange) {
        log.info("Delete all clients by exchange: {}", exchange);
        clientDao.deleteAllByExchange(exchange);
    }

}
