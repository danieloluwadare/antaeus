package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest

abstract class ExceptionHandler {
    abstract fun handleException(request: BillingProcessRequest)
    abstract fun getExceptionType():String
}