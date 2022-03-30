package io.pleo.antaeus.core.services.processor.stateFlow

interface ProcessorState {
    fun getStateType(): String
    fun handleRequest(request: BillingProcessRequest)
}