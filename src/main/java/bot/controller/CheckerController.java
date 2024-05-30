package bot.controller;

import bot.data.FilterRequest;
import bot.service.Mapper;
import bot.service.P2PScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/checker")
@RequiredArgsConstructor
@Slf4j
public class CheckerController {
    private final P2PScheduler p2PScheduler;
    private final Mapper mapper;

    @PostMapping
    public ResponseEntity<String> start(@RequestBody FilterRequest filterRequest) {
        log.info("/checker start: {}", filterRequest);
        p2PScheduler.start(mapper.mapToP2PRequest(filterRequest), mapper.mapToFilter(filterRequest));
        return ResponseEntity.ok("Checker started");
    }

    @DeleteMapping
    public ResponseEntity<String> stop() {
        p2PScheduler.stop();
        return ResponseEntity.ok("Checker stopped");
    }

}
