package com.example.bybitp2p.dao;

import com.example.bybitp2p.data.Links;
import com.example.bybitp2p.data.P2PRequest;
import com.example.bybitp2p.data.P2PResponse;
import com.example.bybitp2p.data.exchangedata.bybit.Item;
import com.example.bybitp2p.data.exchangedata.bybit.BybitResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BybitClient {
    private String baseUrl = Links.BYBIT_GET_ORDERS_URL;
    private final WebClient webClient;

    public List<Item> findOrdersWithFilter(P2PRequest bybitP2PRequest) {
        try {
            ResponseEntity<BybitResponse> response = webClient.post()
                    .uri(baseUrl)
                    .bodyValue(bybitP2PRequest)
                    .retrieve()
                    .toEntity(BybitResponse.class)
                    .block();
            return response.getBody().getResult().getItems();
        } catch (Exception e) {
            log.error("Failed to send bybit request", e);
        }
        return null;
    }

}
