package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import io.pleo.antaeus.models.ExceptionType
import io.pleo.antaeus.models.InvoiceStatus
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
            //Make it Fail
        logger.error { ">>log customer(${request.currentInvoiceProcess.getInvoice().customerId}) not found on payment provider.<<" }
        val invoiceService = request.billingRequestAdapterImpl.getInvoiceService()
        logger.info { "About Update invoice status(${request.currentInvoiceProcess.getInvoice().id}) to Failed." }
        invoiceService.updateInvoiceStatus(request.currentInvoiceProcess.getInvoice().id, InvoiceStatus.FAILED)
        logger.info { "Done Updating invoice status(${request.currentInvoiceProcess.getInvoice().id}) to Failed." }
    }

    override fun getExceptionType(): String {
        return ExceptionType.CUSTOMER_NOT_FOUND.name
    }
}