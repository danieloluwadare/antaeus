package io.pleo.antaeus.core.afterStateChangeAction

import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class PaymentSuccessfulAfterStateChangeServiceImpl : AfterStateChangeService {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE.name
    }

    override fun initiate(request: BillingProcessRequest) {
        logger.info { "Do anything extra outside the bill processor" }
    }
}