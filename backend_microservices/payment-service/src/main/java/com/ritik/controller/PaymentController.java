package com.ritik.controller;

import com.razorpay.RazorpayException;
import com.ritik.domain.PaymentMethod;
import com.ritik.modal.PaymentOrder;
import com.ritik.payload.dto.BookingDTO;
import com.ritik.payload.dto.UserDTO;
import com.ritik.payload.response.PaymentLinkResponse;
import com.ritik.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            @RequestBody BookingDTO bookingDTO,
            @RequestParam PaymentMethod paymentMethod
            ) throws RazorpayException {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFullName("ritik");
        userDTO.setEmail("ritik@gmail.com");
        PaymentLinkResponse paymentLinkResponse = paymentService.createOrder(userDTO, bookingDTO, paymentMethod);
        return ResponseEntity.ok(paymentLinkResponse);
    }

    @GetMapping("/{paymentOrderOd}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(
            @PathVariable Long paymentOrderOd
    ) throws Exception {
        return ResponseEntity.ok(paymentService.getPaymentOrderById(paymentOrderOd));
    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(
            @RequestParam String paymentId,
            @RequestParam String paymentLinkId
    ) throws Exception {

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentLinkId(paymentLinkId);

        return ResponseEntity.ok(paymentService.proceedPayment(paymentOrder,paymentId,paymentLinkId));
    }




}
