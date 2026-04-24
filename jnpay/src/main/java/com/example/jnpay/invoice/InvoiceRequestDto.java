package com.example.jnpay.invoice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record InvoiceRequestDto(
        @NotBlank(message = "O número da fatura é obrigatório.")
        @Size(max = 100, message = "O número da fatura não pode ter mais de 100 caracteres.")
        String number,

        @NotNull(message = "A data de emissão é obrigatória.")
        LocalDate issueDate,

        Double paidAmount,

        @NotBlank(message = "O status da fatura é obrigatório.")
        @Pattern(regexp = "PENDENTE|PARCIALMENTE_PAGO|PAGO", message = "O status deve ser um dos seguintes: PENDENTE, PARCIALMENTE_PAGO ou PAGO.")
        String status,

        List<@Valid InvoiceItemRequestDto> items) {

    public Invoice toInvoice() {
        Invoice invoice = new Invoice();
        this.fillInvoice(invoice);
        return invoice;
    }

    public void fillInvoice(Invoice invoice) {
        invoice.setNumber(number);
        invoice.setIssueDate(issueDate);
        invoice.setPaidAmount(paidAmount);
        invoice.setStatus(status);

        if (items != null) {
            List<InvoiceItem> newItems = items.stream()
                    .map(dto -> dto.toEntity(invoice))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (invoice.getItems() != null) {
                invoice.getItems().clear();
                invoice.getItems().addAll(newItems);
            } else {
                invoice.setItems(newItems);
            }
        }
    }
}
