package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import mu.KotlinLogging

/**
 *we'll retry this right away, on a prod environment however,
 * the mechanism for handling this will be different
 */
class NetworkExceptionHandler : ExceptionHandler() {

    private val logger = KotlinLogging.logger { }

    override fun handleException(
        request: BillingProcessRequest
    ) {
        if (request.exception is NetworkException) {
            logger.info { "Adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) back to the queue due to network error." }
            request.currentInvoiceProcess.incrementCounter()
            request.currentInvoiceProcess.activateDelayNetworkCall()
            logger.info { "queue size before adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) ==> ${request.queue.size}." }
            request.queue.add(request.currentInvoiceProcess)
            logger.info { "queue size after adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) ==> ${request.queue.size}." }

        } else
            successor?.handleException(request)
    }

    override fun getOrder(): Int {
        return 3;
    }
}