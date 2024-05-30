package com.example.bybitp2p.data.exchangedata.bybit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
    private List<Item> items;

    // Getters and setters
}

