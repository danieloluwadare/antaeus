/*
    Defines the main() entry point of the app.
    Configures the database and sets up the REST web service.
 */

@file:JvmName("AntaeusApp")

package io.pleo.antaeus.app

import io.pleo.antaeus.core.cor.AfterStateChangeHandlerFactory
import io.pleo.antaeus.core.cor.ExceptionHandlerBuilder
import io.pleo.antaeus.core.external.OnePipeMockPaymentProvider
import io.pleo.antaeus.core.services.BillingService
import io.pleo.antaeus.core.services.CustomerService
import io.pleo.antaeus.core.services.InvoiceService
import io.pleo.antaeus.core.services.processor.BillingProcessorImpl
import io.pleo.antaeus.core.services.processor.statechange.ProcessorStateBuilder
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

    // Build Chain Of Responsibility of ExceptionsHandler
    val exceptionHandler = ExceptionHandlerBuilder.buildChain()

    // Build a Map of AfterStateChangeService
    val mapOfAfterStateChangeService =
        AfterStateChangeHandlerFactory.build(exceptionHandler = exceptionHandler)

    // Build a Map of ProcessorState
    val mapOfProcessorState = ProcessorStateBuilder.buildMap()

    // Inject Map of ProcessorState into Billing Processor
    val billingProcessor = BillingProcessorImpl(mapOfProcessorState = mapOfProcessorState)

    // This is _your_ billing service to be included where you see fit
    val billingService = BillingService(
        paymentProvider = paymentProvider,
        invoiceService = invoiceService,
        billingProcessor = billingProcessor,
        mapOfAfterStateChangeService = mapOfAfterStateChangeService
    )

    // Create REST web service
    AntaeusRest(
        invoiceService = invoiceService,
        customerService = customerService,
        billingService = billingService
    ).run()
}
