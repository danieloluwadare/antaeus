package io.pleo.antaeus.core.afterStateChangeAction

import io.pleo.antaeus.core.cor.ExceptionHandler
import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest
import io.pleo.antaeus.models.BillProcessorFlowState

class ExceptionEncounteredAfterStateChangeServiceImpl(private val exceptionHandler: ExceptionHandler) :
    AfterStateChangeService {
    override fun getStateType(): String {
        return BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name
    }

    override fun initiate(request: BillingProcessRequest) {
        exceptionHandler.handleException(request)
    }
}