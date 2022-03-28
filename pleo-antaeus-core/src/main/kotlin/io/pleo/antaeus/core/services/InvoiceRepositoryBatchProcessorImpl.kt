package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus

class InvoiceRepositoryBatchProcessorImpl(
    private val invoiceService: InvoiceService,
    private val total: Int,
    private var limit: Int = 50
) : BatchProcessor {
    private var offset: Int = 0
    var lastInvoiceId: Int = 0

    // Count Unpaid Invoices
//    val unpaidInvoiceCount = invoiceService.countUnpaid()

    override fun getNextBatch(): List<Invoice> {
        //retrieve invoices in batches
        var invoicesList:List<Invoice> = invoiceService.fetchInvoiceInBatchesByStatus(lastInvoiceId,limit,InvoiceStatus.PENDING)

        //update last invoiceId
        //it will be used for next query
        if(invoicesList.isNotEmpty()){
            lastInvoiceId = invoicesList.last().id
        }

        offset += limit

        return invoicesList
    }

    override fun nextBatchExist(): Boolean {
        return offset < limit
    }
}