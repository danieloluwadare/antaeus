package io.pleo.antaeus.core.services.processor.stateFlow

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.BillProcessorFlowState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ValidateMaximumNumberRetriesNotExceededProcessorStateImplTest {

    @Test
    fun `get Name Equals START_STATE`() {
        val validateMaximumNumberRetriesExceededProcessorStateImpl =
            ValidateMaximumNumberRetriesExceededProcessorStateImpl();
        assertEquals(
            BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name,
            validateMaximumNumberRetriesExceededProcessorStateImpl.getStateType()
        )
    }

    @Test
    fun `currentInvoiceProcess count is greater than MaximumRetryCount next state must equal MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>() {
            every { getCounter() } returns 3
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
        val validateMaximumNumberRetriesExceededProcessorStateImpl =
            ValidateMaximumNumberRetriesExceededProcessorStateImpl();
        validateMaximumNumberRetriesExceededProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE, billingProcessRequest.state)
    }

    @Test
    fun `currentInvoiceProcess count is less than MaximumRetryCount next state must equal MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>() {
            every { getCounter() } returns 3
        }
        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list

            every { getMaximumRetryCount() } returns 5
        }
        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        val validateMaximumNumberRetriesExceededProcessorStateImpl =
            ValidateMaximumNumberRetriesExceededProcessorStateImpl();
        validateMaximumNumberRetriesExceededProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE, billingProcessRequest.state)
    }

    @Test
    fun `currentInvoiceProcess count is equal to MaximumRetryCount next state must equal MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>() {
            every { getCounter() } returns 3
        }
        val requestAdapter = mockk<RequestAdapter>() {
            every { getInvoicesProcessorAdapters() } returns list

            every { getMaximumRetryCount() } returns 3
        }
        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        val validateMaximumNumberRetriesExceededProcessorStateImpl =
            ValidateMaximumNumberRetriesExceededProcessorStateImpl();
        validateMaximumNumberRetriesExceededProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE, billingProcessRequest.state)
    }
}