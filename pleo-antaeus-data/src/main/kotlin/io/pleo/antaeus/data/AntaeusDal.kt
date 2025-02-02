/*
    Implements the data access layer (DAL).
    The data access layer generates and executes requests to the database.

    See the `mappings` module for the conversions between database rows and Kotlin objects.
 */

package io.pleo.antaeus.data

import io.pleo.antaeus.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class AntaeusDal(private val db: Database) {
    fun fetchInvoice(id: Int): Invoice? {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            // Returns the first invoice with matching id.
            InvoiceTable
                .select { InvoiceTable.id.eq(id) }
                .firstOrNull()
                ?.toInvoice()
        }
    }

    fun fetchInvoices(): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .selectAll()
                .map { it.toInvoice() }
        }
    }

    fun createInvoice(amount: Money, customer: Customer, status: InvoiceStatus = InvoiceStatus.PENDING): Invoice? {
        val id = transaction(db) {
            // Insert the invoice and returns its new id.
            InvoiceTable
                .insert {
                    it[this.value] = amount.value
                    it[this.currency] = amount.currency.toString()
                    it[this.status] = status.toString()
                    it[this.customerId] = customer.id
                } get InvoiceTable.id
        }

        return fetchInvoice(id)
    }

    fun fetchCustomer(id: Int): Customer? {
        return transaction(db) {
            CustomerTable
                .select { CustomerTable.id.eq(id) }
                .firstOrNull()
                ?.toCustomer()
        }
    }

    fun fetchCustomers(): List<Customer> {
        return transaction(db) {
            CustomerTable
                .selectAll()
                .map { it.toCustomer() }
        }
    }

    fun createCustomer(currency: Currency): Customer? {
        val id = transaction(db) {
            // Insert the customer and return its new id.
            CustomerTable.insert {
                it[this.currency] = currency.toString()
            } get CustomerTable.id
        }

        return fetchCustomer(id)
    }

    fun fetchInvoicesByStatus(status: InvoiceStatus = InvoiceStatus.PENDING): List<Invoice> {
        return transaction(db) {
            InvoiceTable
                .select { InvoiceTable.status.eq(status.toString()) }
                .map { it.toInvoice() }
        }
    }

    fun countInvoiceByStatus(status: InvoiceStatus = InvoiceStatus.PENDING): Int {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            //count unpaid invoices
            InvoiceTable
                .select { InvoiceTable.status.eq(status.toString()) }
                .count()
        }
    }


    fun fetchInvoiceInBatchesByStatus(
        lastInvoiceId: Int,
        limit: Int = 1,
        status: InvoiceStatus = InvoiceStatus.PENDING
    ): List<Invoice> {
        // transaction(db) runs the internal query as a new database transaction.
        return transaction(db) {
            //fetch unpaid invoices
            var query = InvoiceTable
                .select { InvoiceTable.status.eq(status.name) }

            //if after invoice id is returned, select results after invoice id
            if (lastInvoiceId > 0) {
                query.andWhere { InvoiceTable.id.greater(lastInvoiceId) }
            }

            query.limit(limit)
                .map { it.toInvoice() }
        }
    }

    fun updateInvoiceStatus(invoiceId: Int, status: InvoiceStatus) {
        transaction(db) {
            InvoiceTable.update({ InvoiceTable.id eq invoiceId }) {
                it[InvoiceTable.status] = status.toString()
            }
        }
    }
}
