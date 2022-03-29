package io.pleo.antaeus.core.services.processor

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.models.Invoice

interface RequestAdapter {
    fun getInvoicesProcessorAdapters(): List<InvoiceProcessorAdapter>
    fun getPaymentProvider(): PaymentProvider
    fun getInvoiceService() : InvoiceService
}