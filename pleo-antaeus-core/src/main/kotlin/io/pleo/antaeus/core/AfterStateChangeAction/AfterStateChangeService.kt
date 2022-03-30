package io.pleo.antaeus.core.AfterStateChangeAction

import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest
import io.pleo.antaeus.models.Invoice

interface AfterStateChangeService {
    fun initiate(request: BillingProcessRequest)
}