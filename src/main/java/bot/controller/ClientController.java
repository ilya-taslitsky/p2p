package bot.controller;

import bot.data.Exchange;
import bot.service.P2PService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/clients")
public class ClientController {
    private final P2PService p2PService;
    @DeleteMapping("/{exchange}/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Exchange exchange, @PathVariable String id) {
        log.info("Triggered endpoint /clients/{}", id);
        p2PService.deleteByExchangeAndId(exchange, id);
        return ResponseEntity.ok("Client deleted");
    }
}
