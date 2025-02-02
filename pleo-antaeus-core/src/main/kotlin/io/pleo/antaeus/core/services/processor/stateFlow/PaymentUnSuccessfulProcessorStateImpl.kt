package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.models.BillProcessorFlowState
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class PaymentUnSuccessfulProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.PAYMENT_UNSUCCESSFUL_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin PaymentUnSuccessfulProcessorStateImpl<<" }

        val invoice = request.currentInvoiceProcess.getInvoice()
        val invoiceService = request.billingRequestAdapterImpl.getInvoiceService()
        logger.info { "invoice for (${invoice.id}) payment Unsuccessful." }
        invoiceService.updateInvoiceStatus(invoice.id, InvoiceStatus.FAILED)
        request.state = BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE

        logger.info { ">>End PaymentUnSuccessfulProcessorStateImpl<<" }
    }
}