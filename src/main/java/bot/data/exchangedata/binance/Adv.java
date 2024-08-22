package bot.data.exchangedata.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Adv {
    private String advNo;
    private String classify;
    private String tradeType;
    private String asset;
    private String fiatUnit;
    private String advStatus;
    private String priceType;
    private String priceFloatingRatio;
    private String rateFloatingRatio;
    private String currencyRate;
    private String price;
    private String initAmount;
    private String surplusAmount;
    private String tradableQuantity;
    private String amountAfterEditing;
    private String maxSingleTransAmount;
    private String minSingleTransAmount;
    private String buyerKycLimit;
    private String buyerRegDaysLimit;
    private String buyerBtcPositionLimit;
    private String remarks;
    private String autoReplyMsg;
    private int payTimeLimit;
    private List<TradeMethod> tradeMethods;
    private String userTradeCountFilterTime;
    private String userBuyTradeCountMin;
    private String userBuyTradeCountMax;
    private String userSellTradeCountMin;
    private String userSellTradeCountMax;
    private String userAllTradeCountMin;
    private String userAllTradeCountMax;
    private String userTradeCompleteRateFilterTime;
    private String userTradeCompleteCountMin;
    private String userTradeCompleteRateMin;
    private String userTradeVolumeFilterTime;
    private String userTradeType;
    private String userTradeVolumeMin;
    private String userTradeVolumeMax;
    private String userTradeVolumeAsset;
    private String createTime;
    private String advUpdateTime;
    private String fiatVo;
    private String assetVo;
    private String advVisibleRet;
    private int takerAdditionalKycRequired;
    private String minFiatAmountForAdditionalKyc;
    private String inventoryType;
    private String offlineReason;
    private String assetLogo;
    private int assetScale;
    private int fiatScale;
    private int priceScale;
    private String fiatSymbol;
    private boolean isTradable;
    private String dynamicMaxSingleTransAmount;
    private String minSingleTransQuantity;
    private String maxSingleTransQuantity;
    private String dynamicMaxSingleTransQuantity;
    private String commissionRate;
    private String takerCommissionRate;
    private String minTakerFee;
    private List<Object> tradeMethodCommissionRates;
    private String launchCountry;
    private String abnormalStatusList;
    private String closeReason;
    private String storeInformation;
    private String allowTradeMerchant;
    private List<Object> adTradeInstructionTagInfoRets;
    private boolean isSafePayment;

    // Getters and Setters
}