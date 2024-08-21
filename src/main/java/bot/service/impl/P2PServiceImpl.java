package bot.service.impl;

import bot.data.*;
import bot.data.Currency;
import bot.data.entity.Client;
import bot.exception.NotFoundException;
import bot.service.ExchangeService;
import bot.service.ExchangeSubscriberService;
import bot.service.P2PService;
import bot.service.ClientService;

import javax.annotation.PostConstruct;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class P2PServiceImpl implements P2PService {
    private final ExchangeSubscriberService exchangeSubscriberService;
    private final ClientService clientService;
    private final Multimap<Exchange, String> userIdCache = ArrayListMultimap.create();
    private final AppContext appContext;
    private StringBuilder urlCache = new StringBuilder();


    @Override
    @Async
    public void parseOrders(P2PRequest request, Filter filter) {
        StringBuilder foundOrderUrls = new StringBuilder(urlCache);
        Multimap<Exchange, String> foundOrderIds = ArrayListMultimap.create();
        Map<String, String> newFoundOrderUrls = new HashMap<>();

        Collection<ExchangeService> exchangeServices = exchangeSubscriberService.getAllSubscribers();

        for (Currency currency : Currency.values()) {
            request.setCurrencyId(currency.name());
            for(ExchangeService exchangeService : exchangeServices) {
                Map<String, String> availableOrderUrls = exchangeService.getAvailableOrderUrls(request, filter, userIdCache, foundOrderIds);
                newFoundOrderUrls.putAll(availableOrderUrls);
            }
        }

        // create a string with all found urls
        newFoundOrderUrls.forEach((url, id) -> foundOrderUrls.append(url).append("\n").append(id).append("\n\n"));

        if (!foundOrderUrls.isEmpty()) {
            log.info("Sending urls:\n" + foundOrderUrls);

            foundOrderUrls.append(urlCache.toString());
            boolean isSent = appContext.getResponseHandler().sendOrders(foundOrderUrls.toString());
            if (isSent) {
                log.info("Orders are sent. Clear cache");
                urlCache = new StringBuilder();
                persistNewClients(foundOrderIds);
            } else {
                log.warn("Order are not sent. Add to cache");
                urlCache.append(foundOrderUrls);
            }
        }
    }


    @PostConstruct
    public void init() {
        List<Client> clients = clientService.findAll();
        clients.forEach(client -> userIdCache.put(client.getExchange(), client.getId()));
        log.info("Populating foundUserIds from DB: " + userIdCache);
    }

    private void persistNewClients(Multimap<Exchange, String> foundUserIds) {
        List<Client> newClients = new ArrayList<>();

        for (Exchange exchange : foundUserIds.keySet()) {
            for (String id : foundUserIds.get(exchange)) {
                newClients.add(new Client(id, exchange));
            }
        }

        // save to db
        log.info("Saving new clients to db: " + foundUserIds);
        clientService.saveAll(newClients);
    }

    public void deleteByExchangeAndId(Exchange exchange, String id) {
        userIdCache.remove(exchange, id);
        boolean isDeleted = clientService.deleteByExchangeAndId(exchange, id);
        if (!isDeleted) {
            log.warn("Client not found by ID: {}", id);
            throw new NotFoundException("Client not found by ID: " + id);
        }
        log.info("Client from {} with id {} deleted", exchange, id);
    }

    @Override
    public void deleteAll() {
        userIdCache.clear();
        clientService.deleteAll();
        log.info("All clients deleted");
    }

    @Override
    public void deleteByTimer(Client client) {
        Client clientServiceById = clientService.findById(client.getId());
        if (clientServiceById.getExchange() != client.getExchange()) {
            log.warn("Client not found by exchange: {}", client.getExchange());
            throw new NotFoundException("Client not found by exchange: " + client.getExchange());
        }

        clientService.save(client);
    }
}
