package io.pleo.antaeus.core.services.processor

import io.pleo.antaeus.core.exceptionCor.ExceptionHandler
import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.core.services.processor.stateFlow.BillingProcessRequest
import io.pleo.antaeus.core.services.processor.stateFlow.ProcessorState
import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class BillingProcessorImpl(
    private val mapOfProcessorState: Map<String, ProcessorState>,
    private val mapOfExceptionHandler: Map<String, ExceptionHandler>
) : BillingProcessor {
    private val logger = KotlinLogging.logger { }

    override fun process(requestAdapter: RequestAdapter) {
        val request = BillingProcessRequest(
            billingRequestAdapterImpl = requestAdapter,
            state = BillProcessorFlowState.START_STATE
        )
        request.mapOfExceptionHandler = mapOfExceptionHandler
        while (request.state != BillProcessorFlowState.STOP_STATE) {
            logger.info { ">> CURRENT STATE ==>==> (${request.state}).<<" }
            val state = mapOfProcessorState[request.state.name]
            state?.handleRequest(request)
        }
        logger.info { "Done with Billing Processor" }
    }

}