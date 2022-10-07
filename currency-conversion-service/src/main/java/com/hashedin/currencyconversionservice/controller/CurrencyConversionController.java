package com.hashedin.currencyconversionservice.controller;

import com.hashedin.currencyconversionservice.dto.CurrencyConversionDto;
import com.hashedin.currencyconversionservice.entity.CurrencyConversion;
import com.hashedin.currencyconversionservice.proxy.CurrencyExchangeProxy;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@RestController
@Api("Api for currency-conversion-service")
@RequestMapping(value = "/api/currency-conversion-service")
public class CurrencyConversionController {
    @Autowired
    private CurrencyExchangeProxy proxy;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);

    @PostMapping
    @ApiOperation("Rest Api for calculating currency value based on exchange rates fetched from currency-exchange-service")
    public CurrencyConversion calculateCurrencyConversion(
            @Valid @RequestBody CurrencyConversionDto currencyConversionDto
            ){
        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(currencyConversionDto.getFrom(), currencyConversionDto.getTo());
        logger.info("CurrencyExchangeService called from CurrencyConversionService with {} to {}",currencyConversionDto.getFrom(),currencyConversion.getTo());
        return new CurrencyConversion(currencyConversion.getId(), currencyConversionDto.getFrom(), currencyConversionDto.getTo(), currencyConversionDto.getQuantity()
                ,currencyConversion.getConversionMultiple(),currencyConversionDto.getQuantity().multiply(currencyConversion.getConversionMultiple()));
    }

}
