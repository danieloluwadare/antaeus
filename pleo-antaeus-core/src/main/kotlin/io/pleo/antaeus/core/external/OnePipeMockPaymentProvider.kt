package io.pleo.antaeus.core.external

import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.exceptions.NetworkException
import io.pleo.antaeus.models.Invoice

class OnePipeMockPaymentProvider : PaymentProvider {

    override fun charge(invoice: Invoice): Boolean {
        val randomNumber = (0..100).shuffled().last()
        val remainder = randomNumber % 4
        return if(remainder==0)
            false
        else if(remainder==1)
            true
        else if(remainder==2)
            throw CustomerNotFoundException(invoice.customerId)
        else
            throw NetworkException()
    }
}