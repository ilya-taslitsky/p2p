package com.example.bybitp2p.data;

import lombok.Data;

@Data
public class Filter {
    private int recentOrderNum; // < 100
    private Double maxAmount;
    private Double minAmount;
    private int hasRegisterTime; // 30+
    private int paymentsCount; // < 3
    private Double lastQuantity;
}
