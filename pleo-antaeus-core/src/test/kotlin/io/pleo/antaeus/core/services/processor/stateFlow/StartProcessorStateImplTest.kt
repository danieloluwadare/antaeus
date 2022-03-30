package io.pleo.antaeus.core.services.processor.stateFlow

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.BillProcessorFlowState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class StartProcessorStateImplTest {

    @Test
    fun `queue must be queried and reduce in size`() {
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
        val startProcessorStateImpl = StartProcessorStateImpl();
        startProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(list.size - 1, billingProcessRequest.queue.size)
    }

    @Test
    fun `currentInvoiceProcess must be initialized`() {
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
        val startProcessorStateImpl = StartProcessorStateImpl();
        startProcessorStateImpl.handleRequest(billingProcessRequest)

        assertNotNull(billingProcessRequest.currentInvoiceProcess)
        assertEquals(invoiceProcessorAdapter, billingProcessRequest.currentInvoiceProcess)
    }

    @Test
    fun `next state equals VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE`() {
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
        val startProcessorStateImpl = StartProcessorStateImpl();
        startProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE, billingProcessRequest.state)
    }

    @Test
    fun `get Name Equals START_STATE`() {
        val startProcessorStateImpl = StartProcessorStateImpl();
        assertEquals(BillProcessorFlowState.START_STATE.name, startProcessorStateImpl.getStateType())
    }
}