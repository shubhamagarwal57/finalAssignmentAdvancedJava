package com.hashedin.currencyconversionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CurrencyConversion {
    private Long id;
    private String from;
    private String to;
    private BigDecimal quantity;
    private BigDecimal conversionMultiple;
    private BigDecimal totalCalculatedAmount;

    public CurrencyConversion(long id, String from, String to, BigDecimal quantity) {
        this.id=id;
        this.from=from;
        this.to=to;
        this.quantity=quantity;
    }
}
