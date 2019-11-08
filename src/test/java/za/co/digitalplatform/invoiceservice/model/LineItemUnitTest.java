package za.co.digitalplatform.invoiceservice.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class LineItemUnitTest {

    @Test
    public void lineItemTotal_validate_calculation(){
        LineItem lineItem = new LineItem();

        lineItem.setQuantity(10L);
        lineItem.setUnitPrice(new BigDecimal(12.98));

        Assertions.assertTrue(BigDecimal.valueOf(129.80).compareTo(lineItem.getLineItemTotal()) == 0);
    }

    @Test
    public void lineItemTotal_validate_calculation_rounding_middle(){
        LineItem lineItem = new LineItem();

        lineItem.setQuantity(5L);
        lineItem.setUnitPrice(new BigDecimal(1.255));

        Assertions.assertTrue(BigDecimal.valueOf(6.27).compareTo(lineItem.getLineItemTotal()) == 0);
    }

    @Test
    public void lineItemTotal_validate_calculation_rounding_bottom(){
        LineItem lineItem = new LineItem();

        lineItem.setQuantity(4L);
        lineItem.setUnitPrice(new BigDecimal(1.211));

        Assertions.assertTrue(BigDecimal.valueOf(4.84).compareTo(lineItem.getLineItemTotal()) == 0);
    }

    @Test
    public void lineItemTotal_validate_calculation_rounding_top(){
        LineItem lineItem = new LineItem();

        lineItem.setQuantity(6L);
        lineItem.setUnitPrice(new BigDecimal(1.211));

        Assertions.assertTrue(BigDecimal.valueOf(7.27).compareTo(lineItem.getLineItemTotal()) == 0);
    }
}
