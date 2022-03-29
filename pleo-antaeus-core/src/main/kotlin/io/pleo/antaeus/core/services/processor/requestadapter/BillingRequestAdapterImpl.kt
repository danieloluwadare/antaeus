package io.pleo.antaeus.core.services.processor.requestadapter

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.AfterStateChangeService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.processor.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.InvoiceProcessorAdapterImpl
import io.pleo.antaeus.models.Invoice

class BillingRequestAdapterImpl(private val invoicesList:List<Invoice>,
                                private val paymentProvider: PaymentProvider,
                                private val invoiceService: InvoiceService,
                                private val afterStateChangeService: AfterStateChangeService
                                ) : RequestAdapter {
    private val invoiceProcessorList = ArrayList<InvoiceProcessorAdapter>()

    init {
        invoicesList
            .forEach { invoice -> invoiceProcessorList.add(InvoiceProcessorAdapterImpl(invoice)) }
    }
    override fun getInvoicesProcessorAdapters(): List<InvoiceProcessorAdapter> {
        return invoiceProcessorList
    }

    override fun getPaymentProvider(): PaymentProvider {
        return paymentProvider
    }

    override fun getInvoiceService(): InvoiceService {
        return invoiceService
    }

    override fun getAfterStateChangeService(): AfterStateChangeService {
        return afterStateChangeService;
    }
}