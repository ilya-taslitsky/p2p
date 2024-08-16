package bot.service;

import bot.data.FilterRequest;
import bot.data.P2PRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
@RequiredArgsConstructor
@Slf4j
@Getter
public class P2PScheduler {
    private Timer timer;
    private static final int TIMER_COOLDOWN = 30_000;
    private final P2PService p2PService;
    private boolean isStarted;
    private FilterRequest lastRequest;
    private final Mapper mapper;

    public void start(FilterRequest filterRequest) {
        this.lastRequest = filterRequest;

        if (timer != null) {
            timer.cancel();
        }
        log.info("Started parsing orders");
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                p2PService.parseOrders(mapper.mapToP2PRequest(filterRequest), mapper.mapToFilter(filterRequest));
            }
        };

        timer.schedule(timerTask, 0, TIMER_COOLDOWN);
        isStarted = true;

    }

    public void start() {
        if (lastRequest == null) {
            log.error("Filter request is not set");
            return;
        }
        start(lastRequest);
    }

    public void stop() {
        log.info("Stopped parsing orders");
        if (timer != null) {
            timer.cancel();
        }
        isStarted = false;
    }
}
