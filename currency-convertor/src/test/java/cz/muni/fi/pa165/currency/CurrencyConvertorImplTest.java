package cz.muni.fi.pa165.currency;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyConvertorImplTest {

    private static Currency currencyCZK = Currency.getInstance("CZK");
    private static Currency currencyEUR = Currency.getInstance("EUR");

    @Mock
    private ExchangeRateTable exchangeRateTable;

    private CurrencyConvertor currencyConvertor;

    @Before
    public void init() {
        currencyConvertor = new CurrencyConvertorImpl(exchangeRateTable);
    }

    @Test
    public void testConvert() throws ExternalServiceFailureException {
        // Don't forget to test border values and proper rounding.
        when(exchangeRateTable.getExchangeRate(currencyEUR, currencyCZK))
                .thenReturn(new BigDecimal("25.00"));

        assertEquals(currencyConvertor.convert(currencyEUR, currencyCZK, new BigDecimal("1")), new BigDecimal("25.00"));
        assertEquals(currencyConvertor.convert(currencyEUR, currencyCZK, new BigDecimal("2")), new BigDecimal("50.00"));
        assertEquals(currencyConvertor.convert(currencyEUR, currencyCZK, new BigDecimal("1.25")), new BigDecimal("31.25"));
        assertEquals(currencyConvertor.convert(currencyEUR, currencyCZK, new BigDecimal("1.121")), new BigDecimal("28.02"));
        assertEquals(currencyConvertor.convert(currencyEUR, currencyCZK, new BigDecimal("1.00492")), new BigDecimal("25.12"));
    }

    @Test
    public void testConvertWithNullSourceCurrency() {
        assertThatThrownBy(() -> {
            currencyConvertor.convert(null, currencyCZK, new BigDecimal("1"));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithNullTargetCurrency() {
        assertThatThrownBy(() -> {
            currencyConvertor.convert(currencyEUR, null, new BigDecimal("1"));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithNullSourceAmount() {
        assertThatThrownBy(() -> {
            currencyConvertor.convert(currencyEUR, currencyCZK, null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testConvertWithUnknownCurrency() {
        assertThatThrownBy(() -> {
            currencyConvertor.convert(currencyEUR, Currency.getInstance("EUR"), new BigDecimal("1"));
        }).isInstanceOf(UnknownExchangeRateException.class);
    }

    @Test
    public void testConvertWithExternalServiceFailure() throws ExternalServiceFailureException {
        when(exchangeRateTable.getExchangeRate(currencyEUR, currencyCZK))
                .thenThrow(ExternalServiceFailureException.class);

        assertThatThrownBy(() -> {
            currencyConvertor.convert(currencyEUR, currencyCZK, new BigDecimal("1"));
        }).isInstanceOf(ExternalServiceFailureException.class);
    }

}
