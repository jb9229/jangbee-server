package com.jangbee.ad;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by test on 2019-02-14.
 */
public class AdDto {
    @Data
    public static class Response {
        private Long id;
        private short adType;
        private String accountId;
        private String photoUrl;
        private String title;
        private String subTitle;
        private String telNumber;
        private String equiTarget;
        private String sidoTarget;
        private String gugunTarget;
        @JsonFormat(pattern="yyyy-MM-dd")
        private Date startDate;
        @JsonFormat(pattern="yyyy-MM-dd")
        private Date endDate;
    }

    @Data
    public static class Create {
        @NotNull
        private short adType;
        @NotBlank
        private String accountId;
        private String equiTarget;
        private String sidoTarget;
        private String gugunTarget;
        @NotBlank
        @Size(max = 15)
        private String title;
        @NotBlank
        @Size(max = 20)
        private String subTitle;
        private String photoUrl;
        private String telNumber;
        @NotNull
        private int price;
        @Min(1)
        private int forMonths;

        private String paymentSid;
    }

    @Data
    public static class Update {
        @NotNull
        private Long id;
        @NotBlank
        @Size(max = 15)
        private String title;
        @NotBlank
        @Size(max = 20)
        private String subTitle;
        private String photoUrl;
        private String telNumber;
        @Min(0)
        private int forMonths;
    }

    @Data
    public static class TransferWithdrawResponse {
        private String rsp_code;    // "A0000"
        private String rsp_message;    // "A0000"

        private String account_holder_name;    // "홍길동"
        private String tran_amt;    // "A0000"
        private String bank_name;    // "A0000"
        private String account_alias; // 급여계좌
        private String print_content;
        private String bank_tran_id;  // "12345678901234567890"
        private String api_tran_dtm;  // "20160310101921567"
    }

    @Data
    public static class TransferDepositResponse {
        private String rsp_code;    // "A0000"
        private String rsp_message;    // "A0000"

        private String api_tran_id;
        private String wd_account_holder_name;    // "홍길동"
        private String tran_amt;    // "A0000"
        private String bank_name;    // "A0000"
        private String account_alias; // 급여계좌
        private String print_content;
        private String bank_tran_id;  // "12345678901234567890"
        private String api_tran_dtm;  // "20160310101921567"
        private List<Deposit> res_list;
    }

    @Data
    public static class RefreshTokenResponse {
        private String rsp_code;    // "A0000"
        private String rsp_message;    // "A0000"

        private String access_token;    // "홍길동"
        private String token_type;    // "A0000"
        private String expires_in;    // "A0000"
        private String refresh_token; // 급여계좌
        private String scope;
        private String user_seq_no;  // "12345678901234567890"
        private Object accTokenExpDate;

        public String getAccTokenExpDate() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar tokenExpDateCal = Calendar.getInstance();
            tokenExpDateCal.add(Calendar.DAY_OF_MONTH, 85);

            return dateFormat.format(tokenExpDateCal.getTime());
        }
    }

    @Data
    private static class Deposit{
        private String bank_tran_id; // "12345678901234567890"
        private String bank_tran_date; // "20160310"
        private String bank_rsp_code; // "000"
        private String bank_rsp_message; //"",
        private String account_alias;  //"급여계좌"
        private String bank_name; // 오픈은행
        private String account_num_masked; // "000-1230000-***"
        private String print_content; // 쇼핑몰환불",
        private String account_holder_name; // "홍길동",
        private String tran_amt; // "10000“

    }
}
