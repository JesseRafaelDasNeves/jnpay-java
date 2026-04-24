package com.example.jnpay.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jnpay.model.Invoice;
import com.example.jnpay.model.InvoiceRepository;

@RestController
@RequestMapping("invoice")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping
    public List<Invoice> getAll() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return invoices;
    }
}
