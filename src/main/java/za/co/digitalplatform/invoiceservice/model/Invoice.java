package za.co.digitalplatform.invoiceservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "INVOICE")
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Invoice {
    @Id
    @Getter
    @Setter
    @GeneratedValue
    private Long id;
    @Getter
    @Setter
    @NotEmpty(message = "Client cannot be empty")
    private String client;
    @Getter
    @Setter
    @ApiModelProperty(value = "VAT expressed as percentage. Value between 0-100%")
    @Min(value = 0, message = "Vat rate is expressed as percentage and should be between 0 and 100")
    @Max(value = 100, message = "Vat rate is expressed as percentage and should be between 0 and 100")
    private Long vatRate;
    @Getter
    @Setter
    @NotNull(message = "Invoice date cannot be null")
    @ApiModelProperty(value = "Invoice date in ISO 8601 format \"2019-11-07T18:25:43+02:00\"")
    private Date invoiceDate;
    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private List<LineItem> lineItems = new ArrayList<>();

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty
    public BigDecimal getSubTotal() {
        if ((lineItems == null) || (lineItems.isEmpty())) {
            return new BigDecimal(0).setScale(2);
        }

        return BigDecimalUtils.applyRounding(lineItems
                .stream()
                .map(LineItem::getLineItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty
    public BigDecimal getVat() {
        return BigDecimalUtils.multiply(getSubTotal(), vatRate / 100.0);
    }

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty
    public BigDecimal getTotal() {
        return BigDecimalUtils.add(getSubTotal(), getVat());
    }
}
