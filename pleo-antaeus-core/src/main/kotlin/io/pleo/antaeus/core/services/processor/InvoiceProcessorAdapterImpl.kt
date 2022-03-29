package io.pleo.antaeus.core.services.processor

import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.models.Invoice

class InvoiceProcessorAdapterImpl(private val invoice: Invoice) : InvoiceProcessorAdapter {
    private var counter : Int = 0
    private var completeStatus : Boolean=false

    override fun getInvoice(): Invoice {
        return invoice
    }

    override fun getCounter(): Int {
        return counter
    }

    override fun incrementCounter() {
        counter++
    }

    override fun isComplete(): Boolean {
        return completeStatus
    }

    override fun setComplete() {
        completeStatus=true;
    }
}