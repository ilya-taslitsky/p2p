package bot.controller;

import bot.data.Exchange;
import bot.data.FilterRequest;
import bot.service.ExchangeSubscriberService;
import bot.service.Mapper;
import bot.service.P2PScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
@Slf4j
public class BotController {
    private final P2PScheduler p2PScheduler;
    private final ExchangeSubscriberService exchangeSubscriberService;

    @PostMapping
    public ResponseEntity<String> start(@RequestBody FilterRequest filterRequest) {
        log.info("Triggered endpoint /checker start: {}", filterRequest);
        p2PScheduler.start(filterRequest);
        return ResponseEntity.ok("Checker started");
    }

    @DeleteMapping
    public ResponseEntity<String> stop() {
        log.info("Triggered endpoint /checker stop");
        p2PScheduler.stop();
        return ResponseEntity.ok("Checker stopped");
    }

    @PutMapping("/exchanges/{exchange}")
    public ResponseEntity<String> addExchange(@PathVariable String exchange) {
        log.info("Triggered endpoint /exchanges/{}", exchange);
        exchangeSubscriberService.subscribe(Exchange.fromString(exchange));
        return ResponseEntity.ok("Exchange added");
    }

    @DeleteMapping("/exchanges/{exchange}")
    public ResponseEntity<String> deleteExchange(@PathVariable String exchange) {
        log.info("Triggered endpoint /exchanges/{}", exchange);
        exchangeSubscriberService.unsubscribe(Exchange.fromString(exchange));
        return ResponseEntity.ok("Exchange removed");
    }

    @GetMapping
    public ResponseEntity<String> getBotStatus() {
        log.info("Triggered endpoint /bot get bot status");
        String message = p2PScheduler.isStarted() ? "Bot is started" : "Bot is stopped";
        message += "\nStatus: " + p2PScheduler.getLastRequest() + "\nExhanges:" + exchangeSubscriberService.getAllExchanges();
        return ResponseEntity.ok(message);
    }
}
