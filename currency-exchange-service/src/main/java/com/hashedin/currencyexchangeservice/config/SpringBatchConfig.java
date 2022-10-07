package com.hashedin.currencyexchangeservice.config;

import com.hashedin.currencyexchangeservice.entity.CurrencyExchange;
import com.hashedin.currencyexchangeservice.repository.CurrencyExchangeRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class SpringBatchConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private CurrencyExchangeRepository currencyExchangeRepository;


    @Bean
    public FlatFileItemReader<CurrencyExchange> reader() {
        FlatFileItemReader<CurrencyExchange> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/currencyExchange.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<CurrencyExchange> lineMapper() {
        DefaultLineMapper<CurrencyExchange> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "from", "to", "conversionMultiple");

        BeanWrapperFieldSetMapper<CurrencyExchange> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CurrencyExchange.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

    @Bean
    public CurrencyExchangeProcessor processor() {
        return new CurrencyExchangeProcessor();
    }

    @Bean
    public RepositoryItemWriter<CurrencyExchange> writer() {
        RepositoryItemWriter<CurrencyExchange> writer = new RepositoryItemWriter<>();
        writer.setRepository(currencyExchangeRepository);
        writer.setMethodName("saveAndFlush");
        return writer;
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<CurrencyExchange, CurrencyExchange>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importCurrencyExchangeRates")
                .flow(step1()).end().build();

    }

}
