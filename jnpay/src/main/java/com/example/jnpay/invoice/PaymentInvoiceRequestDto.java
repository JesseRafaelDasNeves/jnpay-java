package com.example.jnpay.invoice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record PaymentInvoiceRequestDto(
        @NotNull(message = "O valor pago é obrigatório") @DecimalMin(value = "0.1", message = "O valor pago deve ser de no mínimo 0.1") Double paidAmount) {
}
