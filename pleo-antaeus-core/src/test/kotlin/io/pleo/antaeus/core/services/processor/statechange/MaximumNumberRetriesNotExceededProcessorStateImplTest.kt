package io.pleo.antaeus.core.services.processor.statechange

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.BillProcessorFlowState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class MaximumNumberRetriesNotExceededProcessorStateImplTest {

    @Test
    fun `get Name Equals START_STATE`() {
        val maximumNumberRetriesNotExceededProcessorStateImpl = MaximumNumberRetriesNotExceededProcessorStateImpl();
        assertEquals(
            BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE.name,
            maximumNumberRetriesNotExceededProcessorStateImpl.getStateType()
        )
    }

    @Test
    fun ` next state must equal INVOKE_PAYMENT_PROVIDER_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>() {
            every { getCounter() } returns 1
        }
        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list
            every { getMaximumRetryCount() } returns 2
        }
        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        val maximumNumberRetriesNotExceededProcessorStateImpl = MaximumNumberRetriesNotExceededProcessorStateImpl();
        maximumNumberRetriesNotExceededProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.INVOKE_PAYMENT_PROVIDER_STATE, billingProcessRequest.state)
    }
}