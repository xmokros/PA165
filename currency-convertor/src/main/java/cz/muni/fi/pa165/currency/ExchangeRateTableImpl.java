package cz.muni.fi.pa165.currency;

import javax.inject.Named;
import java.math.BigDecimal;
import java.util.Currency;

@Named
public class ExchangeRateTableImpl implements ExchangeRateTable {
    @Override
    public BigDecimal getExchangeRate(Currency sourceCurrency, Currency targetCurrency) throws ExternalServiceFailureException {
        if (!sourceCurrency.equals(Currency.getInstance("EUR")) || !targetCurrency.equals(Currency.getInstance("CZK"))) {
            return null;
        }

        return new BigDecimal("27.00");
    }
}
