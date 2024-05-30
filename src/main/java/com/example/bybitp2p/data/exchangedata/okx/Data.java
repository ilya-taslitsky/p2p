package com.example.bybitp2p.data.exchangedata.okx;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Data {
    private List<SellOrder> sell;
    private int total;

    // Getters and setters
}