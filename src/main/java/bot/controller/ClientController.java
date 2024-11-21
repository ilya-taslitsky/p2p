package bot.controller;

import bot.data.ClientListDto;
import bot.data.ExchangeEnum;
import bot.data.dto.ClientDto;
import bot.data.entity.Client;
import bot.service.ClientService;
import bot.service.P2PService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/clients")
public class ClientController {
    private final P2PService p2PService;
    private final ClientService clientService;

    @GetMapping("/{exchange}/{id}")
    public ResponseEntity<Client> findClientByIdAndExchange(@PathVariable ExchangeEnum exchange, @PathVariable String id) {
        log.info("Triggered endpoint /clients/{}", id);
        return ResponseEntity.ok(clientService.findByIdAndExchange(id, exchange));
    }

    @DeleteMapping("/{exchange}/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable ExchangeEnum exchange, @PathVariable String id) {
        log.info("Triggered endpoint /clients/{}/{}", exchange, id);
        p2PService.deleteByExchangeAndId(exchange, id);
        return ResponseEntity.ok("Client deleted");
    }

    @GetMapping
    public ResponseEntity<ClientListDto> getAllClients() {
        log.info("Triggered endpoint /clients");
        return ResponseEntity.ok(ClientListDto.of(clientService.findAll()));
    }

    @GetMapping("/{exchange}")
    public ResponseEntity<ClientListDto> getClientsByExchange(@PathVariable ExchangeEnum exchange) {
        log.info("Triggered endpoint /clients/{}", exchange);
        return ResponseEntity.ok(ClientListDto.of(clientService.findAllByExchange(exchange)));
    }

    @GetMapping("/sorted")
    public ResponseEntity<ClientListDto> getAllClientsSorted() {
        log.info("Triggered endpoint /clients/sorted");
        return ResponseEntity.ok(ClientListDto.of(clientService.findAllSorted()));
    }

    @DeleteMapping("/timer")
    public ResponseEntity<String> deleteClientAtDate(@RequestBody @Valid ClientDto clientDto) {
        log.info("Triggered endpoint /clients/timer\n{}", clientDto);
        p2PService.deleteByTimer(new Client(clientDto.getId(), clientDto.getExchange(), clientDto.getTimeToDelete(), clientDto.getDescription()));
        return ResponseEntity.ok("Client will be deleted at " + clientDto.getTimeToDelete());
    }

    @DeleteMapping("/{exchange}")
    public ResponseEntity<String> deleteClientsByExchange(@PathVariable ExchangeEnum exchange) {
        log.info("Triggered endpoint /clients/{}", exchange);
        p2PService.deleteByExchange(exchange);
        return ResponseEntity.ok("Clients deleted by exchange: " + exchange);
    }
}
