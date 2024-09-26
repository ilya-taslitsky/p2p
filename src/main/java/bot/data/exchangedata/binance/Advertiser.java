package bot.data.exchangedata.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Advertiser {
    private String userNo;
    private String realName;
    private String nickName;
    private String margin;
    private String marginUnit;
    private String orderCount;
    private int monthOrderCount;
    private double monthFinishRate;
    private double positiveRate;
    private String advConfirmTime;
    private String email;
    private String registrationTime;
    private String mobile;
    private String userType;
    private List<String> tagIconUrls;
    private int userGrade;
    private String userIdentity;
    private List<String> badges;
    private String isBlocked;
    private int activeTimeInSecond;

    // Getters and Setters
}