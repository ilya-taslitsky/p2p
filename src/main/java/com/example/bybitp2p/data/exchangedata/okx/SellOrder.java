package com.example.bybitp2p.data.exchangedata.okx;

import com.example.bybitp2p.data.P2PResponse;
import lombok.Setter;

import java.util.List;

@Setter
public class SellOrder implements P2PResponse {
    private String availableAmount; // lastQuntity
    private String creatorType; // should be "common"
    private int maxCompletedOrderQuantity; // should be 0
    private long maxUserCreatedDate; // in timestamp. current date.after
    private int minCompletedOrderQuantity; // should be 0
    private int completedOrderQuantity; // less < 100
    private int minKycLevel; // 1??
    private List<String> paymentMethods; // paymentMethod
    private String price; // price
    private String publicUserId; // userId
    private String quoteMaxAmountPerOrder; // maxAmount
    private String side; // sell or buy
    private String userType;
    private int verificationType;

    @Override
    public String getUserId() {
        return publicUserId;
    }

    @Override
    public String getCurrency() {
        return null;
    }

    @Override
    public String getCryptoCurrency() {
        return null;
    }

    @Override
    public String getSide() {
        return "sell";
    }

    @Override
    public Double getPrice() {
        return Double.valueOf(price);
    }

    @Override
    public String getPremium() {
        if (!creatorType.equals("common")) {
            return "1";
        }
        return "0";
    }

    @Override
    public int getCompleteOrderRate() {
        return 0;
    }

    @Override
    public Double getLastQuantity() {
        return Double.valueOf(availableAmount);
    }

    @Override
    public List<String> getPayments() {
        return paymentMethods;
    }

    @Override
    public int getMaxCompletedOrderQuantity() {
        return maxCompletedOrderQuantity;
    }

    @Override
    public int getCompletedOrderQuantity() {
        return completedOrderQuantity;
    }

    @Override
    public Double getMaxAmount() {
        return Double.valueOf(quoteMaxAmountPerOrder);
    }

    @Override
    public int getAuthStatus() {
        return 2;
    }


    // Getters and setters
}