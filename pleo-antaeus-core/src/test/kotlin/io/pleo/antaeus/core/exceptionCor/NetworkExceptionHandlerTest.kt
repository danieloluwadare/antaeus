package io.pleo.antaeus.core.exceptionCor

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.cor.NetworkExceptionHandler
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapterImpl
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

internal class NetworkExceptionHandlerTest {

    @Test
    fun ` invoice counter must be increased`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice)

        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        billingProcessRequest.exception = NetworkException()
        val prevCounter = billingProcessRequest.currentInvoiceProcess.getCounter()


        val networkExceptionHandler = NetworkExceptionHandler();
        networkExceptionHandler.handleException(billingProcessRequest)

        assertEquals(prevCounter + 1, billingProcessRequest.currentInvoiceProcess.getCounter())
    }

    @Test
    fun ` invoice delayNetworkCall must be true`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice)

        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        billingProcessRequest.exception = NetworkException()

        val networkExceptionHandler = NetworkExceptionHandler();
        networkExceptionHandler.handleException(billingProcessRequest)

        assertTrue(billingProcessRequest.currentInvoiceProcess.delayNetworkCall())
    }

    @Test
    fun `queue size must increase`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice)

        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        billingProcessRequest.exception = NetworkException()
        val prevSizeOfQueue = billingProcessRequest.queue.size

        val networkExceptionHandler = NetworkExceptionHandler();
        networkExceptionHandler.handleException(billingProcessRequest)

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