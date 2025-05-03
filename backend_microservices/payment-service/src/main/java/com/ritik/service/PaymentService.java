package com.ritik.service;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.ritik.domain.PaymentMethod;
import com.ritik.modal.PaymentOrder;
import com.ritik.payload.dto.BookingDTO;
import com.ritik.payload.dto.UserDTO;
import com.ritik.payload.response.PaymentLinkResponse;

public interface PaymentService {

    PaymentLinkResponse createOrder(UserDTO userDTO,
                                    BookingDTO bookingDTO,
                                    PaymentMethod paymentMethod) throws RazorpayException;

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentLinkId(String paymentLinkId);

    PaymentLink createRazorPayPaymentLink(UserDTO userDTO,
                                          Long amount,
                                          Long orderId) throws RazorpayException;

    Boolean  proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException;
}
