package io.pleo.antaeus.core.services.processor.stateFlow

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.afterStateChangeAction.AfterStateChangeService
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ExceptionEncounteredProcessorStateImplTest {
    @Test
    fun `get Name Equals PAYMENT_SUCCESSFUL_STATE`() {
        val exceptionEncounteredProcessorStateImpl = ExceptionEncounteredProcessorStateImpl()
        assertEquals(
            BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name,
            exceptionEncounteredProcessorStateImpl.getStateType()
        )
    }

    @Test
    fun ` must invoke afterStateChangeService`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
        }
        val afterStateChangeService = mockk<AfterStateChangeService> {
            every { initiate(any()) } returns Unit
        }
        val map = HashMap<String, AfterStateChangeService>()
        map[BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name] = afterStateChangeService

        val requestAdapter = mockk<RequestAdapter> {
            every { getInvoicesProcessorAdapters() } returns list
            every { getAfterStateChangeService() } returns map
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        val exceptionEncounteredProcessorStateImpl = ExceptionEncounteredProcessorStateImpl()
        exceptionEncounteredProcessorStateImpl.handleRequest(billingProcessRequest)

        verify(exactly = 1) {
            afterStateChangeService.initiate(billingProcessRequest)
        }
    }

    @Test
    fun ` next state equals QUERY_QUEUE_STATUS_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
        }

        val afterStateChangeService = mockk<AfterStateChangeService> {
            every { initiate(any()) } returns Unit
        }
        val map = HashMap<String, AfterStateChangeService>()
        map[BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name] = afterStateChangeService

        val requestAdapter = mockk<RequestAdapter> {
            every { getInvoicesProcessorAdapters() } returns list
            every { getAfterStateChangeService() } returns map
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        val exceptionEncounteredProcessorStateImpl = ExceptionEncounteredProcessorStateImpl()
        exceptionEncounteredProcessorStateImpl.handleRequest(billingProcessRequest)

        verify(exactly = 1) {
            afterStateChangeService.initiate(billingProcessRequest)
        }

        assertEquals(
            BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE,
            billingProcessRequest.state
        )
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