package com.example.learnenglish.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.example.learnenglish.service.PaymentWayForPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PaymentWayForPayResController {
    private final PaymentWayForPayService paymentWayForPayService;
    private final HttpSession session;

    @GetMapping("/pay")
    public String pay() {
        return "pay";
    }

    @PostMapping("/api/pay-success/{orderReference}")
    public String endPayment(@PathVariable("orderReference") String pathOrderReference,

                             @RequestParam("merchantAccount") String merchantAccount,
                             @RequestParam("merchantSignature") String merchantSignature,
                             @RequestParam("orderReference") String orderReference,
                             @RequestParam("amount") String amount,
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
                             Model model) {
        String sessionOrderReference = (String) session.getAttribute("orderReference");

        return "pay";
    }


}
