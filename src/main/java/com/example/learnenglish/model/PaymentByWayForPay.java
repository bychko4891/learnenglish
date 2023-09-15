package com.example.learnenglish.model;

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Component
//@Entity
@Getter
@Setter
//@Table(name ="payments_by_way_for_pay")
public class PaymentByWayForPay {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column
//    private Long id;

//    @Transient
    private String merchantAccount; // +
//    @Transient
    private String merchantDomainName;//

//    @Transient
    private String returnUrl; //

//    @Transient
    private String merchantTransactionSecureType = "AUTO"; // NO
//    @Transient
    private String merchantSignature; //\\
//    @Column
    private String orderReference; // +
    private long orderDate; //
    private int amount; // +

//    @Transient
    private String currency = "UAH"; // +
//    @Column
    private String productName ="Донат на розвиток додатка"; //
    private Integer productPrice; //
    private Integer productCount; //

    private String authCode; // +
    private String email;
    private String phone;
    private long createdDate;
    private long processingDate;
    private String cardPan; // +
    private String cardType;
    private String issuerBankCountry;
    private String issuerBankName;
    private String transactionStatus; // +
    private String reason;
    private String reasonCode; // +
    private double fee;
    private String paymentSystem;
    private User user;

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
