package com.example.jnpay.invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InvoiceResponseDto(
        Long id,
        String number,
        LocalDate issueDate,
        Double paidAmount,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        java.util.List<InvoiceItemResponseDto> items) {

    public InvoiceResponseDto(Invoice invoice) {
        this(invoice.getId(), invoice.getNumber(), invoice.getIssueDate(), invoice.getPaidAmount(), invoice.getStatus(),
                invoice.getCreatedAt(), invoice.getUpdatedAt(),
                invoice.getItems() != null ? invoice.getItems().stream().map(InvoiceItemResponseDto::new).toList()
                        : java.util.Collections.emptyList());
    }
}
