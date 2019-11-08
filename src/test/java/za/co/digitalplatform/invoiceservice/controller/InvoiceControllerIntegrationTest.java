package za.co.digitalplatform.invoiceservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import za.co.digitalplatform.invoiceservice.error.ErrorDetail;
import za.co.digitalplatform.invoiceservice.model.Invoice;
import za.co.digitalplatform.invoiceservice.model.LineItem;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class InvoiceControllerIntegrationTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    public static String requestBody(Object request) {
        try {
            return MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseResponse(MvcResult result, Class<T> responseClass) {
        try {
            String contentAsString = result.getResponse().getContentAsString();
            return MAPPER.readValue(contentAsString, responseClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LineItem createLineItem(String description, Long quantity, Double unitPrice) {
        LineItem lineItem = new LineItem();
        lineItem.setQuantity(quantity);
        lineItem.setUnitPrice(BigDecimal.valueOf(unitPrice));
        lineItem.setDescription(description);

        return lineItem;
    }

    @Test
    public void add_invoice_success() {
        Invoice invoice = new Invoice();

        invoice.setClient("Client01");
        invoice.setVatRate(15L);
        invoice.setInvoiceDate(Calendar.getInstance().getTime());
        invoice.getLineItems().add(createLineItem("Item01", 2L, 12.5));
        invoice.getLineItems().add(createLineItem("Item02", 4L, 6.25));
        invoice.getLineItems().add(createLineItem("Item03", 3L, 1.24));


        try {

            MvcResult requestResult = mockMvc.perform(post("/invoices").contentType(MediaType.APPLICATION_JSON).content(requestBody(invoice)))
                    .andExpect(status().isCreated())
                    .andReturn();

            Invoice invoiceResponse = parseResponse(requestResult, Invoice.class);


            Assertions.assertNotNull(invoiceResponse.getClient());
            Assertions.assertEquals(invoice.getClient(), invoiceResponse.getClient());
            Assertions.assertEquals(invoice.getVatRate(), invoiceResponse.getVatRate());
            Assertions.assertEquals(invoice.getInvoiceDate(), invoiceResponse.getInvoiceDate());
            Assertions.assertEquals(invoice.getLineItems().size(), 3);
            Assertions.assertTrue(BigDecimal.valueOf(53.72).compareTo(invoiceResponse.getSubTotal()) == 0);
            Assertions.assertTrue(BigDecimal.valueOf(8.06).compareTo(invoiceResponse.getVat()) == 0);
            Assertions.assertTrue(BigDecimal.valueOf(61.78).compareTo(invoiceResponse.getTotal()) == 0);


            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void add_invoice_invalid_vatRate() {
        Invoice invoice = new Invoice();

        invoice.setClient("Client01");
        invoice.setVatRate(-1L);
        invoice.setInvoiceDate(Calendar.getInstance().getTime());


        try {

            MvcResult requestResult = mockMvc.perform(post("/invoices").contentType(MediaType.APPLICATION_JSON).content(requestBody(invoice)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            ErrorDetail errorDetail = parseResponse(requestResult, ErrorDetail.class);


            Assertions.assertEquals(errorDetail.getStatus(), HttpStatus.BAD_REQUEST.value());
            Assertions.assertNotNull(errorDetail.getErrors());
            Assertions.assertTrue(!errorDetail.getErrors().isEmpty());


            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.assertTrue(false);
        }
    }

    @Test
    public void add_invoice_invalid_date() {
        Invoice invoice = new Invoice();

        invoice.setClient("Client01");
        invoice.setVatRate(15L);
        invoice.setInvoiceDate(null);


        try {

            MvcResult requestResult = mockMvc.perform(post("/invoices").contentType(MediaType.APPLICATION_JSON).content(requestBody(invoice)))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            ErrorDetail errorDetail = parseResponse(requestResult, ErrorDetail.class);


            Assertions.assertEquals(errorDetail.getStatus(), HttpStatus.BAD_REQUEST.value());
            Assertions.assertNotNull(errorDetail.getErrors());
            Assertions.assertTrue(!errorDetail.getErrors().isEmpty());


            Assertions.assertTrue(true);
        } catch (Exception e) {
            Assertions.assertTrue(false);
        }
    }
}
