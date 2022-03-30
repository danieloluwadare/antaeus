package io.pleo.antaeus.core.services.processor.requestadapter

import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class InvoiceProcessorAdapterImplTest {

    @Test
    fun `when Invoice is passed into the constructor getInvoice must equal invoice`() {
        val invoice = createInvoice(InvoiceStatus.PENDING)
        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        assertEquals(invoice, invoiceProcessorAdapter.getInvoice())
    }

    @Test
    fun `when Invoice is passed into the constructor getCounter must equal 0`() {
        val invoice = createInvoice(InvoiceStatus.PENDING)
        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        assertEquals(0, invoiceProcessorAdapter.getCounter())
    }

    @Test
    fun `when invoiceProcessorAdapter is instantiated then increment must increase counter by 1`() {
        val invoice = createInvoice(InvoiceStatus.PENDING)
        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        invoiceProcessorAdapter.incrementCounter()
        assertEquals(1, invoiceProcessorAdapter.getCounter())
    }

    @Test
    fun `when Invoice is passed into the constructor isComplete must equal false`() {
        val invoice = createInvoice(InvoiceStatus.PENDING)
        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        assertEquals(false, invoiceProcessorAdapter.isComplete())
    }

    @Test
    fun `when invoiceProcessorAdapter is instantiated then complete must change completeStatus to true`() {
        val invoice = createInvoice(InvoiceStatus.PENDING)
        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        invoiceProcessorAdapter.setComplete()
        assertEquals(true, invoiceProcessorAdapter.isComplete())
    }

    @Test
    fun `when invoiceProcessorAdapter is instantiated then delayNetworkCall must be false`() {
        val invoice = createInvoice(InvoiceStatus.PENDING)
        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        assertEquals(false, invoiceProcessorAdapter.delayNetworkCall())
    }

    @Test
    fun `when invoiceProcessorAdapter is instantiated then activateDelayNetworkCall must set delayNetworkCall to be false`() {
        val invoice = createInvoice(InvoiceStatus.PENDING)
        val invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        invoiceProcessorAdapter.activateDelayNetworkCall()
        assertEquals(true, invoiceProcessorAdapter.delayNetworkCall())
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