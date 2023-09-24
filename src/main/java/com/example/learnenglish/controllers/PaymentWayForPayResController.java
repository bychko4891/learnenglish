package com.example.learnenglish.controllers;

import com.example.learnenglish.model.PaymentByWayForPay;
import com.example.learnenglish.responsemessage.CustomResponseMessage;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import com.example.learnenglish.service.PaymentWayForPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.SimpleDateFormat;

@RestController
@RequiredArgsConstructor
public class PaymentWayForPayResController {
    private final PaymentWayForPayService paymentWayForPayService;
    private final HttpSession session;

    @PostMapping("/start-payment")
    public ResponseEntity<PaymentByWayForPay> startPay(@RequestBody PaymentByWayForPay payment) {
        PaymentByWayForPay paymentByWayForPay = paymentWayForPayService.startOfPayment(payment);

        session.setAttribute("orderReference", paymentByWayForPay.getOrderReference());


        return ResponseEntity.ok(paymentWayForPayService.startOfPayment(paymentByWayForPay));

    }


    @PostMapping("/api/pay-success/{orderReference}")
    public RedirectView endPayment(@PathVariable("orderReference") String pathOrderReference,
                                   @RequestParam("merchantAccount") String merchantAccount,
                                   @RequestParam("merchantSignature") String merchantSignature,
                                   @RequestParam("orderReference") String orderReference,
                                   @RequestParam("amount") int amount,
                                   @RequestParam("currency") String currency,
                                   @RequestParam("authCode") String authCode,
                                   @RequestParam("email") String email,
                                   @RequestParam("phone") String phone,
                                   @RequestParam("createdDate") long createdDate,
                                   @RequestParam("processingDate") long processingDate,
                                   @RequestParam("cardPan") String cardPan,
                                   @RequestParam("cardType") String cardType,
                                   @RequestParam("issuerBankCountry") String issuerBankCountry,
                                   @RequestParam("issuerBankName") String issuerBankName,
                                   @RequestParam("transactionStatus") String transactionStatus,
                                   @RequestParam("reason") String reason,
                                   @RequestParam("reasonCode") String reasonCode,
                                   @RequestParam("fee") double fee,
                                   @RequestParam("paymentSystem") String paymentSystem,
                                   RedirectAttributes redirectAttributes) {
        if (pathOrderReference.equals(orderReference)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            PaymentByWayForPay payment = new PaymentByWayForPay();
            payment.setMerchantSignature(merchantSignature);
            payment.setOrderReference(orderReference);
            payment.setAmount(amount);
            payment.setCurrency(currency);
            payment.setAuthCode(authCode);
            payment.setEmail(email);
            payment.setPhone(phone);
            payment.setCreatedDate(dateFormat.format(createdDate * 1000));
            payment.setCardPan(cardPan);
            payment.setCardType(cardType);
            payment.setIssuerBankCountry(issuerBankCountry);
            payment.setIssuerBankName(issuerBankName);
            payment.setTransactionStatus(transactionStatus);
            payment.setReason(reason);
            payment.setReasonCode(reasonCode);
            payment.setPaymentSystem(paymentSystem);
            CustomResponseMessage responseMessage = paymentWayForPayService.endOfPayment(payment);
            redirectAttributes.addAttribute("message", reasonCode);
            if (responseMessage.getStatus().equalsIgnoreCase("success")) {
                redirectAttributes.addAttribute("message", "success");
                return  new RedirectView("/payment-success");
            }
        }
        return new RedirectView("/pay");
    }


}
