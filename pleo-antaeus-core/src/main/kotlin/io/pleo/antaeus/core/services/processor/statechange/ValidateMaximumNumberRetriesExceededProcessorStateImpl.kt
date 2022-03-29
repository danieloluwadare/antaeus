package io.pleo.antaeus.core.services.processor.statechange

import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.models.BillProcessorFlowState

class ValidateMaximumNumberRetriesExceededProcessorStateImpl : ProcessorState {
    override fun getStateType(): String {
        return BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        var invoiceProcessorAdapter = request.queue.remove()
        request.currentInvoiceProcess=invoiceProcessorAdapter
        request.state=BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE
    }
}