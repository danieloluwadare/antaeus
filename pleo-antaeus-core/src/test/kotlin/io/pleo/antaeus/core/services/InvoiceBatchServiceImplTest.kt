package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class InvoiceBatchServiceImplTest{
//val paymentProvider: PaymentProvider = mockk<PaymentProvider> {
//            every { charge(any()) } returns false
//        }

//    private val dal = mockk<InvoiceService> {
//        every { fetchInvoiceInBatchesByStatus(any(),any(),any()) } returns ArrayList<Invoice>
//    }

//    private val invoiceService = InvoiceService(dal = dal)

    @Test
    fun `getNextBatch returns a list of invoice`() {
//        org.junit.jupiter.api.assertThrows<InvoiceNotFoundException> {
//            invoiceService.fetch(404)
//        }

        val invoiceList = ArrayList<Invoice>()
        print("for (i in 1..5) print(i) = ")
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService> {
        every { fetchInvoiceInBatchesByStatus(any(),any(),any()) } returns invoiceList
        }

        val invoiceBatchServiceImpl = InvoiceBatchServiceImpl(invoiceService,100,50)

        assertEquals(invoiceList.size, invoiceBatchServiceImpl.getNextBatch().size)
    }

    @Test
    fun `when offset is less than total then nextBatchExist must return true`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..5) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService> {
            every { fetchInvoiceInBatchesByStatus(any(),any(),any()) } returns invoiceList
        }

        val invoiceBatchServiceImpl  = InvoiceBatchServiceImpl(invoiceService,100,50)
        assertTrue(invoiceBatchServiceImpl.nextBatchExist())
    }

    @Test
    fun `when offset is equal to total then nextBatchExist must return false`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..10) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService> {
            every { fetchInvoiceInBatchesByStatus(any(),any(),any()) } returns invoiceList
        }

        val invoiceBatchServiceImpl  = InvoiceBatchServiceImpl(invoiceService,10,10)
        invoiceBatchServiceImpl.getNextBatch()
        assertFalse(invoiceBatchServiceImpl.nextBatchExist())
    }

    @Test
    fun `when offset is greater than total then nextBatchExist must return false`() {
        val invoiceList = ArrayList<Invoice>()
        for (i in 1..12) invoiceList.add(createInvoice(InvoiceStatus.PENDING))

        val invoiceService = mockk<InvoiceService> {
            every { fetchInvoiceInBatchesByStatus(any(),any(),any()) } returns invoiceList
        }

        val invoiceBatchServiceImpl  = InvoiceBatchServiceImpl(invoiceService,10,10)
        invoiceBatchServiceImpl.getNextBatch()
        assertFalse(invoiceBatchServiceImpl.nextBatchExist())
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