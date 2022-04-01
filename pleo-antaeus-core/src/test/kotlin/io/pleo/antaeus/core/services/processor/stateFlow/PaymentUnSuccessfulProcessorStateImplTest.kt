package io.pleo.antaeus.core.services.processor.stateFlow

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.afterStateChangeAction.AfterStateChangeService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PaymentUnSuccessfulProcessorStateImplTest {

    @Test
    fun `get Name Equals PAYMENT_UNSUCCESSFUL_STATE`() {
        val paymentUnSuccessfulProcessorStateImpl = PaymentUnSuccessfulProcessorStateImpl()
        assertEquals(
            BillProcessorFlowState.PAYMENT_UNSUCCESSFUL_STATE.name,
            paymentUnSuccessfulProcessorStateImpl.getStateType()
        )
    }

    @Test
    fun ` must update Invoice to Failed`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
        }

        val invoiceService = mockk<InvoiceService> {
            every { updateInvoiceStatus(any(), any()) } returns Unit
        }

        val requestAdapter = mockk<RequestAdapter> {
            every { getInvoicesProcessorAdapters() } returns list
            every { getInvoiceService() } returns invoiceService
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        val paymentUnSuccessfulProcessorStateImpl = PaymentUnSuccessfulProcessorStateImpl()
        paymentUnSuccessfulProcessorStateImpl.handleRequest(billingProcessRequest)

        verify(exactly = 1) {
            invoiceService.updateInvoiceStatus(invoice.id, InvoiceStatus.FAILED)
        }
    }

    @Test
    fun ` next state equals QUERY_QUEUE_STATUS_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
        }

        val invoiceService = mockk<InvoiceService> {
            every { updateInvoiceStatus(any(), any()) } returns Unit
        }

        val requestAdapter = mockk<RequestAdapter> {
            every { getInvoicesProcessorAdapters() } returns list
            every { getInvoiceService() } returns invoiceService
        }

        val billingProcessRequest = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        billingProcessRequest.currentInvoiceProcess = invoiceProcessorAdapter
        val paymentUnSuccessfulProcessorStateImpl = PaymentUnSuccessfulProcessorStateImpl()
        paymentUnSuccessfulProcessorStateImpl.handleRequest(billingProcessRequest)

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