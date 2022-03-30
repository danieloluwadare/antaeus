package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class ValidateMaximumNumberRetriesExceededProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin ValidateMaximumNumberRetriesExceededProcessorStateImpl<<" }
        if (request.currentInvoiceProcess.getCounter() > request.billingRequestAdapterImpl.getMaximumRetryCount())
            request.state = BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE
        else
            request.state = BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE
        logger.info { ">>End ValidateMaximumNumberRetriesExceededProcessorStateImpl<<" }
    }
}