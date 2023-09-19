package com.example.learnenglish.service;

import com.example.learnenglish.model.PaymentByWayForPay;
import com.example.learnenglish.model.WayForPayModule;
import com.example.learnenglish.responsemessage.Message;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

// Буде змінюватись
@Service
@RequiredArgsConstructor
public class PaymentWayForPayService {
    @Value(("${application.host}"))
    private String host;
    private final WayForPayModuleService wayForPayModuleService;

    private WayForPayModule wayForPayModule;

    private String generateMerchantSignatureMD5(String data) {
        System.out.println(data);
        String algorithm = "HmacMD5";
        String key = wayForPayModule.getMerchantSecretKEY();
        String merchantSignature = new HmacUtils(algorithm, key).hmacHex(data);
        System.out.println("merchantSignature: " + merchantSignature);
        return merchantSignature;
    }


    //    public String buildUrl(PaymentByWayForPay paymentWayForPay) {
    public PaymentByWayForPay startOfPayment(PaymentByWayForPay paymentWayForPay) {
        wayForPayModule = wayForPayModuleService.getWayForPayModule();
        if (wayForPayModule.isActive()) {
            String orderReference = UUID.randomUUID().toString().replaceAll("-", "");
            paymentWayForPay.setMerchantAccount(wayForPayModule.getMerchantAccount());
            paymentWayForPay.setOrderDate(new Date().getTime());
            paymentWayForPay.setOrderReference(orderReference);
            paymentWayForPay.setMerchantDomainName(host);
            paymentWayForPay.setReturnUrl(host + "/api/pay-success/" + orderReference);
            paymentWayForPay.setAmount(paymentWayForPay.getProductPrice());
            paymentWayForPay.setProductCount(1);
            String data = paymentWayForPay.getMerchantAccount() + ";" + paymentWayForPay.getMerchantDomainName() + ";" + paymentWayForPay.getOrderReference() + ";"
                    + paymentWayForPay.getOrderDate() + ";" + paymentWayForPay.getAmount() + ";" + paymentWayForPay.getCurrency() + ";"
                    + paymentWayForPay.getProductName() + ";" + paymentWayForPay.getProductCount() + ";" + paymentWayForPay.getProductPrice();

            String merchantSignature = generateMerchantSignatureMD5(data);
            paymentWayForPay.setMerchantSignature(merchantSignature);
            System.out.println(merchantSignature);

//            Timer timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
////                    userWordLessonStatisticService.deleteWordLessonStatistic(userId, wordLessonId);
//                    TimerStorage.removeTimer(orderReference);
//                    timer.cancel();
//                }
//            }, 10000);
//            TimerStorage.addTimer(orderReference, timer);
        }

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

    public CustomResponseMessage endOfPayment(PaymentByWayForPay paymentWayForPay){
        String data = paymentWayForPay.getMerchantAccount() + ";" + paymentWayForPay.getOrderReference() + ";"
                + paymentWayForPay.getAmount() + ";" + paymentWayForPay.getCurrency() + ";" + paymentWayForPay.getAuthCode() + ";"
                + paymentWayForPay.getCardPan() + ";" + paymentWayForPay.getTransactionStatus() + ";" + paymentWayForPay.getReasonCode();
        String merchantSignature = generateMerchantSignatureMD5(data);
        if(merchantSignature.equals(paymentWayForPay.getMerchantSignature())){
            System.out.println("yes ***************************");
            return new CustomResponseMessage(Message.SUCCESS);

        }


        return null;
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
