package io.pleo.antaeus.core.exceptionCor

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.cor.CurrencyMismatchExceptionHandler
import io.pleo.antaeus.core.cor.NetworkExceptionHandler
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.external.CurrencyConverter
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapterImpl
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CurrencyMismatchExceptionHandlerTest{

    @Test
    fun `queue size must increase`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice)

        val requestAdapter = mockk<RequestAdapter> {
            every { getInvoicesProcessorAdapters() } returns list
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )

        val currencyConverter = mockk<CurrencyConverter> {
            every { convert(any(),any(),any()) } returns BigDecimal.TEN
        }

        val customerService = mockk<CustomerService> {
            every { fetch(any())} returns Customer(1,Currency.EUR)
        }

        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        billingProcessRequest.exception = NetworkException()
        val prevSizeOfQueue = billingProcessRequest.queue.size

        val exceptionHandler = CurrencyMismatchExceptionHandler(currencyConverter=currencyConverter, customerService = customerService)
        exceptionHandler.handleException(billingProcessRequest)

        assertEquals(prevSizeOfQueue + 1, billingProcessRequest.queue.size)
    }

    private fun createInvoice(status: InvoiceStatus): Invoice {
        return Invoice(
            id = 10,
            customerId = 20,
            status = status,
            amount = Money(
                value =
                BigDecimal(100),
                currency = Currency.EUR
            )
        )
    }
}