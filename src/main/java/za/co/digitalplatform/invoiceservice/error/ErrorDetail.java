package za.co.digitalplatform.invoiceservice.error;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorDetail {
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private Integer status;
    @Getter
    @Setter
    private String detail;
    @Getter
    @Setter
    private Date timeStamp;
    @Getter
    @Setter
    private Map<String, List<ValidationError>> errors = new HashMap<>();
}
