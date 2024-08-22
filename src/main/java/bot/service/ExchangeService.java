package bot.service;

import bot.data.Exchange;
import bot.data.P2PRequest;
import bot.data.P2PResponse;
import bot.data.Filter;
import com.google.common.collect.Multimap;

import java.util.List;
import java.util.Map;


public interface ExchangeService {
    // Method to get available order urls to  ids
    Map<String, String> getAvailableOrderUrls(P2PRequest request, Filter filter,
                                              Multimap<Exchange, String> userIdCache,
                                              Multimap<Exchange, String> foundUserIds);
}
