package io.pleo.antaeus.core.services.processor.stateFlow

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.exceptionCor.ExceptionHandler
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ExceptionEncounteredProcessorStateImplTest {

    private val networkExceptionHandler = mockk<ExceptionHandler>() {
        every { handleException(any()) } returns Unit
    }
    private val unKnownErrorExceptionHandler = mockk<ExceptionHandler>() {
        every { handleException(any()) } returns Unit
    }
    private val customerNotFoundExceptionHandler = mockk<ExceptionHandler>() {
        every { handleException(any()) } returns Unit
    }
    private val currencyMismatchExceptionHandler = mockk<ExceptionHandler>() {
        every { handleException(any()) } returns Unit
    }

    private val mapOfExceptionHandler = HashMap<String, ExceptionHandler>()

    @BeforeEach
    internal fun setUp() {
        mapOfExceptionHandler[ExceptionType.CURRENCY_MISMATCH.name] = currencyMismatchExceptionHandler
        mapOfExceptionHandler[ExceptionType.NETWORK.name] = networkExceptionHandler
        mapOfExceptionHandler[ExceptionType.CUSTOMER_NOT_FOUND.name] = customerNotFoundExceptionHandler
        mapOfExceptionHandler[ExceptionType.UNKNOWN.name] = unKnownErrorExceptionHandler
    }

    @Test
    fun `get Name Equals PAYMENT_SUCCESSFUL_STATE`() {
        val exceptionEncounteredProcessorStateImpl = ExceptionEncounteredProcessorStateImpl()
        assertEquals(
            BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name,
            exceptionEncounteredProcessorStateImpl.getStateType()
        )
    }

    @Test
    fun ` next state equals QUERY_QUEUE_STATUS_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
        }

        val requestAdapter = mockk<RequestAdapter> {
            every { getInvoicesProcessorAdapters() } returns list
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        billingProcessRequest.mapOfExceptionHandler = mapOfExceptionHandler
        billingProcessRequest.exception = NetworkException()
        val exceptionEncounteredProcessorStateImpl = ExceptionEncounteredProcessorStateImpl()
        exceptionEncounteredProcessorStateImpl.handleRequest(billingProcessRequest)


        assertEquals(
            BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE,
            billingProcessRequest.state
        )
    }

    @Test
    fun ` exceptionHandler is invoked`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
        }

        val requestAdapter = mockk<RequestAdapter> {
            every { getInvoicesProcessorAdapters() } returns list
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )

        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        billingProcessRequest.mapOfExceptionHandler = mapOfExceptionHandler
        billingProcessRequest.exception = NetworkException()

        val exceptionEncounteredProcessorStateImpl = ExceptionEncounteredProcessorStateImpl()
        exceptionEncounteredProcessorStateImpl.handleRequest(billingProcessRequest)

        val exceptionHandler = mapOfExceptionHandler[ExceptionType.NETWORK.name]
        verify(exactly = 1) {
            exceptionHandler?.handleException(billingProcessRequest)
        }
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