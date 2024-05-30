package com.example.bybitp2p.service;

import com.example.bybitp2p.data.Filter;
import com.example.bybitp2p.data.P2PRequest;
import com.example.bybitp2p.data.P2PResponse;
import com.example.bybitp2p.data.exchangedata.bybit.Currency;

import java.util.List;
import java.util.Set;

public interface OkxService {
    List<String> getAvailableOrderUrls(P2PRequest request, Filter filter, Set<String> foundUserIds);
}
