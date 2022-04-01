package io.pleo.antaeus.core.exceptionCor

import io.pleo.antaeus.core.external.CurrencyConverter
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapterImpl
import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import io.pleo.antaeus.models.ExceptionType
import io.pleo.antaeus.models.Money
import mu.KotlinLogging

/**
 * we can minimize the rate of this error by confirming that the currency on invoice
 * is same as customers currency, if this still occurs, then a business rule determines
 * what is done next.
 */
class CurrencyMismatchExceptionHandler(private val  currencyConverter: CurrencyConverter, private val customerService: CustomerService) : ExceptionHandler() {
    private val logger = KotlinLogging.logger { }

    override fun handleException(
        request: BillingProcessRequest
    ) {
        logger.error { "customer(${request.currentInvoiceProcess.getInvoice().customerId}) invoice currency does not match on payment provider." }
//        Perform a currency conversion and add it to the queue
        var invoice = request.currentInvoiceProcess.getInvoice()
        val customer = customerService.fetch(invoice.customerId)
        val convertedAmount = currencyConverter.convert(invoice.amount.value, invoice.amount.currency, customer.currency)
        invoice = invoice.copy(amount = Money(convertedAmount, customer.currency))
        val  invoiceProcessorAdapter = InvoiceProcessorAdapterImpl(invoice = invoice)
        request.currentInvoiceProcess = invoiceProcessorAdapter
        logger.info { "queue size before adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) ==> ${request.queue.size}." }
        request.queue.add(request.currentInvoiceProcess)
        logger.info { "queue size after adding invoice of id ==> (${request.currentInvoiceProcess.getInvoice().id}) ==> ${request.queue.size}." }
    }

    override fun getExceptionType(): String {
        return ExceptionType.CURRENCY_MISMATCH.name
    }
}