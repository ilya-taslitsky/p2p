package bot.service;

import bot.data.Exchange;
import bot.data.P2PRequest;
import bot.data.Filter;

public interface P2PService {
     void parseOrders(P2PRequest order, Filter filter);
     void deleteByExchangeAndId(Exchange exchange, String id);
}
