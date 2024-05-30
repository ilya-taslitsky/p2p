package com.example.bybitp2p.data.exchangedata.bybit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BybitResponse {
    private Result result;
    // Getters and setters
}

