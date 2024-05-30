package bot.service;

import bot.data.Filter;
import bot.data.P2PRequest;

import java.util.List;
import java.util.Set;

public interface BybitService {
    List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Set<String> foundUserIds);
}
