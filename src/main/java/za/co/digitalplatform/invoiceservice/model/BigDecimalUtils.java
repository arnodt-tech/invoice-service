package za.co.digitalplatform.invoiceservice.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

    static public BigDecimal applyRounding(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    static public BigDecimal multiply(BigDecimal value1, BigDecimal value2) {
        return applyRounding(value1.multiply(value2));
    }

    static public BigDecimal multiply(BigDecimal value1, Long value2) {
        return multiply(value1, BigDecimal.valueOf(value2));
    }

    static public BigDecimal multiply(BigDecimal value1, double value2) {
        return multiply(value1, BigDecimal.valueOf(value2));
    }

    static public BigDecimal divide(BigDecimal value, BigDecimal divisor) {
        return applyRounding(value.divide(divisor));
    }

    static public BigDecimal add(BigDecimal value1, BigDecimal value2) {
        return applyRounding(value1.add(value2));
    }
}
