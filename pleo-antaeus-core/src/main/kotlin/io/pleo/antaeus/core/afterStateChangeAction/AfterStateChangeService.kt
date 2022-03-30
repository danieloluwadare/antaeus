package io.pleo.antaeus.core.afterStateChangeAction

import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest

interface AfterStateChangeService {
    fun getStateType(): String
    fun initiate(request: BillingProcessRequest)
}