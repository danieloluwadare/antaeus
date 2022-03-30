package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class ExceptionEncounteredProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin PaymentUnSuccessfulProcessorStateImpl<<" }
        val mapOfAfterStateChangeService = request.billingRequestAdapterImpl.getAfterStateChangeService()
        val afterStateChangeService =
            mapOfAfterStateChangeService[BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name]
        logger.info { "About Invoking afterStateChangeService for EXCEPTION_ENCOUNTERED_STATE" }
        afterStateChangeService?.initiate(request)
        logger.info { "End Invoking afterStateChangeService" }
        request.state = BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE
        logger.info { ">>End PaymentUnSuccessfulProcessorStateImpl<<" }
    }
}