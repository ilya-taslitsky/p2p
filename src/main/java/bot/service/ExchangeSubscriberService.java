package bot.service;

import bot.data.Exchange;

import java.util.Collection;
import java.util.List;

public interface ExchangeSubscriberService {
    void subscribe(Exchange exchange);
    void unsubscribe(Exchange exchange);
    Collection<ExchangeService> getAllSubscribers();
}
