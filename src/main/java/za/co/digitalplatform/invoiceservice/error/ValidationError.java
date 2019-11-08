package za.co.digitalplatform.invoiceservice.error;

import lombok.Getter;
import lombok.Setter;

public class ValidationError {
    @Getter
    @Setter
    private String code;
    @Getter
    @Setter
    private String message;
}
