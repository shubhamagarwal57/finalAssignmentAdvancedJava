package com.hashedin.currencyexchangeservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hashedin.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.hashedin.currencyexchangeservice.entity.CurrencyExchange;

import java.util.List;

public interface CurrencyExchangeService {
    CurrencyExchangeDto createExchangeRecord(CurrencyExchangeDto currencyExchangeDto) throws JsonProcessingException;
    List<CurrencyExchange> getAllExchangeRecord();
    CurrencyExchange getExchangeByFromAndTo(String from, String to);
    CurrencyExchange updateExchangeRecord(CurrencyExchangeDto currencyExchangeDto) throws JsonProcessingException;
    CurrencyExchange deleteExchangeByFromAndTo(String from, String to);
}
