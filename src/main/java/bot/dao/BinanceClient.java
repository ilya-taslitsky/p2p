package bot.dao;

import bot.data.Links;
import bot.data.exchangedata.binance.BinanceRequest;
import bot.data.exchangedata.binance.BinanceResponse;
import bot.data.exchangedata.binance.DataItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinanceClient {
    private String baseUrl = Links.BINANCE_GET_ORDERS_URL;
    private final WebClient webClient;

    public List<DataItem> findOrdersWithFilter(BinanceRequest binanceRequest) {
            try {
                ResponseEntity<BinanceResponse> response = webClient.post()
                        .uri(baseUrl)
                        .bodyValue(binanceRequest)
                        .retrieve()
                        .toEntity(BinanceResponse.class)
                        .block();
                return response.getBody().getData();
            } catch (Exception e) {
                log.error("Failed to send binance request", e);
            }
          return null;
    }

    public String get(String url) {
        try {
            return webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Failed to send binance request", e);
        }
        return null;
    }
}