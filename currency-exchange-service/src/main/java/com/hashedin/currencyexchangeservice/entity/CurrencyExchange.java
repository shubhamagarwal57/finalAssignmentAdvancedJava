package com.hashedin.currencyexchangeservice.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
public class CurrencyExchange {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "currency_from", nullable=false)
    private String from;
    @Column(name = "currency_to",nullable=false)
    private  String to;
    @Column(nullable = false)
    private BigDecimal conversionMultiple;

    public CurrencyExchange(long id, String from, String to, BigDecimal conversionMultiple) {
        this.id=id;
        this.from=from;
        this.to=to;
        this.conversionMultiple=conversionMultiple;
    }
}
