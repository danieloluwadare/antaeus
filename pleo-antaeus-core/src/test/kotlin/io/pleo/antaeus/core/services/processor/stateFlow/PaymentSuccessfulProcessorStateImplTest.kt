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

class PaymentSuccessfulProcessorStateImplTest {
    @Test
    fun `get Name Equals PAYMENT_SUCCESSFUL_STATE`() {
        val paymentSuccessfulProcessorStateImpl = PaymentSuccessfulProcessorStateImpl()
        assertEquals(
            BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE.name,
            paymentSuccessfulProcessorStateImpl.getStateType()
        )
    }

    @Test
    fun ` must update Invoice to Paid`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
            every { setComplete() } returns Unit
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
        val paymentSuccessfulProcessorStateImpl = PaymentSuccessfulProcessorStateImpl()
        paymentSuccessfulProcessorStateImpl.handleRequest(billingProcessRequest)

        verify(exactly = 1) {
            invoiceService.updateInvoiceStatus(invoice.id, InvoiceStatus.PAID)
        }
    }

    @Test
    fun ` next state equals QUERY_QUEUE_STATUS_STATE`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter> {
            every { getInvoice() } returns invoice
            every { setComplete() } returns Unit
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
        val paymentSuccessfulProcessorStateImpl = PaymentSuccessfulProcessorStateImpl()
        paymentSuccessfulProcessorStateImpl.handleRequest(billingProcessRequest)

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