package io.pleo.antaeus.core.cor

import io.pleo.antaeus.core.afterStateChangeAction.AfterStateChangeService
import io.pleo.antaeus.core.afterStateChangeAction.ExceptionEncounteredAfterStateChangeServiceImpl
import io.pleo.antaeus.core.afterStateChangeAction.PaymentSuccessfulAfterStateChangeServiceImpl
import io.pleo.antaeus.core.afterStateChangeAction.PaymentUnSuccessfulAfterStateChangeServiceImpl
import io.pleo.antaeus.core.exceptionCor.ExceptionHandler

class AfterStateChangeHandlerFactory {

    companion object {
        fun build(exceptionHandler: ExceptionHandler): Map<String, AfterStateChangeService> {
            val list = ArrayList<AfterStateChangeService>()
            list.add(PaymentUnSuccessfulAfterStateChangeServiceImpl())
            list.add(PaymentSuccessfulAfterStateChangeServiceImpl())
            list.add(ExceptionEncounteredAfterStateChangeServiceImpl(exceptionHandler = exceptionHandler))

            val mapOfAfterStateChange = HashMap<String, AfterStateChangeService>()
            for (afterStateChangeService in list) {
                mapOfAfterStateChange[afterStateChangeService.getStateType()] = afterStateChangeService
            }
            return mapOfAfterStateChange

        }
    }

}