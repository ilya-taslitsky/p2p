package bot.service;

import bot.data.P2PRequest;
import bot.data.P2PResponse;
import bot.data.Filter;

import java.util.List;
import java.util.Set;

public interface ExchangeService {
    List<P2PResponse> processResponses(List<P2PResponse> items, Filter filter);
    List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Set<String> userIdCache,  List<String> foundUserIds);
}
