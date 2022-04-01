package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus

class InvoiceBatchServiceImpl(
    private val invoiceService: InvoiceService,
    private var limit: Int = 50
) : BatchService {
    private var total: Int = 0
    private var offset: Int = 0
    private var lastInvoiceId: Int = 0

    init {
        total = invoiceService.countInvoiceByStatus(InvoiceStatus.PENDING)
    }


    override fun getNextBatch(): List<Invoice> {
        //retrieve invoices in batches
        val invoicesList: List<Invoice> =
            invoiceService.fetchInvoiceInBatchesByStatus(lastInvoiceId, limit, InvoiceStatus.PENDING)
        //update last invoiceId
        //it will be used for next query
        if (invoicesList.isNotEmpty()) {
            lastInvoiceId = invoicesList.last().id
        }
        offset += limit
        return invoicesList
    }

    override fun nextBatchExist(): Boolean {
        return offset < total
    }


}