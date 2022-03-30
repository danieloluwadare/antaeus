package io.pleo.antaeus.core.services.processor.statechange

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
        logger.info { "invoice for (${invoice.id}) payment successful." }
        invoiceService.updateInvoiceStatus(invoice.id, InvoiceStatus.FAILED)

        val mapOfAfterStateChangeService = request.billingRequestAdapterImpl.getAfterStateChangeService()
        val afterStateChangeService = mapOfAfterStateChangeService[BillProcessorFlowState.PAYMENT_UNSUCCESSFUL_STATE.name]

        logger.info { "About Invoking afterStateChangeService" }
        afterStateChangeService?.initiate(request);
        logger.info { "End Invoking afterStateChangeService" }

        request.state=BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE
        logger.info { ">>End PaymentUnSuccessfulProcessorStateImpl<<" }
    }
}