package com.example.bybitp2p.service;

import com.example.bybitp2p.data.P2PRequest;
import com.example.bybitp2p.data.Filter;
import com.example.bybitp2p.data.FilterRequest;
import com.example.bybitp2p.data.exchangedata.okx.OkxRequest;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public P2PRequest mapToP2PRequest(FilterRequest filterRequest) {
        P2PRequest p2PRequest = new P2PRequest();
        p2PRequest.setTokenId(filterRequest.getTokenId());
        p2PRequest.setPayment(filterRequest.getPayment());
        return p2PRequest;
    }

    public Filter mapToFilter(FilterRequest filterRequest) {
        Filter filter = new Filter();
        filter.setRecentOrderNum(filterRequest.getRecentOrderNum());
        filter.setMaxAmount(filterRequest.getMaxAmount());
        filter.setHasRegisterTime(filterRequest.getHasRegisterTime());
        filter.setMinAmount(filterRequest.getMinAmount());
        filter.setPaymentsCount(filterRequest.getPaymentsCount());
        filter.setLastQuantity(filterRequest.getLastQuantity());
        return filter;
    }

    public OkxRequest mapToOkxRequest(P2PRequest p2PRequest) {
        OkxRequest okxRequest = new OkxRequest();
        okxRequest.setCurrency(p2PRequest.getCurrencyId());
        okxRequest.setCryptoCurrency(p2PRequest.getTokenId());
        return okxRequest;
    }

}