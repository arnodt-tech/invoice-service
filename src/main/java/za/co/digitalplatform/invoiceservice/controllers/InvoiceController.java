package za.co.digitalplatform.invoiceservice.controllers;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.co.digitalplatform.invoiceservice.error.ResourceNotFoundException;
import za.co.digitalplatform.invoiceservice.model.Invoice;
import za.co.digitalplatform.invoiceservice.repositories.InvoiceRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {
    @Autowired
    private InvoiceRepository invoiceRepository;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Invoice> addInvoice(@Validated  @RequestBody Invoice invoice) {
        invoice = invoiceRepository.saveAndFlush(invoice);

        return new ResponseEntity<>(invoice, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Invoice>> viewAllInvoices() {
        List<Invoice> invoices = invoiceRepository.findAll();

        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }

    @RequestMapping(value = "/{invoiceId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Invoice> viewInvoice(@PathVariable Long invoiceId) {
        Optional<Invoice> invoice = invoiceRepository.findById(invoiceId);

        if (!invoice.isPresent()) {
            throw new ResourceNotFoundException("Invoice with id " + invoiceId + " not found");
        }

        return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
    }
}
