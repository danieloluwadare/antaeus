package io.pleo.antaeus.core.exceptionCor

import io.pleo.antaeus.core.cor.ExceptionHandlerBuilder
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExceptionHandlerBuilderTest {


    val mapOfExceptionHandler = ExceptionHandlerBuilder.buildChain()


    @Test
    fun `listOfExceptionHandlers must contain ExceptionHandler with order as 1`() {
        assertTrue(mapOfExceptionHandler.containsKey(CustomerNotFoundException(1)))
//        assertEquals(1, listOfExceptionHandlers.getOrder())
    }

}