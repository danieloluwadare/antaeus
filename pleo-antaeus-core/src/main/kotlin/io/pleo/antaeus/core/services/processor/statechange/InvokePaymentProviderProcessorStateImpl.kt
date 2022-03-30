package io.pleo.antaeus.core.services.processor.statechange

import io.pleo.antaeus.models.BillProcessorFlowState
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging

class InvokePaymentProviderProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.INVOKE_PAYMENT_PROVIDER_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin InvokePaymentProviderProcessorStateImpl<<" }
        try {
            logger.info { "begin payment charge" }

            val invoice = request.currentInvoiceProcess.getInvoice()
            val paymentProvider = request.billingRequestAdapterImpl.getPaymentProvider()
            val paymentProviderResponse = paymentProvider.charge(invoice)
            if(request.currentInvoiceProcess.delayNetworkCall())
                Thread.sleep(10)
            request.paymentProviderResponse=paymentProviderResponse
//            afterBillingChargeService.process(invoice, paymentProviderResponse)
            if(paymentProviderResponse)
                request.state=BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE
            else
                request.state=BillProcessorFlowState.PAYMENT_UNSUCCESSFUL_STATE

            logger.info { "end payment charge" }

        } catch (ex: Exception) {
            logger.info { "exception occurred **(${ex.message})**" }
            request.exception=ex
            request.state=BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE
//            exceptionHandler.handleException(ex, invoice, numberOfRetries, this)
        }
        logger.info { ">>End InvokePaymentProviderProcessorStateImpl<<" }
    }
}