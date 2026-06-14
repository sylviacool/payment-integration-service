package com.paymentintegration.helper;

import com.paymentintegration.constant.Constants;
import com.paymentintegration.dto.paypal.createorder.request.Amount;
import com.paymentintegration.dto.paypal.createorder.request.CreateOrderRequest;
import com.paymentintegration.dto.paypal.createorder.request.ExperienceContext;
import com.paymentintegration.dto.paypal.createorder.request.PaymentSource;
import com.paymentintegration.dto.paypal.createorder.request.Paypal;
import com.paymentintegration.dto.paypal.createorder.request.PurchaseUnit;
import com.paymentintegration.dto.request.CreateOrderReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class CreateOrderHelper {    //helps to converts frontend DTO → PayPal DTO

    public CreateOrderRequest buildCreateOrderRequest(CreateOrderReq createOrderReq) {

        log.info("Building PayPal CreateOrderRequest");

        ExperienceContext experienceContext =
                new ExperienceContext();

        experienceContext.setReturnUrl(
                createOrderReq.getReturnUrl()
        );

        experienceContext.setCancelUrl(
                createOrderReq.getCancelUrl()
        );

        experienceContext.setUserAction(
                Constants.PAY_NOW
        );

        Paypal paypal = new Paypal();

        paypal.setExperienceContext(experienceContext);

        PaymentSource paymentSource =
                new PaymentSource();

        paymentSource.setPaypal(paypal);

        Amount amount = new Amount();

        amount.setCurrencyCode(
                createOrderReq.getCurrency()
        );

        amount.setValue(
                createOrderReq.getAmount()
        );

        PurchaseUnit purchaseUnit =
                new PurchaseUnit();

        purchaseUnit.setAmount(amount);

        CreateOrderRequest createOrderRequest =
                new CreateOrderRequest();

        createOrderRequest.setIntent(
                Constants.CAPTURE
        );

        createOrderRequest.setPaymentSource(
                paymentSource
        );

        createOrderRequest.setPurchaseUnits(
                List.of(purchaseUnit)
        );

        log.info("PayPal CreateOrderRequest built successfully");

        return createOrderRequest;
    }
}