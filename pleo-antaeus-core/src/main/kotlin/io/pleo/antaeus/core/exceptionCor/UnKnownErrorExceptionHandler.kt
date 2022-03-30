package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import mu.KotlinLogging

/**
 * this is fatal, an error we don't understand...proper logging and alerting will work here
 */
class UnKnownErrorExceptionHandler : ExceptionHandler() {
    private val logger = KotlinLogging.logger { }

    override fun handleException(
        request: BillingProcessRequest

    ) {

        logger.error { "unknown error occurred charging invoice(${request.currentInvoiceProcess.getInvoice().id})" }
    }

    override fun getOrder(): Int {
        return 4;
    }
}