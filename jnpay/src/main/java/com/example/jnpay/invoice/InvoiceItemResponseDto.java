package com.example.jnpay.invoice;

public record InvoiceItemResponseDto(
        Long id,
        String description,
        Double amount,
        Double percentagePaid) {

    public InvoiceItemResponseDto(InvoiceItem item) {
        this(item.getId(), item.getDescription(), item.getAmount(), item.getPercentagePaid());
    }
}
