package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class StartProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.START_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin StartProcessorStateImpl<<" }

        val invoiceProcessorAdapter = request.queue.remove()
        request.currentInvoiceProcess = invoiceProcessorAdapter
        request.state = BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE

        logger.info { ">>end StartProcessorStateImpl<<" }

    }
}