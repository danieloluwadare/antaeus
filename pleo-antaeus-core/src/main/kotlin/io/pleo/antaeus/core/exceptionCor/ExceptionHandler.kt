package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.services.BillingService
import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest
import io.pleo.antaeus.models.Invoice

abstract class ExceptionHandler {
    var successor: ExceptionHandler? = null

    abstract fun handleException(request: BillingProcessRequest)
    abstract fun getOrder():Int

}