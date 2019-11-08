package za.co.digitalplatform.invoiceservice.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class InvoiceUnitTest {

    private LineItem createLineItem(Long quantity, Double unitPrice){
        LineItem lineItem = new LineItem();
        lineItem.setQuantity(quantity);
        lineItem.setUnitPrice(BigDecimal.valueOf(unitPrice));

        return lineItem;
    }

    private Invoice createInvoice(){
        Invoice invoice = new Invoice();

        invoice.setVatRate(15L);
        invoice.getLineItems().add(createLineItem(2L,12.5));
        invoice.getLineItems().add(createLineItem(4L,6.25));
        invoice.getLineItems().add(createLineItem(3L,1.24));

        return invoice;
    }

    @Test
    public void invoice_calculate_subtotal(){
        Assertions.assertTrue(BigDecimal.valueOf(53.72).compareTo(createInvoice().getSubTotal()) == 0);
    }

    @Test
    public void invoice_calculate_vat(){
        Assertions.assertTrue(BigDecimal.valueOf(8.06).compareTo(createInvoice().getVat()) == 0);
    }

    @Test
    public void invoice_calculate_total(){
        Assertions.assertTrue(BigDecimal.valueOf(61.78).compareTo(createInvoice().getTotal()) == 0);
    }
}
