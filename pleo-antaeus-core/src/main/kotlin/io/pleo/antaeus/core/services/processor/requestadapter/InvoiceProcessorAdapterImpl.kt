package io.pleo.antaeus.core.services.processor.requestadapter

import io.pleo.antaeus.core.services.processor.requestadapter.InvoiceProcessorAdapter
import io.pleo.antaeus.models.Invoice

class InvoiceProcessorAdapterImpl(private val invoice: Invoice) : InvoiceProcessorAdapter {
    private var counter : Int = 0
    private var completeStatus : Boolean=false
    private var delayNetworkCall : Boolean = false

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

    override fun delayNetworkCall(): Boolean {
        return delayNetworkCall
    }

    override fun activateDelayNetworkCall() {
        delayNetworkCall=true
    }
}