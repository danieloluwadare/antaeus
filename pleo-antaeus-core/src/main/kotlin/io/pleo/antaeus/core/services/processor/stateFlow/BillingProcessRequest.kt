package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.core.exceptionCor.ExceptionHandler
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.models.BillProcessorFlowState
import java.util.*

data class BillingProcessRequest(val billingRequestAdapterImpl: RequestAdapter, var state: BillProcessorFlowState) {
    val queue: Queue<InvoiceProcessorAdapter> = LinkedList<InvoiceProcessorAdapter>()
    var mapOfExceptionHandler: Map<String, ExceptionHandler>? = null
    init {
        queue.addAll(billingRequestAdapterImpl.getInvoicesProcessorAdapters())
    }

    //computed values
    lateinit var currentInvoiceProcess: InvoiceProcessorAdapter
    var paymentProviderResponse: Boolean = false
    lateinit var exception: Exception

}
