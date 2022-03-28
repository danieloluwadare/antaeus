package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice

class InvoiceRepositoryBatchProcessorImpl(private val invoiceService: InvoiceService, private var limit: Boolean = true) : BatchProcessor {
    private var offset: Int=0

    override fun getNextBatch(): List<Invoice> {
        TODO("Not yet implemented")
    }

    override fun nextBatchExist(): Boolean {
        TODO("Not yet implemented")
    }
}