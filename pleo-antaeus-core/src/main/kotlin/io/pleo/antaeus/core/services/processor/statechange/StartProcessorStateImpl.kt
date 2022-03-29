package io.pleo.antaeus.core.services.processor.statechange

import io.pleo.antaeus.models.BillProcessorFlowState

class StartProcessorStateImpl : ProcessorState {
    override fun getStateType(): String {
        return BillProcessorFlowState.START_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        val invoiceProcessorAdapter = request.queue.remove()
        request.currentInvoiceProcess=invoiceProcessorAdapter
        request.state=BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE
    }
}