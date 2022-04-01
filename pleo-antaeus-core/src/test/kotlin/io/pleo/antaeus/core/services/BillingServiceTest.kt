package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.afterStateChangeAction.AfterStateChangeService
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.processor.BillingProcessor
import io.pleo.antaeus.models.Invoice
import org.junit.jupiter.api.Test

class BillingServiceTest {

    @Test
    fun `billingProcessor is invoked when getBatch return true`() {


        val list = ArrayList<Invoice>()
        val invoiceProcessorAdapter = mockk<Invoice>()
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)

        val paymentProvider = mockk<PaymentProvider>()
        val billingProcessor = mockk<BillingProcessor> {
            every { process(any()) } returns Unit

        }
        val invoiceService = mockk<InvoiceService> {
            every { countInvoiceByStatus(any()) } returns 10
        }

        val batchService = mockk<BatchService> {
            every { nextBatchExist() } returns true andThen false
            every { getNextBatch() } returns list
//            every { setTotalNumberOfRecords(any()) } returns Unit
        }

        val billingService = BillingService(
            paymentProvider = paymentProvider, invoiceService = invoiceService,
            billingProcessor = billingProcessor,  batchService = batchService
        )

        billingService.initiate()

        verify(exactly = 1) {
            billingProcessor.process(any())
        }
    }

    @Test
    fun `billingProcessor is not invoked when getBatch return false`() {


        val list = ArrayList<Invoice>()
        val invoiceProcessorAdapter = mockk<Invoice>()
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)
        list.add(invoiceProcessorAdapter)

        val paymentProvider = mockk<PaymentProvider>()
        val billingProcessor = mockk<BillingProcessor> {
            every { process(any()) } returns Unit

        }
        val invoiceService = mockk<InvoiceService> {
            every { countInvoiceByStatus(any()) } returns 10
        }

        val batchService = mockk<BatchService> {
            every { nextBatchExist() } returns false
            every { getNextBatch() } returns list
//            every { setTotalNumberOfRecords(any()) } returns Unit
        }

        val billingService = BillingService(
            paymentProvider = paymentProvider, invoiceService = invoiceService,
            billingProcessor = billingProcessor,  batchService = batchService
        )

        billingService.initiate()

        verify(exactly = 0) {
            billingProcessor.process(any())
        }
    }
}