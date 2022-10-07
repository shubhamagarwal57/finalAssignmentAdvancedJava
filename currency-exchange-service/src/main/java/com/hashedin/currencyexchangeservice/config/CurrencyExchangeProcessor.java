package com.hashedin.currencyexchangeservice.config;

import com.hashedin.currencyexchangeservice.entity.CurrencyExchange;
import com.hashedin.currencyexchangeservice.repository.CurrencyExchangeRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;

public class CurrencyExchangeProcessor implements ItemProcessor<CurrencyExchange,CurrencyExchange> {
    @Autowired
    CurrencyExchangeRepository currencyExchangeRepository;

    /**
     * This method preprocesses and checks if to from mapping already present. If present it updates the exchange value else adds new records
     * @param currencyExchange
     * @return currencyExchange Object
     * @throws Exception
     */
    @Override
    @CacheEvict(value="currencyExchange", allEntries = true)
    public CurrencyExchange process(CurrencyExchange currencyExchange) throws Exception {
        CurrencyExchange currencyExchange1 = currencyExchangeRepository.findByFromAndToIgnoreCase(currencyExchange.getFrom(), currencyExchange.getTo());
        if(currencyExchange1!=null){
            currencyExchange1.setConversionMultiple(currencyExchange.getConversionMultiple());
            currencyExchangeRepository.save(currencyExchange1);
            return null;
        }
        else {
            return currencyExchange;
        }
    }
}