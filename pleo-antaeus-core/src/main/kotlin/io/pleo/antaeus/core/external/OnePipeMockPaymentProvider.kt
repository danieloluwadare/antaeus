package io.pleo.antaeus.core.external

import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.models.Invoice

class OnePipeMockPaymentProvider : PaymentProvider {

    override fun charge(invoice: Invoice): Boolean {
        val randomNumber = (0..100).shuffled().last()
        return when (randomNumber % 4) {
            0 -> false
            1 -> true
            2 -> throw CustomerNotFoundException(invoice.customerId)
            else -> throw NetworkException()
        }
    }
}