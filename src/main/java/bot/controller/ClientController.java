package bot.controller;

import bot.data.ClientListDto;
import bot.data.Exchange;
import bot.service.ClientService;
import bot.service.P2PService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/clients")
public class ClientController {
    private final P2PService p2PService;
    private final ClientService clientService;
    @DeleteMapping("/{exchange}/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Exchange exchange, @PathVariable String id) {
        log.info("Triggered endpoint /clients/{}", id);
        p2PService.deleteByExchangeAndId(exchange, id);
        return ResponseEntity.ok("Client deleted");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAllClients() {
        log.info("Triggered endpoint delete all clients");
        p2PService.deleteAll();
        return ResponseEntity.ok("Clients deleted");
    }

    @GetMapping
    public ResponseEntity<ClientListDto> getAllClients() {
        log.info("Triggered endpoint /clients");
        return ResponseEntity.ok(ClientListDto.of(clientService.findAll()));
    }
}
