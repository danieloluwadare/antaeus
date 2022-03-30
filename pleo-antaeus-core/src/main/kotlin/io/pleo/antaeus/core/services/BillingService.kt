package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.afterStateChangeAction.AfterStateChangeService
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
    private val mapOfAfterStateChangeService: Map<String, AfterStateChangeService>
) {
    private val logger = KotlinLogging.logger { }


    fun initiate() {
        val total: Int = invoiceService.countInvoiceByStatus(InvoiceStatus.PENDING)
        logger.info { ">> invoices total size ==> (${total}) .<<" }
        val batchService =
            InvoiceBatchServiceImpl(invoiceService = invoiceService, total = total, limit = 20)
        var batchCount = 1
        while (batchService.nextBatchExist()) {
            val invoicesList: List<Invoice> = batchService.getNextBatch()
            logger.info { ">> invoice batch size ==> (${invoicesList.size}) .<<" }
            logger.info { ">> Begin Execution of BATCH NUMBER $batchCount <<" }
            val requestAdapter = BillingRequestAdapterImpl(
                invoicesList = invoicesList,
                paymentProvider = paymentProvider,
                invoiceService = invoiceService,
                mapOfAfterStateChangeService = mapOfAfterStateChangeService,
                maximumRetryCount = 1
            );
            billingProcessor.process(requestAdapter);
            batchCount++
        }
    }
}
