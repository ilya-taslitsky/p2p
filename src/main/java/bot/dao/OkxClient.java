package bot.dao;

import bot.data.exchangedata.okx.OkxResponse;
import bot.data.exchangedata.okx.SellOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OkxClient {
    private final WebClient webClient;

    public List<SellOrder> findOrdersWithFilter(String url) {
        try {
            ResponseEntity<OkxResponse> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(OkxResponse.class)
                    .block();
            return response.getBody().getData().getSell();
        } catch (Exception e) {
            log.error("Failed to send bybit request", e);
        }
        return null;
    }
}
