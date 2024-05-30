package com.example.bybitp2p.data;

import lombok.Data;

import java.util.List;
@Data
public class P2PRequest {
    private String tokenId;
    private String currencyId;
    private List<String> payment;
    private String side = "1";
    private String page;
}
