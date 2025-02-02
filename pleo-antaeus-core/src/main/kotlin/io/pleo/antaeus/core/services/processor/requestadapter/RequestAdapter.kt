package io.pleo.antaeus.core.services.processor.requestadapter

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.InvoiceService

interface RequestAdapter {
    fun getInvoicesProcessorAdapters(): List<InvoiceProcessorAdapter>
    fun getPaymentProvider(): PaymentProvider
    fun getInvoiceService(): InvoiceService
    fun getMaximumRetryCount(): Int
}