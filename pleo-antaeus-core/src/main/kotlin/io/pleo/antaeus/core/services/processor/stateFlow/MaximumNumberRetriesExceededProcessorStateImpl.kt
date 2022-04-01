package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.models.BillProcessorFlowState
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class MaximumNumberRetriesExceededProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin MaximumNumberRetriesExceededProcessorStateImpl<<" }
        val invoiceService = request.billingRequestAdapterImpl.getInvoiceService()
        logger.info { "invoice(${request.currentInvoiceProcess.getInvoice().id}) exceeded Maximum Retries with retry count of ==> ${request.currentInvoiceProcess.getCounter()}." }
        logger.info { "About Update invoice status(${request.currentInvoiceProcess.getInvoice().id}) to Failed." }
        invoiceService.updateInvoiceStatus(request.currentInvoiceProcess.getInvoice().id, InvoiceStatus.FAILED)
        logger.info { "Done Updating invoice status(${request.currentInvoiceProcess.getInvoice().id}) to Failed." }
        request.state = BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE
        logger.info { ">>End MaximumNumberRetriesExceededProcessorStateImpl<<" }
    }
}