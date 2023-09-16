package com.example.learnenglish.controllers;

import org.springframework.ui.Model;
import com.example.learnenglish.responsemessage.ResponseMessage;
import com.example.learnenglish.service.PaymentWayForPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class PaymentWayForPayController {

    private final PaymentWayForPayService paymentWayForPayService;

    @GetMapping("/pay")
    public String pay() {
        return "pay";
    }

    @GetMapping("/payment-success")
    public String paymentSuccess(@RequestParam(value = "message", required = false) String message, Model model){
        message = "Дякую за донат";
        model.addAttribute("message", message);



        return "pay";
    }

}
