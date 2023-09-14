package com.example.learnenglish.controllers;

import org.springframework.ui.Model;
import com.example.learnenglish.model.PaymentByWayForPay;
import com.example.learnenglish.service.PaymentWayForPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class PaymentWayForPayResController {
    private final PaymentWayForPayService paymentWayForPayService;

    @GetMapping("/pay")
    public String pay(){
        return "pay";
    }

    @ResponseBody
    @PostMapping("/pay-success")
    public String paySuccess(@RequestParam("merchantAccount") String merchantAccount,
                             @RequestParam("merchantSignature") String merchantSignature,
                             @RequestParam("orderReference") String orderReference,
                             @RequestParam("amount") String amount, Model model) {
        // jsonResponse - це рядок JSON, який ви отримали від зовнішнього API




        return "pay";
    }


}
