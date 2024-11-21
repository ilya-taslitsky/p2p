package bot.service.impl;

import bot.data.ExchangeEnum;
import bot.exception.NotFoundException;
import bot.service.ExchangeService;
import bot.service.ExchangeSubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ExchangeSubscriberServiceImpl implements ExchangeSubscriberService, ApplicationContextAware {
    private Map<ExchangeEnum, ExchangeService> exchangeServices = new ConcurrentHashMap<>();
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        exchangeServices.put(ExchangeEnum.BYBIT, applicationContext.getBean(ExchangeEnum.BYBIT.name(), ExchangeService.class));
        exchangeServices.put(ExchangeEnum.BINANCE, applicationContext.getBean(ExchangeEnum.BINANCE.name(), ExchangeService.class));
        log.info("ExchangeSubscriberService initialized with default exchange: BYBIT, BINANCE");
    }

    @Override
    public void subscribe(ExchangeEnum exchange) {
        if (exchangeServices.containsKey(exchange)) {
            throw new NotFoundException("Долбаеб, эта биржа уже подключена");
        }
        ExchangeService exchangeService = applicationContext.getBean(exchange.name(), ExchangeService.class);
        exchangeServices.put(exchange, exchangeService);
        log.info("Exchange {} added", exchange);
    }

    @Override
    public void unsubscribe(ExchangeEnum exchange) {
        if (exchangeServices.remove(exchange) == null) {
           throw new NotFoundException("Долбаеб, эта биржа и так не подключена");
        }
        log.info("Exchange {} removed", exchange);
    }

    @Override
    public Collection<ExchangeService> getAllSubscribers() {
        return exchangeServices.values();
    }

    @Override
    public Collection<ExchangeEnum> getAllExchanges() {
        return exchangeServices.keySet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
