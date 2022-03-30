package io.pleo.antaeus.core.exceptionCor

import io.pleo.antaeus.core.cor.ExceptionHandlerBuilder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExceptionHandlerBuilderTest {


    val listOfExceptionHandlers = ExceptionHandlerBuilder.buildChain()


    @Test
    fun `listOfExceptionHandlers must contain ExceptionHandler with order as 1`() {
        assertEquals(1, listOfExceptionHandlers.getOrder())
    }

    @Test
    fun `listOfExceptionHandlers must contain ExceptionHandler with order as 2`() {
        assertEquals(2, listOfExceptionHandlers.successor?.getOrder())
    }

    @Test
    fun `listOfExceptionHandlers must contain ExceptionHandler with order as 3`() {
        assertEquals(3, listOfExceptionHandlers.successor?.successor?.getOrder())
    }

    @Test
    fun `listOfExceptionHandlers must contain ExceptionHandler with order as 4`() {
        assertEquals(3, listOfExceptionHandlers.successor?.successor?.successor?.getOrder())
    }

}