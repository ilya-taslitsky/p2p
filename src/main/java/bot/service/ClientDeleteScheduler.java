package bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
@RequiredArgsConstructor
public class ClientDeleteScheduler {
    private final ClientService clientService;
    private final P2PService p2PService;

    @PostConstruct
    public void deleteAllThatShouldHaveBeenDelete() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        clientService.findAllByTimeToDeleteBefore(now).forEach(client ->
                p2PService.deleteByExchangeAndId(client.getExchange(), client.getId())
        );
    }

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    @Transactional
    public void deleteClientAtDate() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        clientService.findAllByTimeToDelete(now).forEach(client ->
               p2PService.deleteByExchangeAndId(client.getExchange(), client.getId())
        );
    }
}
