package io.pleo.antaeus.core.external

import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class OnePipePaymentProviderTest{

    @Test
    fun `test randomNumber`() {
        for(i in 1..10){
            val randomNumber = (0..100).shuffled().last()
            println(randomNumber)
        }

    }
}