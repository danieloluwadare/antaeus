//package io.pleo.antaeus.core.afterStateChangeAction
//
//import io.pleo.antaeus.core.cor.AfterStateChangeHandlerFactory
//import io.pleo.antaeus.core.exceptionCor.NetworkExceptionHandler
//import io.pleo.antaeus.models.BillProcessorFlowState
//import org.junit.jupiter.api.Assertions.assertTrue
//import org.junit.jupiter.api.Test
//
//class AfterStateChangeHandlerFactoryTest {
////    private val networkExceptionHandler = NetworkExceptionHandler()
//    private val mapOfAfterStateChangeHandler: Map<String, AfterStateChangeService> =
////        AfterStateChangeHandlerFactory.build(networkExceptionHandler)
//
//
//    @Test
//    fun `processor map must contain EXCEPTION_ENCOUNTERED_STATE`() {
//        assertTrue(mapOfAfterStateChangeHandler.containsKey(BillProcessorFlowState.EXCEPTION_ENCOUNTERED_STATE.name))
//    }
//
//    @Test
//    fun `processor map must contain PAYMENT_SUCCESSFUL_STATE`() {
//        assertTrue(mapOfAfterStateChangeHandler.containsKey(BillProcessorFlowState.PAYMENT_SUCCESSFUL_STATE.name))
//    }
//
//    @Test
//    fun `processor map must contain PAYMENT_UNSUCCESSFUL_STATE`() {
//        assertTrue(mapOfAfterStateChangeHandler.containsKey(BillProcessorFlowState.PAYMENT_UNSUCCESSFUL_STATE.name))
//    }
//
//}