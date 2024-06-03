package bot.service;

import bot.data.Exchange;
import bot.data.P2PRequest;
import bot.data.P2PResponse;
import bot.data.Filter;
import com.google.common.collect.Multimap;

import java.util.List;


public interface ExchangeService {
    List<P2PResponse> processResponses(List<P2PResponse> items, Filter filter);
    List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Multimap<Exchange, String> userIdCache, Multimap<Exchange, String> foundUserIds);
}
