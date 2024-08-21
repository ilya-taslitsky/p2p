package bot.service;

import bot.data.Exchange;
import bot.data.P2PRequest;
import bot.data.Filter;
import bot.data.entity.Client;

public interface P2PService {
     void parseOrders(P2PRequest order, Filter filter);
     void deleteByExchangeAndId(Exchange exchange, String id);
     void deleteAll();
     void deleteByTimer(Client client);
}
