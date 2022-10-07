package com.hashedin.currencyexchangeservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hashedin.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.hashedin.currencyexchangeservice.entity.CurrencyExchange;
import com.hashedin.currencyexchangeservice.exception.ResourceNotFoundException;
import com.hashedin.currencyexchangeservice.repository.CurrencyExchangeRepository;
import com.hashedin.currencyexchangeservice.service.CurrencyExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeServiceImpl.class);
    @Override
    @CacheEvict(value="currencyExchange", allEntries = true)
    public CurrencyExchangeDto createExchangeRecord(CurrencyExchangeDto currencyExchangeDto) throws JsonProcessingException {
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndToIgnoreCase(currencyExchangeDto.getFrom(), currencyExchangeDto.getTo());
        System.out.println(currencyExchangeDto.getFrom());
        if(currencyExchange==null){
            CurrencyExchange theCurrencyExchange = mapToEntity(currencyExchangeDto);
            CurrencyExchange newCurrencyExchange = currencyExchangeRepository.saveAndFlush(theCurrencyExchange);
            logger.info("Saved CurrencyExchange Object : " + new ObjectMapper().writeValueAsString(newCurrencyExchange));
            return mapToDto(newCurrencyExchange);
        }
        else{
            CurrencyExchange theCurrencyExchange = currencyExchangeRepository.getById(currencyExchange.getId());
            theCurrencyExchange.setTo(currencyExchangeDto.getTo());
            theCurrencyExchange.setFrom(currencyExchangeDto.getFrom());
            theCurrencyExchange.setConversionMultiple(currencyExchangeDto.getConversionMultiple());
            CurrencyExchange newCurrencyExchange = currencyExchangeRepository.saveAndFlush(theCurrencyExchange);
            logger.info("Saved CurrencyExchange Object : " + new ObjectMapper().writeValueAsString(newCurrencyExchange));
            return mapToDto(newCurrencyExchange);
        }
    }

    @Override
    @Cacheable("currencyExchange")
    public List<CurrencyExchange> getAllExchangeRecord() {
        List<CurrencyExchange> currencyExchanges = currencyExchangeRepository.findAll();
        logger.info("inside getAllExchangeRecord()");
       return currencyExchanges.stream().collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value="currencyExchange", allEntries = true)
    public CurrencyExchange getExchangeByFromAndTo(String from, String to) {
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndToIgnoreCase(from, to);
        if(currencyExchange==null)
            throw new ResourceNotFoundException("mapping",from,to);
        logger.info("getExchangeByFromAndTo() called with {} to {}",from,to);
        return currencyExchange;
    }

    @Override
    @CacheEvict(value="currencyExchange", allEntries = true)
    public CurrencyExchange updateExchangeRecord(CurrencyExchangeDto currencyExchangeDto) throws JsonProcessingException {
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndToIgnoreCase(currencyExchangeDto.getFrom(), currencyExchangeDto.getTo());
        if(currencyExchange==null)
            throw new ResourceNotFoundException("mapping", currencyExchangeDto.getFrom(), currencyExchangeDto.getTo());
        else{
            CurrencyExchange theCurrencyExchange = currencyExchangeRepository.getById(currencyExchange.getId());
            theCurrencyExchange.setTo(currencyExchangeDto.getTo());
            theCurrencyExchange.setFrom(currencyExchangeDto.getFrom());
            theCurrencyExchange.setConversionMultiple(currencyExchangeDto.getConversionMultiple());
            CurrencyExchange newCurrencyExchange = currencyExchangeRepository.save(theCurrencyExchange);
            logger.info("Updated CurrencyExchange Object updateExchangeRecord()  : " + new ObjectMapper().writeValueAsString(newCurrencyExchange));
            return newCurrencyExchange;
        }
    }

    @Override
    @CacheEvict(value="currencyExchange", allEntries = true)
    public CurrencyExchange deleteExchangeByFromAndTo(String from, String to) {
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndToIgnoreCase(from,to);
        if(currencyExchange==null)
            throw new ResourceNotFoundException("mapping", from,to);
        currencyExchangeRepository.delete(currencyExchange);
        logger.info("deleteExchangeByFromAndTo() called with {} to {}",from,to);
        return currencyExchange;

    }

    public CurrencyExchangeDto mapToDto(CurrencyExchange currencyExchange){
        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto();
        currencyExchangeDto.setTo(currencyExchange.getTo());
        currencyExchangeDto.setFrom(currencyExchange.getFrom());
        currencyExchangeDto.setConversionMultiple(currencyExchange.getConversionMultiple());
        return currencyExchangeDto;
    }

    public CurrencyExchange mapToEntity(CurrencyExchangeDto currencyExchangeDto){
        CurrencyExchange currencyExchange = new CurrencyExchange();
        currencyExchange.setTo(currencyExchangeDto.getTo());
        currencyExchange.setFrom(currencyExchangeDto.getFrom());
        currencyExchange.setConversionMultiple(currencyExchangeDto.getConversionMultiple());
        return currencyExchange;
    }
}
