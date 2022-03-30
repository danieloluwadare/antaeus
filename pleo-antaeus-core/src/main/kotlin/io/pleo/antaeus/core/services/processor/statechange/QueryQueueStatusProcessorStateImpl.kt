package io.pleo.antaeus.core.services.processor.statechange

import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class QueryQueueStatusProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin QueryQueueStatusProcessorStateImpl<<" }
        if (request.queue.isEmpty())
            request.state = BillProcessorFlowState.STOP_STATE
        else
            request.state = BillProcessorFlowState.START_STATE

        logger.info { ">>End QueryQueueStatusProcessorStateImpl<<" }
    }
}