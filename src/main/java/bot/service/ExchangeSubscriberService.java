package bot.service;

import bot.data.ExchangeEnum;

import java.util.Collection;

public interface ExchangeSubscriberService {
    void subscribe(ExchangeEnum exchange);
    void unsubscribe(ExchangeEnum exchange);
    Collection<ExchangeService> getAllSubscribers();
    Collection<ExchangeEnum> getAllExchanges();
}
