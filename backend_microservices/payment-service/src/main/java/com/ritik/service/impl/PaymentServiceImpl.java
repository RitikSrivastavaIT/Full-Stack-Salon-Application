package com.ritik.service.impl;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.ritik.domain.PaymentMethod;
import com.ritik.domain.PaymentOrderStatus;
import com.ritik.modal.PaymentOrder;
import com.ritik.payload.dto.BookingDTO;
import com.ritik.payload.dto.UserDTO;
import com.ritik.payload.response.PaymentLinkResponse;
import com.ritik.repository.PaymentOrderRepository;
import com.ritik.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentOrderRepository;

    @Value( "${razorpay.api.key}" )
    private String razorpayApiKey;

    @Value( "${razorpay.api.secret}" )
    private String razorpayApiSecretKey;

    @Override
    public PaymentLinkResponse createOrder(UserDTO userDTO,
                                           BookingDTO bookingDTO,
                                           PaymentMethod paymentMethod) throws RazorpayException {
        Long amount = (long)bookingDTO.getTotalPrice();

        PaymentOrder order = new PaymentOrder();
        order.setAmount(amount);
        order.setPaymentMethod(paymentMethod);
//        paymentOrder.setUserId(userDTO.getId());
        order.setBookingId(bookingDTO.getId());
        order.setSalonId(bookingDTO.getSalonId());
        PaymentOrder savedOrder = paymentOrderRepository.save(order);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if(paymentMethod == PaymentMethod.RAZORPAY){
            PaymentLink payment = createRazorPayPaymentLink(userDTO,
                    savedOrder.getAmount(),
                    savedOrder.getId());

            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");
            paymentLinkResponse.setPayment_link_url(paymentUrl);
            paymentLinkResponse.setPayment_link_id(paymentUrlId);

            savedOrder.setPaymentLinkId(paymentUrlId);

            paymentOrderRepository.save(savedOrder);
        }

        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) throws Exception {
        PaymentOrder paymentOrder = paymentOrderRepository.findById(id).orElse(null);
        if(paymentOrder == null){
            throw new Exception("Payment order not found for id :: " + id);
        }
        return paymentOrder;
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentLinkId(String paymentLinkId) {
        return paymentOrderRepository.findByPaymentLinkId(paymentLinkId);
    }

    @Override
    public PaymentLink createRazorPayPaymentLink(UserDTO userDTO,
                                                 Long Amount,
                                                 Long orderId) throws RazorpayException {
        Long amount = Amount * 100;

        RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiSecretKey);
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", userDTO.getFullName());
        customer.put("email", userDTO.getEmail());

        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);

        paymentLinkRequest.put("notify", notify);

        paymentLinkRequest.put("reminder_enable", true);

        paymentLinkRequest.put("callback_url","http://localhost:3000/payment-success/"+orderId);

        paymentLinkRequest.put("callback_method","get");

        return razorpayClient.paymentLink.create(paymentLinkRequest);
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder,
                                  String paymentId,
                                  String paymentLinkId) throws RazorpayException {
        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(razorpayApiKey,razorpayApiSecretKey);

                Payment payment = razorpayClient.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if(status.equals("captured")){
                    //produce kafka event to update booking status to paid
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepository.save(paymentOrder);
                    return true;
                }
                return false;
            }
        }
        return false;
    }
}
