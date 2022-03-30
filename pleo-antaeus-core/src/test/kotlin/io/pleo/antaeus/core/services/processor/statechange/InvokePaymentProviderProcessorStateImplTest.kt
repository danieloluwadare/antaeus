package io.pleo.antaeus.core.services.processor.statechange

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class InvokePaymentProviderProcessorStateImplTest{

    @Test
    fun `get Name Equals MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE`() {
        val invokePaymentProviderProcessorStateImpl = InvokePaymentProviderProcessorStateImpl();
        assertEquals(
            BillProcessorFlowState.INVOKE_PAYMENT_PROVIDER_STATE.name,
            invokePaymentProviderProcessorStateImpl.getStateType())
    }

    @Test
    fun ` must invoke PaymentProvider`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getInvoice() } returns invoice
        }

        val paymentProvider = mockk<PaymentProvider>(){
            every { charge(any()) } returns true
        }

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getPaymentProvider() } returns paymentProvider
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val invokePaymentProviderProcessorStateImpl = InvokePaymentProviderProcessorStateImpl();
        invokePaymentProviderProcessorStateImpl.handleRequest(billingProcessRequest)

        verify(exactly = 1) {
            paymentProvider.charge(invoice)
        }
    }

    @Test
    fun ` when PaymentProvider is Successful`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getInvoice() } returns invoice
        }

        val paymentProvider = mockk<PaymentProvider>(){
            every { charge(any()) } returns true
        }

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getPaymentProvider() } returns paymentProvider
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val invokePaymentProviderProcessorStateImpl = InvokePaymentProviderProcessorStateImpl();
        invokePaymentProviderProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(
            BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE,
            billingProcessRequest.state)
    }

    @Test
    fun ` when PaymentProvider is Unsuccessful`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getInvoice() } returns invoice
        }

        val paymentProvider = mockk<PaymentProvider>(){
            every { charge(any()) } returns false
        }

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getPaymentProvider() } returns paymentProvider
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val invokePaymentProviderProcessorStateImpl = InvokePaymentProviderProcessorStateImpl();
        invokePaymentProviderProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(
            BillProcessorFlowState.PAYMENT_UNSUCCESSFUL_STATE,
            billingProcessRequest.state)
    }

    @Test
    fun ` when PaymentProvider throws Exception`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getInvoice() } returns invoice
        }

        val customerNotFoundException = CustomerNotFoundException(1)

        val paymentProvider = mockk<PaymentProvider>(){
            every { charge(any()) } throws customerNotFoundException
        }

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getPaymentProvider() } returns paymentProvider
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val invokePaymentProviderProcessorStateImpl = InvokePaymentProviderProcessorStateImpl();
        invokePaymentProviderProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(
            BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE,
            billingProcessRequest.state)
    }

    @Test
    fun ` when PaymentProvider throws Exception expect billingProcessRequest to store exception`() {
        val list = ArrayList<InvoiceProcessorAdapter>()

        val invoice = createInvoice(InvoiceStatus.PENDING)

        val invoiceProcessorAdapter = mockk<InvoiceProcessorAdapter>(){
            every { getInvoice() } returns invoice
        }

        val customerNotFoundException = CustomerNotFoundException(1)

        val paymentProvider = mockk<PaymentProvider>(){
            every { charge(any()) } throws customerNotFoundException
        }

        val requestAdapter = mockk<RequestAdapter>(){
            every { getInvoicesProcessorAdapters() } returns list
            every { getPaymentProvider() } returns paymentProvider
        }

        val billingProcessRequest = BillingProcessRequest(requestAdapter, BillProcessorFlowState.START_STATE)
        billingProcessRequest.currentInvoiceProcess=invoiceProcessorAdapter
        val invokePaymentProviderProcessorStateImpl = InvokePaymentProviderProcessorStateImpl();
        invokePaymentProviderProcessorStateImpl.handleRequest(billingProcessRequest)

        assertEquals(
            customerNotFoundException,
            billingProcessRequest.exception)
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