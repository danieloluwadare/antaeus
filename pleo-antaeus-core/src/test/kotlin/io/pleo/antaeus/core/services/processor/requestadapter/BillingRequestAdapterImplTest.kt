package io.pleo.antaeus.core.services.processor.requestadapter

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.AfterStateChangeService
import io.pleo.antaeus.core.services.InvoiceBatchServiceImpl
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class BillingRequestAdapterImplTest{

    @Test
    fun `when a list of invoices is passed into the constructor getInvoicesProcessorAdapters size must equal invoice size`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()
        val afterStateChangeService = mockk<AfterStateChangeService>()
        val map = HashMap<String, AfterStateChangeService>()
        map["test"] = afterStateChangeService;

        val requestAdapterImpl = BillingRequestAdapterImpl(invoiceList,paymentProvider,invoiceService,map)

        assertEquals(invoiceList.size, requestAdapterImpl.getInvoicesProcessorAdapters().size)
    }

    @Test
    fun `when invoiceService is passed into the constructor getInvoiceService must equal invoiceService`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()
        val afterStateChangeService = mockk<AfterStateChangeService>()
        val map = HashMap<String, AfterStateChangeService>()
        map["test"] = afterStateChangeService;

        val requestAdapterImpl = BillingRequestAdapterImpl(invoiceList,paymentProvider,invoiceService,map)

        assertEquals(invoiceService, requestAdapterImpl.getInvoiceService())
    }

    @Test
    fun `when paymentProvider is passed into the constructor getPaymentProvider must equal paymentProvider`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()
        val afterStateChangeService = mockk<AfterStateChangeService>()
        val map = HashMap<String, AfterStateChangeService>()
        map["test"] = afterStateChangeService;

        val requestAdapterImpl = BillingRequestAdapterImpl(invoiceList,paymentProvider,invoiceService,map)

        assertEquals(paymentProvider, requestAdapterImpl.getPaymentProvider())
    }

    @Test
    fun `when afterStateChangeService is passed into the constructor getAfterStateChangeService must equal afterStateChangeService`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService>()
        val paymentProvider = mockk<PaymentProvider>()
        val afterStateChangeService = mockk<AfterStateChangeService>()
        val map = HashMap<String, AfterStateChangeService>()
        map["test"] = afterStateChangeService;

        val requestAdapterImpl = BillingRequestAdapterImpl(invoiceList,paymentProvider,invoiceService,map)

        assertEquals(map, requestAdapterImpl.getAfterStateChangeService())
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