package com.example.bybitp2p.service;

import com.example.bybitp2p.data.P2PRequest;
import com.example.bybitp2p.data.Filter;

public interface P2PService {
     void parseOrders(P2PRequest order, Filter filter);
}
