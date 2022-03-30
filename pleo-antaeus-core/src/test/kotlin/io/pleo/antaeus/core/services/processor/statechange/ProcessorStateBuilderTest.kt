package io.pleo.antaeus.core.services.processor.statechange

import io.pleo.antaeus.models.BillProcessorFlowState
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ProcessorStateBuilderTest{

    val mapOfProcessorState : Map<String, ProcessorState> = ProcessorStateBuilder.buildMap()


    @Test
    fun `processor map must contain START_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.START_STATE.name))
    }

    @Test
    fun `processor map must contain VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name))
    }

    @Test
    fun `processor map must contain QUERY_QUEUE_STATUS_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.QUERY_QUEUE_STATUS_STATE.name))
    }

    @Test
    fun `processor map must contain PAYMENT_SUCCESSFUL_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE.name))
    }

    @Test
    fun `processor map must contain PAYMENT_UNSUCCESSFUL_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.PAYMENT_UNSUCCESSFUL_STATE.name))
    }

    @Test
    fun `processor map must contain MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE.name))
    }

    @Test
    fun `processor map must contain MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE.name))
    }

    @Test
    fun `processor map must contain INVOKE_PAYMENT_PROVIDER_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.INVOKE_PAYMENT_PROVIDER_STATE.name))
    }

    @Test
    fun `processor map must contain EXCEPTION_ENCOUNTERED_STATE`() {
        assertTrue(mapOfProcessorState.containsKey(BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name))
    }
}