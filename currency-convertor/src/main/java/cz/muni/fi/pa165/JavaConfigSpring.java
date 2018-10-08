package cz.muni.fi.pa165;

import cz.muni.fi.pa165.currency.CurrencyConvertor;
import cz.muni.fi.pa165.currency.CurrencyConvertorImpl;
import cz.muni.fi.pa165.currency.ExchangeRateTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.inject.Inject;

@Configuration
@ComponentScan("cz.muni.fi.pa165")
@EnableAspectJAutoProxy
public class JavaConfigSpring {
    @Inject
    ExchangeRateTable exchangeRateTable;

    @Bean
    public CurrencyConvertor getCurrencyConvertor() {
        System.out.println("Creating currency convertor");
        return new CurrencyConvertorImpl(exchangeRateTable);
    }
}
