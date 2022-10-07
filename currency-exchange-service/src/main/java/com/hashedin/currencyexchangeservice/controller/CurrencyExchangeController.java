package com.hashedin.currencyexchangeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hashedin.currencyexchangeservice.dto.CurrencyExchangeDto;
import com.hashedin.currencyexchangeservice.entity.CurrencyExchange;
import com.hashedin.currencyexchangeservice.exception.ResourceNotFoundException;
import com.hashedin.currencyexchangeservice.repository.CurrencyExchangeRepository;
import com.hashedin.currencyexchangeservice.service.impl.CurrencyExchangeServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@Validated
@RequestMapping(value = "/api/currency-exchange-service")
@Api(value = "controller which exposes currency-exchange related api")
public class CurrencyExchangeController {
    @Autowired
    private CurrencyExchangeRepository repository;
    @Autowired
    private CurrencyExchangeServiceImpl currencyExchangeService;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    private final Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);
    @GetMapping("/from/{from}/to/{to}")
    @ApiOperation("Rest Api to retrieve currency exchange record by from and to")
    public CurrencyExchange retrieveExchangeValue(
            @PathVariable @NotBlank(message = "from_currency field is required") @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for from field")
            String from,
            @PathVariable @NotBlank(message = "from_currency field is required") @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for to field")
            String to){
        logger.info("retrieveExchangeValue() called with {} to {}",from,to);
        CurrencyExchange currencyExchange = repository.findByFromAndToIgnoreCase(from, to);
        if(currencyExchange==null)
            throw new ResourceNotFoundException("mapping",from,to);
        return currencyExchange;
    }

    @PostMapping
    @ApiOperation("Rest Api to create currency exchange record")
    public ResponseEntity<CurrencyExchangeDto> createCurrencyExchangeMapping(@Valid @RequestBody CurrencyExchangeDto currencyExchangeDto) throws JsonProcessingException {
        return new ResponseEntity<>(currencyExchangeService.createExchangeRecord(currencyExchangeDto), HttpStatus.CREATED);
    }

    @GetMapping
    @ApiOperation("Rest Api to get all currency exchange rates")
    public ResponseEntity<List<CurrencyExchange>> getAllCurrencyExchangeRates(){
        return new ResponseEntity<>(currencyExchangeService.getAllExchangeRecord(),HttpStatus.OK);
    }


    @ApiOperation("Rest Api to delete exchange record by from and to")
    @DeleteMapping("/from/{from}/to/{to}")
    public ResponseEntity<CurrencyExchange> deleteCurrencyExchange(@PathVariable @NotBlank(message = "from_currency field is required") @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for from field")
                                                                   String from,
                                                                   @PathVariable @NotBlank(message = "from_currency field is required") @Pattern(regexp = "^[a-zA-Z]{3}$", flags = { Pattern.Flag.CASE_INSENSITIVE, Pattern.Flag.MULTILINE }, message = "3 Alphabet code req for to field")
                                                                   String to){
        return new ResponseEntity<CurrencyExchange>(currencyExchangeService.deleteExchangeByFromAndTo(from, to), HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation("Rest Api to update currency exchange record ")
    public ResponseEntity<CurrencyExchange> updateCurrencyExchange(@Valid @RequestBody CurrencyExchangeDto currencyExchangeDto) throws JsonProcessingException {
        return new ResponseEntity<CurrencyExchange>(currencyExchangeService.updateExchangeRecord(currencyExchangeDto),HttpStatus.OK);
    }

    @PostMapping("/import")
    @ApiOperation("Rest Api to import the records from csv file located at src/main/resources/currencyExchange.csv using spring batch ")
    public void importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
