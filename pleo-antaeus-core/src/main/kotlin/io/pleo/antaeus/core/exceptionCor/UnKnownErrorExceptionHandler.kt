package io.pleo.antaeus.core.exceptionCor

import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import io.pleo.antaeus.models.ExceptionType
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

/**
 * this is fatal, an error we don't understand...proper logging and alerting will work here
 */
class UnKnownErrorExceptionHandler : ExceptionHandler() {
    private val logger = KotlinLogging.logger { }

    override fun handleException(
        request: BillingProcessRequest

    ) {
        logger.error { "Log unknown error occurred charging invoice(${request.currentInvoiceProcess.getInvoice().id})" }
        val invoiceService = request.billingRequestAdapterImpl.getInvoiceService()
        logger.info { "About Update invoice status(${request.currentInvoiceProcess.getInvoice().id}) to Failed." }
        invoiceService.updateInvoiceStatus(request.currentInvoiceProcess.getInvoice().id, InvoiceStatus.FAILED)
        logger.info { "Done Updating invoice status(${request.currentInvoiceProcess.getInvoice().id}) to Failed." }
    }

    override fun getExceptionType(): String {
        return ExceptionType.UNKNOWN.name
    }
}