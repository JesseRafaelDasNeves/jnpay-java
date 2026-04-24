package com.example.jnpay.invoice;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InvoiceItemRequestDto(
        @NotBlank(message = "A descrição do item é obrigatória.")
        @Size(max = 100, message = "A descrição do item não pode ter mais de 100 caracteres.")
        String description,

        @NotNull(message = "O valor do item é obrigatório.")
        @DecimalMin(value = "1", message = "O valor do item deve ser maior que 0.")
        Double amount,

        Double percentagePaid) {

    public InvoiceItem toEntity(Invoice invoice) {
        InvoiceItem item = new InvoiceItem();
        item.setDescription(description);
        item.setAmount(amount);
        item.setPercentagePaid(percentagePaid);
        item.setInvoice(invoice);
        return item;
    }
}
