package io.pleo.antaeus.core.services.processor

import io.pleo.antaeus.models.Invoice

interface InvoiceProcessorAdapter {
    fun getInvoice(): Invoice
    fun getCounter(): Int
    fun incrementCounter()
    fun isComplete(): Boolean
    fun setComplete()
}