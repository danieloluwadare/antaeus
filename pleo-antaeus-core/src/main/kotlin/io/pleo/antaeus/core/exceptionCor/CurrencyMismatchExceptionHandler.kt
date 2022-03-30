package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import mu.KotlinLogging

/**
 * we can minimize the rate of this error by confirming that the currency on invoice
 * is same as customers currency, if this still occurs, then a business rule determines
 * what is done next.
 */
class CurrencyMismatchExceptionHandler : ExceptionHandler() {
    private val logger = KotlinLogging.logger { }

    override fun handleException(
        request: BillingProcessRequest
    ) {
        if (request.exception is CurrencyMismatchException)
            logger.error { "customer(${request.currentInvoiceProcess.getInvoice().customerId}) invoice currency does not match on payment provider." }
        else
            successor?.handleException(request)
    }

    override fun getOrder(): Int {
        return 1;
    }
}