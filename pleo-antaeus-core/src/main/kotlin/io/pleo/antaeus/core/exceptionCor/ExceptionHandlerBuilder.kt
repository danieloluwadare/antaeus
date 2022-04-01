package io.pleo.antaeus.core.exceptionCor

import io.pleo.antaeus.core.external.CurrencyConverter
import io.pleo.antaeus.core.services.CustomerService

class ExceptionHandlerBuilder {

    companion object {
        fun build(
            currencyConverter: CurrencyConverter,
            customerService: CustomerService
        ): Map<String, ExceptionHandler> {
            val list = ArrayList<ExceptionHandler>()
            list.add(CustomerNotFoundExceptionHandler())
            list.add(
                CurrencyMismatchExceptionHandler(
                    currencyConverter = currencyConverter,
                    customerService = customerService
                )
            )
            list.add(NetworkExceptionHandler())
            list.add(UnKnownErrorExceptionHandler())

            val mapOfExceptionHandler = HashMap<String, ExceptionHandler>()
            for (exceptionHandler in list) {
                mapOfExceptionHandler[exceptionHandler.getExceptionType()] = exceptionHandler
            }
            return mapOfExceptionHandler
        }
    }

}