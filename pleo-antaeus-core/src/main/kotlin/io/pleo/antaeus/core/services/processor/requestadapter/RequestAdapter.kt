package io.pleo.antaeus.core.services.processor.requestadapter

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.services.AfterStateChangeService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.processor.statechange.ProcessorState
import io.pleo.antaeus.models.BillProcessorFlowState

interface RequestAdapter {
    fun getInvoicesProcessorAdapters(): List<InvoiceProcessorAdapter>
    fun getPaymentProvider(): PaymentProvider
    fun getInvoiceService() : InvoiceService
    fun getAfterStateChangeService(): Map<String, AfterStateChangeService>
}