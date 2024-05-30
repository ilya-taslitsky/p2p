package bot.service.impl;

import bot.data.exchangedata.okx.OkxRequest;
import bot.service.OkxService;
import bot.dao.OkxClient;
import bot.data.Filter;
import bot.data.Links;
import bot.data.P2PRequest;
import bot.data.P2PResponse;
import bot.service.ExchangeService;
import bot.service.Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Component
@Slf4j
public class OkxServiceImpl extends ExchangeService implements OkxService {
    private String baseUrl = Links.OKX_GET_ORDERS_URL;
    private final OkxClient okxClient;
    private final Mapper mapper;

    @Override
    public List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Set<String> foundUserIds) {
        List<String> foundOrderUrls = new ArrayList<>();
        OkxRequest okxRequest = mapper.mapToOkxRequest(request);
        String urlWithParams = String.format(baseUrl, okxRequest.getCryptoCurrency(), okxRequest.getCurrency(), okxRequest.getTimestamp());
        List<P2PResponse> responses = new ArrayList<>(okxClient.findOrdersWithFilter(urlWithParams));
        List<P2PResponse> processResponses = processResponses(responses, filter);
        processResponses.stream()
                .filter(item -> !foundUserIds.contains(item.getUserId()))
                .forEach(item -> {
                    foundUserIds.add(item.getUserId());
                    foundOrderUrls.add(String.format(Links.OKX_MERCHANT_URL, item.getUserId()));
                });
        return foundOrderUrls;
    }
}
