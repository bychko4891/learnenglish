package com.example.learnenglish.service;

import com.example.learnenglish.model.PaymentByWayForPay;
import com.example.learnenglish.model.WayForPayModule;
import com.example.learnenglish.repository.PaymentByWayForPayRepository;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import com.example.learnenglish.responsemessage.Message;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

// Не Буде змінюватись в роботу Артур
@Service
@RequiredArgsConstructor
public class PaymentWayForPayService {
    @Value(("${application.host}"))
    private String host;
    private final WayForPayModuleService wayForPayModuleService;
    private final PaymentByWayForPayRepository paymentByWayForPayRepository;

    private WayForPayModule wayForPayModule;

    private String generateMerchantSignatureMD5(String data) {
        System.out.println(data);
        String algorithm = "HmacMD5";
        String key = wayForPayModule.getMerchantSecretKey();
        return new HmacUtils(algorithm, key).hmacHex(data);
    }


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
        }
        return paymentWayForPay;
    }

    public CustomResponseMessage endOfPayment(PaymentByWayForPay paymentWayForPay) {
        paymentWayForPay.setMerchantAccount(wayForPayModule.getMerchantAccount());
        String data = paymentWayForPay.getMerchantAccount() + ";" + paymentWayForPay.getOrderReference() + ";"
                + paymentWayForPay.getAmount() + ";" + paymentWayForPay.getCurrency() + ";" + paymentWayForPay.getAuthCode() + ";"
                + paymentWayForPay.getCardPan() + ";" + paymentWayForPay.getTransactionStatus() + ";" + paymentWayForPay.getReasonCode();
        String merchantSignature = generateMerchantSignatureMD5(data);
        if (merchantSignature.equals(paymentWayForPay.getMerchantSignature())) {
            paymentByWayForPayRepository.save(paymentWayForPay);
            if (paymentWayForPay.getReasonCode().equals("1100")) {
                return new CustomResponseMessage(Message.SUCCESS);
            }
        }
        return new CustomResponseMessage(Message.ERROR);
    }

    public String paymentCodeResponseMessage(String paymentCode) {
        return switch (paymentCode) {
            case "1101" -> "Не вдалося здійснити оплату. Зв'яжіться з вашим банком або скористайтеся іншою картою";
            case "1102" ->
                    "Не вдалося здійснити оплату. Будь ласка, переконайтеся в правильності введення параметрів і спробуйте ще";
            case "1103" ->
                    "Не вдалося здійснити оплату. Зв'яжіться з вашим банком або скористайтеся іншою картою. Будь ласка, переконайтеся в правильності введення установок і спробуйте ще";
            case "1104" -> "Не вдалося здійснити оплату. Недостатньо коштів на карті";
            case "1105" ->
                    "Не вдалося здійснити оплату. Зв'яжіться з вашим банком або скористайтесь іншою карткою. Будь ласка, переконайтеся в правильності введення параметрів і спробуйте ще";
            case "1106" -> "Не вдалося здійснити оплату. Зв'яжіться з вашим банком або скористайтесь іншою карткою.";
            case "1108" -> "Зв'яжіться з вашим банком або скористайтесь іншою карткою.";
            case "1109" -> "Не вдалося здійснити оплату. Повторіть спробу оплати пізніше або зв'яжіться з торговцем в адресу котрого здійснюєте платіж.";
            default ->
                    "Не вдалося здійснити оплату. Повторіть спробу оплати пізніше або зв'яжіться з торговцем в адресу котрого здійснюєте платіж";
        };
    }
}
