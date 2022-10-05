package com.app.payment.web;

import com.app.payment.model.Payment;
import com.app.payment.service.PaymentService;
import com.app.payment.web.dto.MakePaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @GetMapping("/{customerId}")
    public List<Payment> getAllByCustomerId(@PathVariable("customerId") String customerId) {
        return paymentService.getAllByCustomerId(customerId);
    }

    @PostMapping
    public void makePayment(@RequestBody MakePaymentDTO dto) {
        paymentService.chargeCard(dto);
    }

}
