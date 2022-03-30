package io.pleo.antaeus.core.afterStateChangeAction

import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest

interface AfterStateChangeService {
    fun getStateType():String
    fun initiate(request: BillingProcessRequest)
}