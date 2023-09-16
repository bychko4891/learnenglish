package com.example.learnenglish.model;

import com.example.learnenglish.model.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


//@Component
@Entity
@Getter
@Setter
@Table(name ="payments_by_way_for_pay")
public class PaymentByWayForPay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Transient
    private String merchantAccount; // +
    @Transient
    private String merchantDomainName;//

    @Transient
    private String returnUrl; //

    @Transient
    private String merchantTransactionSecureType = "AUTO"; // NO
    @Transient
    private String merchantSignature; //\\
    @Column
    private String orderReference; // +
    @Transient
    private long orderDate; //
    @Column
    private int amount; // +

    @Transient
    private String currency = "UAH"; // +
    @Column
    private String productName ="Донат на розвиток додатка"; //
    @Transient
    private Integer productPrice; //
    @Transient
    private Integer productCount; //
    @Column
    private String authCode; // +
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String createdDate;
    @Transient
    private long processingDate;
    @Column
    private String cardPan; // +
    @Column
    private String cardType;
    @Column
    private String issuerBankCountry;

    @Column
    private String issuerBankName;
    @Column
    private String transactionStatus; // +
    @Column
    private String reason;
    @Column
    private String reasonCode; // +
    @Transient
    private double fee;

    @Column
    private String paymentSystem;
    @Transient
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
