package io.pleo.antaeus.models

enum class BillProcessorFlowState {
    START_STATE,
    VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE,
    MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE,
    MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE,
    STOP_STATE
}
