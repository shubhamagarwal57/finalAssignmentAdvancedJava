package com.hashedin.currencyexchangeservice.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ApiModel("DTO for currencyExchange class")
public class CurrencyExchangeDto {
    @NotBlank(message = "from_currency field is required")
    @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for from field")
    private String from;

    @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for to field")
    @NotBlank(message = "to_currency field is required")
    private String to;

    @NotNull(message = "Conversion Multiple cannot be empty")
    @Min(value = 1, message = "Conversion Multiple should be greater than 0")
    private BigDecimal conversionMultiple;
}
