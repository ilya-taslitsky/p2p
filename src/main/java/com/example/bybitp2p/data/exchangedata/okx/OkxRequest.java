package com.example.bybitp2p.data.exchangedata.okx;

import lombok.Data;

@Data
public class OkxRequest {
    private String currency;
    private String cryptoCurrency;
    private long timestamp = System.currentTimeMillis();
}
