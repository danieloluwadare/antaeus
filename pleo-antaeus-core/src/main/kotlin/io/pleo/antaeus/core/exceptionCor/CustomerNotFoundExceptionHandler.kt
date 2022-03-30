package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.services.BillingService
import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest
import io.pleo.antaeus.models.Invoice
import mu.KotlinLogging

/**
 * this error should be reported first, an action can be performed based on business logic
 * e.g Notify administrator, create customer, notify customer etc
 */
class CustomerNotFoundExceptionHandler : ExceptionHandler() {
    private val logger = KotlinLogging.logger { }

    override fun handleException(
        request: BillingProcessRequest
    ) {
        if (request.exception is CustomerNotFoundException)
            logger.error { ">>customer(${request.currentInvoiceProcess.getInvoice().customerId}) not found on payment provider.<<" }
        else
            successor?.handleException(request)
    }

    override fun getOrder(): Int {
        return 2;
    }
}