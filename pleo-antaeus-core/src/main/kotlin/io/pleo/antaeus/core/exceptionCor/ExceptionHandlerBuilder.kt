package io.pleo.antaeus.core.cor

class ExceptionHandlerBuilder {

    companion object {
        fun buildChain(): ExceptionHandler {
            val list = ArrayList<ExceptionHandler>()
            list.add(CustomerNotFoundExceptionHandler())
            list.add(CurrencyMismatchExceptionHandler())
            list.add(NetworkExceptionHandler())
            list.add(UnKnownErrorExceptionHandler())

            var sortedList = list.sortedWith(compareBy { it.getOrder() })
            for (i in 1 until sortedList.size) {
                val prevExceptionHandler = sortedList[i - 1]
                val currentExceptionHandler = sortedList[i]
                prevExceptionHandler.successor = currentExceptionHandler
            }
            return sortedList[0]

        }
    }

}