package io.pleo.antaeus.core.afterStateChangeAction

import io.pleo.antaeus.core.exceptionCor.ExceptionHandler
import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
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