package io.pleo.antaeus.core.services.processor.requestadapter

import io.mockk.mockk
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BillingRequestAdapterImplTest {

    @Test
    fun `when a list of invoices is passed into the constructor getInvoicesProcessorAdapters size must equal invoice size`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()

        val requestAdapterImpl = BillingRequestAdapterImpl(
            invoicesList = invoiceList,
            paymentProvider = paymentProvider,
            invoiceService = invoiceService,
            maximumRetryCount = 10
        )

        assertEquals(invoiceList.size, requestAdapterImpl.getInvoicesProcessorAdapters().size)
    }

    @Test
    fun `when invoiceService is passed into the constructor getInvoiceService must equal invoiceService`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()

        val requestAdapterImpl = BillingRequestAdapterImpl(
            invoicesList = invoiceList,
            paymentProvider = paymentProvider,
            invoiceService = invoiceService,
            maximumRetryCount = 10
        )

        assertEquals(invoiceService, requestAdapterImpl.getInvoiceService())
    }

    @Test
    fun `when paymentProvider is passed into the constructor getPaymentProvider must equal paymentProvider`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()

        val requestAdapterImpl = BillingRequestAdapterImpl(
            invoicesList = invoiceList,
            paymentProvider = paymentProvider,
            invoiceService = invoiceService,
            maximumRetryCount = 10
        )

        assertEquals(paymentProvider, requestAdapterImpl.getPaymentProvider())
    }

    @Test
    fun `when maximumRetryCount is passed into the constructor getMaximumRetryCount must equal maximumRetryCount`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()

        val requestAdapterImpl = BillingRequestAdapterImpl(
            invoicesList = invoiceList,
            paymentProvider = paymentProvider,
            invoiceService = invoiceService,
            maximumRetryCount = 10
        )

        assertEquals(10, requestAdapterImpl.getMaximumRetryCount())
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