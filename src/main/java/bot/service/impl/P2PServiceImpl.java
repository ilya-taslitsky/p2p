package bot.service.impl;

import bot.data.AppContext;
import bot.data.Filter;
import bot.data.entity.Client;
import bot.data.exchangedata.bybit.Currency;
import bot.exception.NotFoundException;
import bot.service.ExchangeService;
import bot.service.ExchangeSubscriberService;
import bot.service.P2PService;
import bot.data.P2PRequest;
import bot.service.ClientService;

import javax.annotation.PostConstruct;

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
    private final Set<String> userIdCache = new HashSet<>();
    private final AppContext appContext;
    private StringBuilder urlCache = new StringBuilder();


    @Override
    @Async
    public void parseOrders(P2PRequest request, Filter filter) {
        StringBuilder foundOrderUrls = new StringBuilder(urlCache);
        List<String> foundOrderIds = new ArrayList<>();
        List<String> newFoundOrderUrls = new ArrayList<>();

        Collection<ExchangeService> exchangeServices = exchangeSubscriberService.getAllSubscribers();

        for (Currency currency : Currency.values()) {
            request.setCurrencyId(currency.name());
            for(ExchangeService exchangeService : exchangeServices) {
                List<String> availableOrderUrls = exchangeService.getAvailableOrderUrls(request, filter, userIdCache, foundOrderIds);
                newFoundOrderUrls.addAll(availableOrderUrls);
            }
        }

        // create a string with all found urls
        newFoundOrderUrls.forEach(url -> foundOrderUrls.append(url).append("\n\n"));

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
        clients.forEach(client -> userIdCache.add(client.getId()));
        log.info("Populating foundUserIds from DB: " + userIdCache);
    }

    private void persistNewClients(List<String> foundUserIds) {
        List<Client> newClients = foundUserIds.stream().map(Client::new).toList();

        // save to db
        log.info("Saving new clients to db: " + foundUserIds);
        clientService.saveAll(newClients);
    }

    public void deleteById(String id) {
        userIdCache.remove(id);
        boolean isDeleted = clientService.deleteById(id);
        if (!isDeleted) {
            log.warn("Client not found by ID: {}", id);
            throw new NotFoundException("Client not found by ID: " + id);
        }
        log.info("Client deleted");
    }
}
