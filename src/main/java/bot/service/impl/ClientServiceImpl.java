package bot.service.impl;

import bot.data.Exchange;
import bot.data.entity.Client;
import bot.dao.ClientRepository;
import bot.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientDao;
    @Override
    @Transactional
    public void save(Client client) {
        clientDao.save(client);
    }

    @Override
    public void saveAll(List<Client> client) {
        clientDao.saveAll(client);
    }

    @Override
    @Transactional
    @Modifying
    public boolean deleteByExchangeAndId(Exchange exchange, String id) {
        Optional<Client> clientOptional = clientDao.findByExchangeAndId(exchange, id);
        if (clientOptional.isEmpty()) {
            return false;
        }
        clientDao.delete(clientOptional.get());
        return true;
    }

    @Override
    @Transactional
    public Client findById(String userId) {
        return clientDao.findById(userId).orElse(null);
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        clientDao.findAll().forEach(clients::add);
        return clients;
    }

    public void test() {
        Iterable<Client> all = clientDao.findAll();
        all.forEach(client -> client.setExchange(Exchange.BYBIT));
        clientDao.saveAll(all);
        log.info("Test: "+ all);
    }
}
