package io.pleo.antaeus.core.services.processor.statechange

interface ProcessorState {
    fun getStateType():String
    fun handleRequest(request : BillingProcessRequest)
}