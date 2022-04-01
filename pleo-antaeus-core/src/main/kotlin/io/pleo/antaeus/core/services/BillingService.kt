package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.processor.BillingProcessor
import io.pleo.antaeus.core.services.processor.requestadapter.BillingRequestAdapterImpl
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService,
    private val billingProcessor: BillingProcessor,
    private val batchService: BatchService
) {
    private val logger = KotlinLogging.logger { }


    fun charge() {
        var batchCount = 1
        while (batchService.nextBatchExist()) {
            val invoicesList: List<Invoice> = batchService.getNextBatch()
            logger.info { ">> invoice batch size ==> (${invoicesList.size}) .<<" }
            logger.info { ">> Begin Execution of BATCH NUMBER $batchCount <<" }
            val requestAdapter = BillingRequestAdapterImpl(
                invoicesList = invoicesList,
                paymentProvider = paymentProvider,
                invoiceService = invoiceService,
                maximumRetryCount = 1
            )
            billingProcessor.process(requestAdapter)
            batchCount++
        }
    }

    fun charge(invoice: Invoice) {
        if (invoice.status == InvoiceStatus.PAID) {
            logger.info { ">> invoice of id ==> ${invoice.id} . has already been paid<<" }
            return
        }

        val listOfInvoice = ArrayList<Invoice>()
        listOfInvoice.add(invoice)
        val requestAdapter = BillingRequestAdapterImpl(
            invoicesList = listOfInvoice,
            paymentProvider = paymentProvider,
            invoiceService = invoiceService,
            maximumRetryCount = 1
        )
        billingProcessor.process(requestAdapter)
    }
}
