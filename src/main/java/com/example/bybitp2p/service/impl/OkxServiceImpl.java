package com.example.bybitp2p.service.impl;

import com.example.bybitp2p.dao.OkxClient;
import com.example.bybitp2p.data.Filter;
import com.example.bybitp2p.data.Links;
import com.example.bybitp2p.data.P2PRequest;
import com.example.bybitp2p.data.P2PResponse;
import com.example.bybitp2p.data.exchangedata.bybit.Currency;
import com.example.bybitp2p.data.exchangedata.okx.OkxRequest;
import com.example.bybitp2p.data.exchangedata.okx.SellOrder;
import com.example.bybitp2p.service.ExchangeService;
import com.example.bybitp2p.service.Mapper;
import com.example.bybitp2p.service.OkxService;
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
