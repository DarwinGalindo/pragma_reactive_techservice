package com.darwin.techservice.infrastructure.configuration;

import com.darwin.techservice.domain.api.ITechnologyServicePort;
import com.darwin.techservice.domain.spi.ITechnologyPersistencePort;
import com.darwin.techservice.domain.usecase.TechnologyUseCase;
import com.darwin.techservice.infrastructure.output.jpa.adapter.TechnologyR2dbcAdapter;
import com.darwin.techservice.infrastructure.output.jpa.mapper.TechnologyEntityMapper;
import com.darwin.techservice.infrastructure.output.jpa.repository.ITechnologyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@RequiredArgsConstructor
@Configuration
public class BeanConfiguration {
    private final ITechnologyRepository technologyRepository;
    private final TechnologyEntityMapper technologyEntityMapper;

    @Bean
    public ITechnologyServicePort technologyServicePort() {
        return new TechnologyUseCase(technologyPersistencePort());
    }

    @Bean
    public ITechnologyPersistencePort technologyPersistencePort() {
        return new TechnologyR2dbcAdapter(technologyRepository, technologyEntityMapper);
    }

    @Bean
    @Primary
    public Validator springValidator() {
        return new LocalValidatorFactoryBean();
    }
}
