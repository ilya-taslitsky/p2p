package bot.dao;

import bot.data.exchangedata.huobi.HuobiResponse;
import bot.data.exchangedata.huobi.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class HuobiClient {
    private final WebClient webClient;

    public List<Data> findOrdersWithFilter(String url) {
        try {
            ResponseEntity<HuobiResponse> response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .toEntity(HuobiResponse.class)
                    .block();
            return response.getBody().getData();
        } catch (Exception e) {
            log.error("Failed to send bybit request", e);
        }
        return null;
    }

}
