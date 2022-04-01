package io.pleo.antaeus.core.exceptionCor

import io.mockk.mockk
import io.pleo.antaeus.core.cor.ExceptionHandlerBuilder
import io.pleo.antaeus.core.external.CurrencyConverter
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.models.ExceptionType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExceptionHandlerBuilderTest {

    private val currencyConverter = mockk<CurrencyConverter>()
    private val customerService = mockk<CustomerService>()

    private val mapOfExceptionHandler = ExceptionHandlerBuilder.buildChain(currencyConverter,customerService)


    @Test
    fun `mapOfExceptionHandler must contain NETWORK`() {
        assertTrue(mapOfExceptionHandler.containsKey(ExceptionType.NETWORK.name))
    }

    @Test
    fun `mapOfExceptionHandler must contain CURRENCY_MISMATCH`() {
        assertTrue(mapOfExceptionHandler.containsKey(ExceptionType.CURRENCY_MISMATCH.name))
    }

    @Test
    fun `mapOfExceptionHandler must contain CUSTOMER_NOT_FOUND`() {
        assertTrue(mapOfExceptionHandler.containsKey(ExceptionType.CUSTOMER_NOT_FOUND.name))
    }
}