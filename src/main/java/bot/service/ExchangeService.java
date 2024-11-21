package bot.service;

import bot.data.ExchangeEnum;
import bot.data.P2PRequest;
import bot.data.Filter;
import com.google.common.collect.Multimap;

import java.util.Map;


public interface ExchangeService {
    // Method to get available order urls to  ids
    Map<String, String> getAvailableOrderUrls(P2PRequest request, Filter filter,
                                              Multimap<ExchangeEnum, String> userIdCache,
                                              Multimap<ExchangeEnum, String> foundUserIds);
}
