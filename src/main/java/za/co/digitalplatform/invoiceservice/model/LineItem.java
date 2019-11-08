package za.co.digitalplatform.invoiceservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Entity
@Table(name = "LINE_ITEM")
@EqualsAndHashCode
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LineItem {
    @Id
    @Getter
    @Setter
    @GeneratedValue
    private Long id;
    @Getter
    @Setter
    @Min(value = 0, message = "Item quantity should not be less than 0")
    private Long quantity;
    @Getter
    @Setter
    @NotEmpty(message = "Item description  cannot be empty")
    private String description;
    @Getter
    @Setter
    @Min(value = 0, message = "Item unit price should not be less than 0")
    private BigDecimal unitPrice;

    @ApiModelProperty(readOnly = true, accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    @JsonProperty
    public BigDecimal getLineItemTotal() {
        return BigDecimalUtils.multiply(unitPrice, quantity);
    }
}

