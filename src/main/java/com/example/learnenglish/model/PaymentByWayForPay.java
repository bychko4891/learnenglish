package com.example.learnenglish.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Getter
@Setter
public class PaymentByWayForPay {

    private String merchantAccount = "e_learn_top"; //

    private String merchantDomainName = "www.e-learn.top";//
//    private String returnUrl = "www.e-learn.top/pay-success";//

    private String merchantTransactionSecureType = "AUTO"; // NO

    private String merchantSignature; //\\

    private String orderReference; //
    private long orderDate; //
    private int amount; //
    private String currency = "UAH"; //
    private List<String> productName = new ArrayList<>(Collections.singleton("Донат на розвиток додатка")); //
    private List<Integer> productPrice = new ArrayList<>(); //
    private List<Integer> productCount = new ArrayList<>(); //

    public PaymentByWayForPay() {
    }

    @Override
    public String toString() {
        return "PaymentByWayForPay{" +
                "merchantAccount='" + merchantAccount + '\'' +
                ", merchantDomainName='" + merchantDomainName + '\'' +
                ", merchantTransactionSecureType='" + merchantTransactionSecureType + '\'' +
                ", merchantSignature='" + merchantSignature + '\'' +
                ", orderReference='" + orderReference + '\'' +
                ", orderDate=" + orderDate +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
