package io.pleo.antaeus.core.services.processor.statechange

import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class MaximumNumberRetriesNotExceededProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin MaximumNumberRetriesNotExceededProcessorStateImpl<<" }
        request.state = BillProcessorFlowState.INVOKE_PAYMENT_PROVIDER_STATE
        logger.info { ">>End MaximumNumberRetriesNotExceededProcessorStateImpl<<" }
    }
}