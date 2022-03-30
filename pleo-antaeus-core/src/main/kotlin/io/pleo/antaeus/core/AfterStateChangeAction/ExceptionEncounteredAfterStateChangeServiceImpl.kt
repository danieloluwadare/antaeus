package io.pleo.antaeus.core.AfterStateChangeAction

import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest
import io.pleo.antaeus.models.BillProcessorFlowState

class ExceptionEncounteredAfterStateChangeServiceImpl : AfterStateChangeService {
    override fun getStateType(): String {
        return BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name
    }

    override fun initiate(request: BillingProcessRequest) {

    }
}