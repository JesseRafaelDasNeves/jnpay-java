package com.example.jnpay.invoice;

import lombok.Getter;

@Getter
public enum InvoiceStatus {
    PENDING("PENDENTE"),
    PARTIALLY_PAID("PARCIALMENTE_PAGO"),
    PAID("PAGO");

    private final String value;

    InvoiceStatus(String value) {
        this.value = value;
    }
}
