package io.pleo.antaeus.core.external

import io.pleo.antaeus.models.Currency
import java.math.BigDecimal

/**
 *   mocked implementation of currency provider, an actual implementation will connect to an exchange rate service.
 */
class CurrencyConverterImpl : CurrencyConverter {
    override fun convert(amount: BigDecimal, fromCurrency: Currency, toCurrency: Currency): BigDecimal {
        return amount.multiply(BigDecimal(100))
    }
}