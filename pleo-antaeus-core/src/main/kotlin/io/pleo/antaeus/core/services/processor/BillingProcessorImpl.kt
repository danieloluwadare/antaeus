package io.pleo.antaeus.core.services.processor

import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter
import io.pleo.antaeus.core.services.processor.statechange.BillingProcessRequest
import io.pleo.antaeus.core.services.processor.statechange.ProcessorState
import io.pleo.antaeus.models.BillProcessorFlowState
import mu.KotlinLogging

class BillingProcessorImpl(private val map: Map<BillProcessorFlowState,ProcessorState>) : BillingProcessor {
    private val logger = KotlinLogging.logger { }

    override fun process(requestAdapter: RequestAdapter) {
        val request = BillingProcessRequest(requestAdapter,BillProcessorFlowState.START_STATE)
        while (request.state != BillProcessorFlowState.STOP_STATE){
            logger.info { ">> CURRENT STATE ==>==> (${request.state}).<<" }
            val state = map[request.state]
            state?.handleRequest(request);
        }
    }

}