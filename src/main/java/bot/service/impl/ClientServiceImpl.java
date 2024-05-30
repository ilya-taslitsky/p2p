package bot.service.impl;

import bot.data.entity.Client;
import bot.dao.ClientRepository;
import bot.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
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
    public void deleteById(String userId) {

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
}
