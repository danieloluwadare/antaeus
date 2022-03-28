/*
    Implements endpoints related to invoices.
 */

package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus

class InvoiceService(private val dal: AntaeusDal) {
    fun fetchAll(): List<Invoice> {
        return dal.fetchInvoices()
    }

    fun fetch(id: Int): Invoice {
        return dal.fetchInvoice(id) ?: throw InvoiceNotFoundException(id)
    }

    fun countInvoiceByStatus(status: InvoiceStatus):Int {
        return dal.countInvoiceByStatus(status)
    }

    fun fetchInvoiceInBatchesByStatus(lastInvoiceId:Int,limit:Int=1,status: InvoiceStatus): List<Invoice>{
        return dal.fetchInvoiceInBatchesByStatus(lastInvoiceId,limit,status)
    }

    fun updateInvoiceStatus(invoiceId:Int, status: InvoiceStatus):Any{
        return dal.updateInvoiceStatus(invoiceId,status)
    }
}
