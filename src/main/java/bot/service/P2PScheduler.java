package bot.service;

import bot.data.Filter;
import bot.data.P2PRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

@Component
@RequiredArgsConstructor
@Slf4j
public class P2PScheduler {
    private Timer timer;
    private static final int TIMER_COOLDOWN = 30_000;
    private final P2PService p2PService;

    public void start(P2PRequest request, Filter filter) {
        if (timer != null) {
            timer.cancel();
        }
        log.info("Started parsing orders");
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                p2PService.parseOrders(request, filter);
            }
        };

        timer.schedule(timerTask, 0, TIMER_COOLDOWN);

    }

    public void stop() {
        log.info("Stopped parsing orders");
        if (timer != null) {
            timer.cancel();
        }
    }
}
