package com.hashedin.currencyexchangeservice.repository;

import com.hashedin.currencyexchangeservice.entity.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange,Long> {
    CurrencyExchange findByFromAndToIgnoreCase(String from, String to);
    //Optional<CurrencyExchange> findById(Long id);
}
