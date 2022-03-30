package io.pleo.antaeus.core.services.processor.statechange

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.BillProcessorFlowState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class QueryQueueStatusProcessorStateImplTest {

    @Test
    fun `get Name Equals START_STATE`() {
        val queryQueueStatusProcessorStateImpl = QueryQueueStatusProcessorStateImpl();
        assertEquals(
            BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE.name,
            queryQueueStatusProcessorStateImpl.getStateType()
        )
    }

    @Test
    fun `when queue is empty next state must be STOP_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list
        }
        var billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        val queryQueueStatusProcessorStateImpl = QueryQueueStatusProcessorStateImpl();
        queryQueueStatusProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.STOP_STATE, billingProcessRequest.state)
    }

    @Test
    fun `when queue is not empty next state must be START_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()
        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>()
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)
        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list
        }
        var billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        val queryQueueStatusProcessorStateImpl = QueryQueueStatusProcessorStateImpl();
        queryQueueStatusProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.START_STATE, billingProcessRequest.state)
    }

}