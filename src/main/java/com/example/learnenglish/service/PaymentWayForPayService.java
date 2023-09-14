package com.example.learnenglish.service;

import com.example.learnenglish.model.PaymentByWayForPay;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Service
public class PaymentWayForPayService {

    public String generateMerchantSignatureMD5(PaymentByWayForPay paymentWayForPay) {
        String data = paymentWayForPay.getMerchantAccount() + ";" + paymentWayForPay.getMerchantDomainName() + ";" + paymentWayForPay.getOrderReference() + ";" +
                 paymentWayForPay.getOrderDate() + ";" + paymentWayForPay.getAmount() + ";" + paymentWayForPay.getCurrency() + ";" +
                 paymentWayForPay.getProductName().get(0) + ";" + paymentWayForPay.getProductCount().get(0) + ";" + paymentWayForPay.getProductPrice().get(0);
        System.out.println(data);
        String algorithm = "HmacMD5";
        String key = "3bafd97458e4e54cf71d09fb22c3094e3f3a192b";
        String merchantSignature = new HmacUtils(algorithm, key).hmacHex(data);
        return merchantSignature;
    }


//    public String buildUrl(PaymentByWayForPay paymentWayForPay) {
    public PaymentByWayForPay buildUrl(PaymentByWayForPay paymentWayForPay) {
        String orderReference = UUID.randomUUID().toString().replaceAll("-", "");

        paymentWayForPay.setOrderDate(new Date().getTime());
        paymentWayForPay.setOrderReference(orderReference);
        paymentWayForPay.setAmount(paymentWayForPay.getProductPrice().get(0));
        String merchantSignature = generateMerchantSignatureMD5(paymentWayForPay);
        paymentWayForPay.setMerchantSignature(merchantSignature);
        System.out.println(merchantSignature);

        // Створюємо UriComponentsBuilder
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://secure.wayforpay.com/pay")
//                .queryParam("merchantAccount", paymentWayForPay.getMerchantAccount())
//                .queryParam("merchantDomainName", paymentWayForPay.getMerchantDomainName())
////                .queryParam("returnUrl", returnUrl)
//                .queryParam("merchantTransactionSecureType", paymentWayForPay.getMerchantTransactionSecureType())
//                .queryParam("merchantSignature", paymentWayForPay.getMerchantSignature())
//                .queryParam("orderReference", paymentWayForPay.getOrderReference())
//                .queryParam("orderDate", paymentWayForPay.getOrderDate())
//                .queryParam("amount", paymentWayForPay.getAmount())
//                .queryParam("currency", paymentWayForPay.getCurrency())
//                .queryParam("productName", encodeValue((paymentWayForPay.getProductName().get(0))))
//                .queryParam("productPrice", paymentWayForPay.getProductPrice().get(0))
//                .queryParam("productCount", paymentWayForPay.getProductCount().get(0));

        // Повертаємо URL-рядок
//        return builder.toUriString();
        return paymentWayForPay;
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
