package com.hashedin.currencyexchangeservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hashedin.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.hashedin.currencyexchangeservice.entity.CurrencyExchange;
import com.hashedin.currencyexchangeservice.repository.CurrencyExchangeRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class CurrencyExchangeServiceImplTest {
    @MockBean
    CurrencyExchangeRepository currencyExchangeRepository;

    @Autowired
    CurrencyExchangeServiceImpl currencyExchangeService;

    @Test
    void createExchangeRecord() throws JsonProcessingException {
        CurrencyExchange currencyExchange = CurrencyExchange.builder()
                .id(2L)
                .from("EUR")
                .to("INR")
                .conversionMultiple(BigDecimal.valueOf(100))
                .build();

        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto("EUR","INR",BigDecimal.valueOf(100));

        when(currencyExchangeRepository.save(currencyExchange)).thenReturn(currencyExchange);

        assertEquals(currencyExchange.getConversionMultiple(), currencyExchangeService.createExchangeRecord(currencyExchangeDto).getConversionMultiple());
    }

    @Test
    void getAllExchangeRecord() {
        when(currencyExchangeRepository.findAll()).thenReturn(List.of(
                CurrencyExchange.builder()
                        .id(1L)
                        .from("USD")
                        .to("INR")
                        .conversionMultiple(BigDecimal.valueOf(80))
                        .build(),
                CurrencyExchange.builder()
                        .id(2L)
                        .from("EUR")
                        .to("INR")
                        .conversionMultiple(BigDecimal.valueOf(100))
                        .build()
        ));
        assertEquals(2, currencyExchangeService.getAllExchangeRecord().size());
    }

    @Test
    void getExchangeByFromAndTo() {
        CurrencyExchange currencyExchange = CurrencyExchange.builder()
                .id(2L)
                .from("EUR")
                .to("INR")
                .conversionMultiple(BigDecimal.valueOf(100))
                .build();

         when(currencyExchangeRepository.existsById(2L)).thenReturn(true);
        when(currencyExchangeRepository.findById(2L)).thenReturn(Optional.ofNullable(currencyExchange));
        assertEquals(2L, currencyExchangeService.getExchangeByFromAndTo("EUR","INR").getId());
    }
}