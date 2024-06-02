package bot.controller;

import bot.exception.NotFoundException;
import bot.service.ClientService;
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
    private final ClientService clientService;
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable String id) {
        log.info("Triggered endpoint /clients/{}", id);
        boolean isDeleted = clientService.deleteById(id);
        if (!isDeleted) {
            log.warn("Client not found by ID: {}", id);
            throw new NotFoundException("Client not found by ID: " + id);
        }
        log.info("Client deleted");
        return ResponseEntity.ok("Client deleted");
    }
}
