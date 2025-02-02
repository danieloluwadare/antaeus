/*
    Defines the main() entry point of the app.
    Configures the database and sets up the REST web service.
 */

@file:JvmName("AntaeusApp")

package io.pleo.antaeus.app

import io.pleo.antaeus.core.cron.BillingScheduler
import io.pleo.antaeus.core.exceptionCor.ExceptionHandlerBuilder
import io.pleo.antaeus.core.external.CurrencyConverterImpl
import io.pleo.antaeus.core.external.OnePipeMockPaymentProvider
import io.pleo.antaeus.core.services.BillingService
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.InvoiceBatchServiceImpl
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.processor.BillingProcessorImpl
import io.pleo.antaeus.core.services.processor.stateFlow.ProcessorStateBuilder
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.data.CustomerTable
import io.pleo.antaeus.data.InvoiceTable
import io.pleo.antaeus.rest.AntaeusRest
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import setupInitialData
import java.io.File
import java.sql.Connection

fun main() {
    // The tables to create in the database.
    val tables = arrayOf(InvoiceTable, CustomerTable)

    val dbFile: File = File.createTempFile("antaeus-db", ".sqlite")
    // Connect to the database and create the needed tables. Drop any existing data.
    val db = Database
        .connect(
            url = "jdbc:sqlite:${dbFile.absolutePath}",
            driver = "org.sqlite.JDBC",
            user = "root",
            password = ""
        )
        .also {
            TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
            transaction(it) {
                addLogger(StdOutSqlLogger)
                // Drop all existing tables to ensure a clean slate on each run
                SchemaUtils.drop(*tables)
                // Create all tables
                SchemaUtils.create(*tables)
            }
        }

    // Set up data access layer.
    val dal = AntaeusDal(db = db)

    // Insert example data in the database.
    setupInitialData(dal = dal)

    // Get third parties
    val paymentProvider = OnePipeMockPaymentProvider()


    // Create core services
    val invoiceService = InvoiceService(dal = dal)
    val customerService = CustomerService(dal = dal)
    val currencyConverter = CurrencyConverterImpl()

    // This Service fetch  invoice in batches according to the limit set
    val batchService = InvoiceBatchServiceImpl(invoiceService = invoiceService, limit = 20)

    // Build  ExceptionsHandler
    val mapOfExceptionHandler =
        ExceptionHandlerBuilder.build(currencyConverter = currencyConverter, customerService = customerService)

    // Build a Map of BillingProcessorState
    val mapOfProcessorState = ProcessorStateBuilder.buildMap()

    // Inject Map of ProcessorState, into Billing Processor
    val billingProcessor =
        BillingProcessorImpl(mapOfProcessorState = mapOfProcessorState, mapOfExceptionHandler = mapOfExceptionHandler)

    // This is _your_ billing service to be included where you see fit
    val billingService = BillingService(
        paymentProvider = paymentProvider,
        invoiceService = invoiceService,
        billingProcessor = billingProcessor,
        batchService = batchService
    )
    val billingScheduler = BillingScheduler(billingService)
    billingScheduler.schedule()

    // Create REST web service
    AntaeusRest(
        invoiceService = invoiceService,
        customerService = customerService,
        billingService = billingService
    ).run()
}
