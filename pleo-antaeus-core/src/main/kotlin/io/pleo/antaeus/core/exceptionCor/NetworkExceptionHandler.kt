package io.pleo.antaeus.core.exceptionCor

import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import io.pleo.antaeus.models.ExceptionType
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
        logger.info { "Adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) back to the queue due to network error." }
        request.currentInvoiceProcess.incrementCounter()
        request.currentInvoiceProcess.activateDelayNetworkCall()
        logger.info { "queue size before adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) ==> ${request.queue.size}." }
        request.queue.add(request.currentInvoiceProcess)
        logger.info { "queue size after adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) ==> ${request.queue.size}." }

    }

    override fun getExceptionType(): String {
        return ExceptionType.NETWORK.name
    }
}