package com.hashedin.currencyconversionservice.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@ApiModel
public class CurrencyConversionDto {
    @NotBlank(message = "from_currency field is required")
     @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for from field")
    private String from;

    @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for to field")
    @NotBlank(message = "to_currency field is required")
    private String to;

    @NotNull(message = "quantity cannot be empty")
    @Min(value = 1, message = "quantity should be greater than 0")
    private BigDecimal quantity;
}
