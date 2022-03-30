package io.pleo.antaeus.core.services.processor.requestadapter

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.AfterStateChangeAction.AfterStateChangeService
import io.pleo.antaeus.core.services.InvoiceService

interface RequestAdapter {
    fun getInvoicesProcessorAdapters(): List<InvoiceProcessorAdapter>
    fun getPaymentProvider(): PaymentProvider
    fun getInvoiceService() : InvoiceService
    fun getAfterStateChangeService(): Map<String, AfterStateChangeService>
    fun getMaximumRetryCount():Int
}