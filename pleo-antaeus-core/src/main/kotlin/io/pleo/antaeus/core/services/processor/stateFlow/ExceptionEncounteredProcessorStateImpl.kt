package io.pleo.antaeus.core.services.processor.stateFlow

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.models.BillProcessorFlowState
import io.pleo.antaeus.models.ExceptionType
import mu.KotlinLogging

class ExceptionEncounteredProcessorStateImpl : ProcessorState {
    private val logger = KotlinLogging.logger { }

    override fun getStateType(): String {
        return BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name
    }

    override fun handleRequest(request: BillingProcessRequest) {
        logger.info { ">>Begin ExceptionEncounteredProcessorStateImpl<<" }
        val exceptionType: ExceptionType = when (request.exception) {
            is NetworkException -> ExceptionType.NETWORK
            is CustomerNotFoundException -> ExceptionType.CUSTOMER_NOT_FOUND
            is CurrencyMismatchException -> ExceptionType.CURRENCY_MISMATCH
            else -> ExceptionType.UNKNOWN
        }
        request.mapOfExceptionHandler?.get(exceptionType.name)?.handleException(request)
        request.state = BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE
        logger.info { ">>End ExceptionEncounteredProcessorStateImpl<<" }
    }
}