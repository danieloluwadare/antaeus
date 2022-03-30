package io.pleo.antaeus.core.cor

import java.util.Collections

class ExceptionHandlerBuilder {

    companion object {
        fun buildChain(): ExceptionHandler {
            val list = ArrayList<ExceptionHandler>()
            list.add(CustomerNotFoundExceptionHandler())
            list.add(CurrencyMismatchExceptionHandler())
            list.add(NetworkExceptionHandler())
            list.add(UnKnownErrorExceptionHandler())

            var sortedList = list.sortedWith(compareBy { it.getOrder() })
            for(i in 1 until sortedList.size){
                val prevExceptionHandler = sortedList[i-1]
                val currentExceptionHandler = sortedList[i]
                prevExceptionHandler.successor=currentExceptionHandler
            }

//            val customerNotFoundExceptionHandler = CustomerNotFoundExceptionHandler();
//            val currencyMismatchExceptionHandler = CurrencyMismatchExceptionHandler();
//            val networkExceptionHandler = NetworkExceptionHandler();
//            val unKnownErrorExceptionHandler = UnKnownErrorExceptionHandler();
//
//            customerNotFoundExceptionHandler.successor = currencyMismatchExceptionHandler;
//            currencyMismatchExceptionHandler.successor = networkExceptionHandler;
//            networkExceptionHandler.successor = unKnownErrorExceptionHandler;
//
//            return customerNotFoundExceptionHandler;
                return sortedList[0];

        }
    }

}