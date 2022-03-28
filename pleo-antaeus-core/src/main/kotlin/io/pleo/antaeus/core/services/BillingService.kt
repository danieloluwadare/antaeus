package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class BillingService(
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService
) {
    private val logger = KotlinLogging.logger { }


    fun initiate() {
        val total : Int = invoiceService.countInvoiceByStatus(InvoiceStatus.PENDING)
        logger.info { ">> invoices total size ==> (${total}) .<<" }
        val batchService : BatchService = InvoiceBatchServiceImpl(invoiceService =invoiceService, total = total, limit = 20)
        while (batchService.nextBatchExist()){
            val invoicesList:List<Invoice> = batchService.getNextBatch()
            logger.info { ">> invoice batch size ==> ${invoicesList.size}) .<<" }
        }
    }
// TODO - Add code e.g. here
}
