package cz.muni.fi.pa165.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * This is base implementation of {@link CurrencyConvertor}.
 *
 * @author petr.adamek@embedit.cz
 */
public class CurrencyConvertorImpl implements CurrencyConvertor {

    private final ExchangeRateTable exchangeRateTable;
    private final Logger logger = LoggerFactory.getLogger(CurrencyConvertorImpl.class);

    public CurrencyConvertorImpl(ExchangeRateTable exchangeRateTable) {
        this.exchangeRateTable = exchangeRateTable;
    }


    @Override
    public BigDecimal convert(Currency sourceCurrency, Currency targetCurrency, BigDecimal sourceAmount)
            throws IllegalArgumentException, UnknownExchangeRateException, ExternalServiceFailureException {
        logger.trace("Converting from" + sourceCurrency + " to " + targetCurrency
            + " with amount of " + sourceAmount);
        if (sourceCurrency == null || targetCurrency == null || sourceAmount == null) {
            throw new IllegalArgumentException("Input values are null");
        }

        try {
            BigDecimal exchangeRate = exchangeRateTable.getExchangeRate(sourceCurrency, targetCurrency);

            if (exchangeRate == null) {
                logger.warn("Missing exchange rate");
                throw new UnknownExchangeRateException("Unknown exchange rate");
            }

            return sourceAmount.multiply(exchangeRate).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } catch (ExternalServiceFailureException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
