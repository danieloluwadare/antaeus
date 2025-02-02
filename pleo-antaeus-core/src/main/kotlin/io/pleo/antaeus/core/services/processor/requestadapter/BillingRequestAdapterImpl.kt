package io.pleo.antaeus.core.services.processor.requestadapter

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.models.Invoice

class BillingRequestAdapterImpl(
    invoicesList: List<Invoice>,
    private val paymentProvider: PaymentProvider,
    private val invoiceService: InvoiceService,
    private val maximumRetryCount: Int
) : RequestAdapter {
    private val invoiceProcessorList = invoicesList.map { invoice -> InvoiceProcessorAdapterImpl(invoice = invoice) }


    override fun getInvoicesProcessorAdapters(): List<InvoiceProcessorAdapter> {
        return invoiceProcessorList
    }

    override fun getPaymentProvider(): PaymentProvider {
        return paymentProvider
    }

    override fun getInvoiceService(): InvoiceService {
        return invoiceService
    }

    override fun getMaximumRetryCount(): Int {
        return maximumRetryCount
    }
}