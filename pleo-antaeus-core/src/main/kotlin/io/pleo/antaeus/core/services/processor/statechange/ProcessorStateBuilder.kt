package io.pleo.antaeus.core.services.processor.statechange

class ProcessorStateBuilder {

    companion object {
        fun buildMap(): Map<String, ProcessorState> {

            val list = ArrayList<ProcessorState>();
            list.add(StartProcessorStateImpl())
            list.add(ValidateMaximumNumberRetriesExceededProcessorStateImpl())
            list.add(QueryQueueStatusProcessorStateImpl())
            list.add(PaymentUnSuccessfulProcessorStateImpl())
            list.add(PaymentSuccessfulProcessorStateImpl())
            list.add(MaximumNumberRetriesNotExceededProcessorStateImpl())
            list.add(MaximumNumberRetriesExceededProcessorStateImpl())
            list.add(InvokePaymentProviderProcessorStateImpl())
            list.add(ExceptionEncounteredProcessorStateImpl())

            val mapOfProcessorState = HashMap<String,ProcessorState>();
            for (processorState in list){
                mapOfProcessorState[processorState.getStateType()]=processorState
            }

            return mapOfProcessorState
        }
    }

}