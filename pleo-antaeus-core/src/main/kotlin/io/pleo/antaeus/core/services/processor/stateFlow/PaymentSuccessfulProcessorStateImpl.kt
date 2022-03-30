package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.models.BillProcessorFlowState
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class PaymentSuccessfulProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin PaymentSuccessfulProcessorStateImpl<<" }

        val invoice = request.currentInvoiceProcess.getInvoice()
        val invoiceService = request.billingRequestAdapterImpl.getInvoiceService()
        logger.info { "invoice for (${invoice.id}) payment successful." }
        invoiceService.updateInvoiceStatus(invoice.id, InvoiceStatus.PAID)

        val mapOfAfterStateChangeService = request.billingRequestAdapterImpl.getAfterStateChangeService()
        val afterStateChangeService = mapOfAfterStateChangeService[BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE.name]

        logger.info { "About Invoking afterStateChangeService" }
        afterStateChangeService?.initiate(request);
        logger.info { "End Invoking afterStateChangeService" }

        request.currentInvoiceProcess.setComplete()

        request.state = BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE
        logger.info { ">>End PaymentSuccessfulProcessorStateImpl<<" }
    }
}