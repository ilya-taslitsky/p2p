package com.example.bybitp2p.service;

import com.example.bybitp2p.data.entity.Client;

import java.util.List;

public interface ClientService {
    void save(Client client);
    void saveAll(List<Client> client);
    void deleteById(String id);
    Client findById(String id);
    List<Client> findAll();
}
