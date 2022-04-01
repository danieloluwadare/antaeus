package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.afterStateChangeAction.AfterStateChangeService

class ExceptionHandlerBuilder {

    companion object {
        fun buildChain(): Map<Exception, ExceptionHandler> {
            val list = ArrayList<ExceptionHandler>()
            list.add(CustomerNotFoundExceptionHandler())
//            list.add(CurrencyMismatchExceptionHandler())
//            list.add(NetworkExceptionHandler())
//            list.add(UnKnownErrorExceptionHandler())

            val mapOfExceptionHandler = HashMap<Exception, ExceptionHandler>()
            for (exceptionHandler in list) {
                mapOfExceptionHandler[exceptionHandler.getExceptionType()] = exceptionHandler
            }
            return mapOfExceptionHandler
        }
    }

}