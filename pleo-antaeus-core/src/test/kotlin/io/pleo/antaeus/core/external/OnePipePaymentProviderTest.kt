package io.pleo.antaeus.core.external

import org.junit.jupiter.api.Test

class OnePipePaymentProviderTest {

    @Test
    fun `test randomNumber`() {
        for (i in 1..10) {
            val randomNumber = (0..100).shuffled().last()
            println(randomNumber)
        }

    }
}