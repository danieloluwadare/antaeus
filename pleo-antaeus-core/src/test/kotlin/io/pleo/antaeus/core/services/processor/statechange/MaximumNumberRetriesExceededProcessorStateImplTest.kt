package io.pleo.antaeus.core.services.processor.statechange

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.afterStateChangeAction.AfterStateChangeService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class MaximumNumberRetriesExceededProcessorStateImplTest{
    @Test
    fun `get Name Equals START_STATE`() {
        val maximumNumberRetriesExceededProcessorStateImpl = MaximumNumberRetriesExceededProcessorStateImpl();
        assertEquals(
            BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name,
            maximumNumberRetriesExceededProcessorStateImpl.getStateType())
    }

    @Test
    fun ` next state must equal QUERY_QUEUE_STATUS_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getCounter() } returns 3
            every { getInvoice() } returns invoice
        }

        val invoiceService = mockk<InvoiceService>(){
            every { updateInvoiceStatus(any(),any()) } returns Unit
        }

        val afterStateChangeService = mockk<AfterStateChangeService>(){
            every { initiate(any()) } returns Unit
        }
        val map = HashMap<String, AfterStateChangeService>()
        map[BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name] = afterStateChangeService;

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getMaximumRetryCount() } returns 2
            every { getInvoiceService() } returns invoiceService
            every { getAfterStateChangeService() } returns map
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val maximumNumberRetriesExceededProcessorStateImpl = MaximumNumberRetriesExceededProcessorStateImpl();
        maximumNumberRetriesExceededProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE, billingProcessRequest.state)
    }

    @Test
    fun ` must update Invoice to Failed`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getCounter() } returns 3
            every { getInvoice() } returns invoice
        }

        val invoiceService = mockk<InvoiceService>(){
            every { updateInvoiceStatus(any(),any()) } returns Unit
        }

        val afterStateChangeService = mockk<AfterStateChangeService>(){
            every { initiate(any()) } returns Unit
        }
        val map = HashMap<String, AfterStateChangeService>()
        map[BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name] = afterStateChangeService;

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getMaximumRetryCount() } returns 2
            every { getInvoiceService() } returns invoiceService
            every { getAfterStateChangeService() } returns map
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val maximumNumberRetriesExceededProcessorStateImpl = MaximumNumberRetriesExceededProcessorStateImpl();
        maximumNumberRetriesExceededProcessorStateImpl.handleRequest(billingProcessRequest)


        verify(exactly = 1) {
            invoiceService.updateInvoiceStatus(invoice.id,InvoiceStatus.FAILED)
        }
    }

    @Test
    fun ` must invoke afterStateChangeService`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getCounter() } returns 3
            every { getInvoice() } returns invoice
        }

        val invoiceService = mockk<InvoiceService>(){
            every { updateInvoiceStatus(any(),any()) } returns Unit
        }

        val afterStateChangeService = mockk<AfterStateChangeService>(){
            every { initiate(any()) } returns Unit
        }
        val map = HashMap<String, AfterStateChangeService>()
        map[BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name] = afterStateChangeService;

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getMaximumRetryCount() } returns 2
            every { getInvoiceService() } returns invoiceService
            every { getAfterStateChangeService() } returns map
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val maximumNumberRetriesExceededProcessorStateImpl = MaximumNumberRetriesExceededProcessorStateImpl();
        maximumNumberRetriesExceededProcessorStateImpl.handleRequest(billingProcessRequest)

        verify(exactly = 1) {
            invoiceService.updateInvoiceStatus(invoice.id,InvoiceStatus.FAILED)
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