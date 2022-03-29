package io.pleo.antaeus.core.services.processor.statechange

import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.BillProcessorFlowState
import io.pleo.antaeus.models.Invoice
import java.util.LinkedList
import java.util.Queue

data class BillingProcessRequest(val billingRequestAdapterImpl: RequestAdapter, val state: BillProcessorFlowState){
    private val queue: Queue<InvoiceProcessorAdapter> = LinkedList<InvoiceProcessorAdapter>()
    init {
        queue.addAll(billingRequestAdapterImpl.getInvoicesProcessorAdapters())
    }

}
