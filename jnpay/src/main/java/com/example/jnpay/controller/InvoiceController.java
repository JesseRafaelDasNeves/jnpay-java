package com.example.jnpay.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jnpay.invoice.Invoice;
import com.example.jnpay.invoice.InvoiceRepository;
import com.example.jnpay.invoice.InvoiceRequestDto;
import com.example.jnpay.invoice.InvoiceStatus;
import com.example.jnpay.invoice.InvoiceResponseDto;
import com.example.jnpay.invoice.PaymentInvoiceRequestDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("invoice")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping
    public List<InvoiceResponseDto> getAll() {
        List<InvoiceResponseDto> invoices = invoiceRepository.findAll().stream().map(InvoiceResponseDto::new).toList();
        return invoices;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable @NonNull Long id) {
        return invoiceRepository.findById(id)
                .map(invoice -> ResponseEntity.ok(new InvoiceResponseDto(invoice)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public InvoiceResponseDto createInvoice(@RequestBody @Valid InvoiceRequestDto data) {
        Invoice invoice = data.toInvoice();
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setUpdatedAt(LocalDateTime.now());
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return new InvoiceResponseDto(savedInvoice);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> updateInvoice(@PathVariable @NonNull Long id,
            @RequestBody @Valid InvoiceRequestDto data) {
        return invoiceRepository.findById(id).map(invoice -> {
            data.fillInvoice(invoice);
            invoice.setUpdatedAt(LocalDateTime.now());
            Invoice updatedInvoice = invoiceRepository.save(invoice);
            return ResponseEntity.ok(new InvoiceResponseDto(updatedInvoice));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable @NonNull Long id) {
        if (invoiceRepository.existsById(id)) {
            invoiceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/pay")
    @Transactional
    public ResponseEntity<InvoiceResponseDto> updatePay(@PathVariable @NonNull Long id,
            @RequestBody @Valid PaymentInvoiceRequestDto data) {
        return invoiceRepository.findById(id).map(invoice -> {
            double currentPayment = invoice.getPaidAmount() != null ? invoice.getPaidAmount() : 0.0;
            double totalAmount = invoice.getItems().stream()
                    .mapToDouble(item -> item.getAmount() != null ? item.getAmount() : 0.0)
                    .sum();

            double paidAmountInput = data.paidAmount() != null ? data.paidAmount() : 0.0;
            double paidAmountNew = paidAmountInput + currentPayment;

            if (paidAmountNew > totalAmount) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O valor pago não pode ser superior ao total da fatura.");
            }

            double paidProportion = totalAmount > 0 ? paidAmountNew / totalAmount : 0;

            invoice.setPaidAmount(paidAmountNew);
            invoice.setStatus(paidAmountNew == totalAmount ? InvoiceStatus.PAID.getValue()
                    : InvoiceStatus.PARTIALLY_PAID.getValue());
            invoice.setUpdatedAt(LocalDateTime.now());

            invoice.getItems().forEach(item -> {
                item.setPercentagePaid(paidProportion * 100);
            });

            Invoice updatedInvoice = invoiceRepository.save(invoice);
            return ResponseEntity.ok(new InvoiceResponseDto(updatedInvoice));
        }).orElse(ResponseEntity.notFound().build());
    }
}
