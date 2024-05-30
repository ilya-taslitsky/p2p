package com.example.bybitp2p.service.impl;

import com.example.bybitp2p.data.*;
import com.example.bybitp2p.data.exchangedata.bybit.Currency;
import com.example.bybitp2p.data.entity.Client;
import com.example.bybitp2p.data.P2PRequest;
import com.example.bybitp2p.service.BybitService;
import com.example.bybitp2p.service.ClientService;
import com.example.bybitp2p.service.OkxService;
import com.example.bybitp2p.service.P2PService;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class P2PServiceImpl implements P2PService {
    private final BybitService bybitService;
    private final OkxService okxService;
    private final ClientService clientService;
    private final Set<String> foundUserIds = new HashSet<>();
    private List<Client> clients;
    private final AppContext appContext;
    private StringBuilder urlCache = new StringBuilder();


    @Override
    @Async
    public void parseOrders(P2PRequest request, Filter filter) {
        StringBuilder foundOrderUrls = new StringBuilder(urlCache);
        List<String> foundUrls = new ArrayList<>();
        for (Currency currency : Currency.values()) {
            request.setCurrencyId(currency.name());
                foundUrls.addAll(bybitService.getAvailableOrderUrls(request, filter, foundUserIds));
//            foundUrls.addAll(okxService.getAvailableOrderUrls(request, filter, foundUserIds));
        }
        foundUrls.forEach(url -> foundOrderUrls.append(url).append("\n\n"));
        if (!foundOrderUrls.isEmpty()) {
            log.info("Sending urls:\n" + foundOrderUrls);
            boolean isSent = appContext.getResponseHandler().sendMessage(foundOrderUrls.toString());
            if (isSent) {
                log.info("Orders are sent. Clear cache");
                urlCache = new StringBuilder();
            } else {
                log.warn("Order are not sent. Add to cache");
                urlCache.append(foundOrderUrls);
            }
        }
    }


    @PostConstruct
    public void init() {
        List<Client> clients = clientService.findAll();
        clients.forEach(client -> foundUserIds.add(client.getId()));
        this.clients = clients;
        log.info("Populating foundUserIds from DB: " + foundUserIds);
    }

    @PreDestroy
    public void destroy() {
        Set<String> clientIds = clients.stream().map(Client::getId).collect(Collectors.toSet());
        foundUserIds.removeAll(clientIds);

        List<Client> newClients = foundUserIds.stream().map(Client::new).toList();

        // save to db
        log.info("Saving new clients to db: " + clientIds);
        clientService.saveAll(newClients);
    }
}
