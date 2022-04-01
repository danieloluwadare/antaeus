## Antaeus

Antaeus (/ænˈtiːəs/), in Greek mythology, a giant of Libya, the son of the sea god Poseidon and the Earth goddess Gaia.
He compelled all strangers who were passing through the country to wrestle with him. Whenever Antaeus touched the
Earth (his mother), his strength was renewed, so that even if thrown to the ground, he was invincible. Heracles, in
combat with him, discovered the source of his strength and, lifting him up from Earth, crushed him to death.

Welcome to our challenge.

## The challenge

As most "Software as a Service" (SaaS) companies, Pleo needs to charge a subscription fee every month. Our database
contains a few invoices for the different markets in which we operate. Your task is to build the logic that will
schedule payment of those invoices on the first of the month. While this may seem simple, there is space for some
decisions to be taken and you will be expected to justify them.

## Instructions

Fork this repo with your solution. Ideally, we'd like to see your progression through commits, and don't forget to
update the README.md to explain your thought process.

Please let us know how long the challenge takes you. We're not looking for how speedy or lengthy you are. It's just
really to give us a clearer idea of what you've produced in the time you decided to take. Feel free to go as big or as
small as you want.

## Developing

Requirements:

- \>= Java 11 environment

Open the project using your favorite text editor. If you are using IntelliJ, you can open the `build.gradle.kts` file
and it is gonna setup the project in the IDE for you.

### Building

```
./gradlew build
```

### Running

There are 2 options for running Anteus. You either need libsqlite3 or docker. Docker is easier but requires some docker
knowledge. We do recommend docker though.

*Running Natively*

Native java with sqlite (requires libsqlite3):

If you use homebrew on MacOS `brew install sqlite`.

```
./gradlew run
```

*Running through docker*

Install docker for your platform

```
docker build -t antaeus
docker run antaeus
```

### App Structure

The code given is structured as follows. Feel free however to modify the structure to fit your needs.

```
├── buildSrc
|  | gradle build scripts and project wide dependency declarations
|  └ src/main/kotlin/utils.kt 
|      Dependencies
|
├── pleo-antaeus-app
|       main() & initialization
|
├── pleo-antaeus-core
|       This is probably where you will introduce most of your new code.
|       Pay attention to the PaymentProvider and BillingService class.
|
├── pleo-antaeus-data
|       Module interfacing with the database. Contains the database 
|       models, mappings and access layer.
|
├── pleo-antaeus-models
|       Definition of the Internal and API models used throughout the
|       application.
|
└── pleo-antaeus-rest
        Entry point for HTTP REST API. This is where the routes are defined.
```

### Main Libraries and dependencies

* [Exposed](https://github.com/JetBrains/Exposed) - DSL for type-safe SQL
* [Javalin](https://javalin.io/) - Simple web framework (for REST)
* [kotlin-logging](https://github.com/MicroUtils/kotlin-logging) - Simple logging framework for Kotlin
* [JUnit 5](https://junit.org/junit5/) - Testing framework
* [Mockk](https://mockk.io/) - Mocking library
* [Sqlite3](https://sqlite.org/index.html) - Database storage engine

Happy hacking 😁!

# Solution Overview

## My Mantra

"Jeffrey Way always says Get it to green stage then keep refactoring it to make it better and better". This has been one
of my guiding principles for software development, it gives me the opportunity to get a working solution out and then
keep building my optimizations on top of that.

### Implementation Overview

I chose Quartz Scheduler to handle the processing of invoices as it is an open-source job scheduling library that can be
integrated within virtually any Java/Kotlin application.

Quartz can be used to create simple or complex schedules for executing tens, hundreds, or even tens-of-thousands of
jobs. Quartz offers job scheduling through patterns like cronjobs available in Unix systems. This is a great way of
leveraging existing cronjob documentations as a quick guide.

## Design Pattern Implemented

- Factory
- State Design pattern
- Adapter

# Algorithm For Processing Invoice

### InvoiceProcessorAdapter Interface

- This interface defines extra methods for one to add more properties to the invoice without directly altering the
  invoice model.These methods are used by some classes implementing the ProcessorState (
  ValidateMaximumNumberRetriesExceededProcessorStateImpl, InvokePaymentProviderProcessorStateImpl etc)
- class implementing this interface: InvoiceProcessorAdapterImpl

### RequestAdapter Interface

- This interface defines extra methods needed by the class that implements the BillingProcessor interface
- class implementing this interface: BillingRequestAdapterImpl

### BatchService Interface

- This interface defines the method used to help to fetch invoices in batches.
    - InvoiceBatchServiceImpl

    - nextBatchExist() methods checks if the there is any available batch of invoice to be fetched.
    - getNextBatch() This fetches the invoices with the limit passed into its constructor

### Billing Service

* charge()
    * This method fetches the invoice in batches and invokes the BillingProcessorImpl to process each Batch
* charge(invoice)
    * This method charges a single invoice

### BillingProcessor Interface

- This interface helps in processing the invoices
    - BillingProcessorImpl
        - This Service Initializes the state machine
        - It populates the queue with the InvoiceProcessorAdapter
        - Keeps processing the invoice adapter until stop state is reached, then it terminates.

## ProcessorState Interface

- states flow process


- StartProcessorStateImpl
    - This state pull an InvoiceProcessorAdapter from the queue
    - set the invoice adapter as the current Invoice Process being processed
    - set the next state to VALIDATE_MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE


- ValidateMaximumNumberRetriesExceededProcessorStateImpl
    - This state check if the current invoice has not exceeded the Maximum Retry Count
    - set the next state to either MAXIMUM_NUMBER_RETRIES_EXCEEDED_STATE or MAXIMUM_NUMBER_RETRIES_NOT_EXCEEDED_STATE


- MaximumNumberRetriesExceededProcessorStateImpl
    - Updates the status of the current invoice to failed
    - set the next state QUERY_QUEUE_STATUS_STATE


- MaximumNumberRetriesNotExceededProcessorStateImpl
    - set the next state INVOKE_PAYMENT_PROVIDER_STATE


- InvokePaymentProviderProcessorStateImpl
    - Delay Invoking Payment provider call if Invoice has been processed before
    - Invoke Payment provider and save the response in the request
    - set the next state to either PAYMENT_SUCCESSFUL_STATE or PAYMENT_UNSUCCESSFUL_STATE or EXCEPTION_ENCOUNTERED_STATE


- PaymentSuccessfulProcessorStateImpl
    - update status of the current invoice to paid
    - set the next state QUERY_QUEUE_STATUS_STATE


- PaymentUnSuccessfulProcessorStateImpl
    - update status of the current invoice to failed
    - set the next state QUERY_QUEUE_STATUS_STATE


- ExceptionEncounteredProcessorStateImpl
    - get the right exception type and invoke the ExceptionHandler class that handles such exception
    - set the next state QUERY_QUEUE_STATUS_STATE


- QueryQueueStatusProcessorStateImpl
    - check if the queue is empty
    - set the next state to either START_STATE or STOP_STATE

### ExceptionHandler class

*This abstract class defines the abstract method handleException() which major exception handler class implements such
as

- CustomerNotFoundException:
    - update the invoice to failed
    - logic should report such issues to external services like sentry, so that investigation can be carried out on such
      invoice
- UnKnownErrorExceptionHandler
    - update the invoice to failed
    - logic should report such issues to external services like sentry
- CurrencyMismatchException
    - Invokes the CurrencyConverter interface to convert the currency;
    - Adds the process back to the queue to be re-processed
    - logic should report such issues to external services like sentry
- NetworkException
    - This increment the invoice counter
    - activates the delay network call
    - add the invoice back on the queue

`Improvements: for better insight the invoice model can have an extra field, that allows one to update the kind of exception or failure that occured when the exception handler is invoked, before the invoice is persisted.`

### BillingScheduler Cron

- The BillingJob is the entry point of the cron
- The BillingScheduler houses the cron configuration (including the config to run the cron on the first of every month).
  The config can be made more generic such that we can easily add other crons which might be added to the service)

### Assumptions

- Only one instance of the app will be used in billing the invoices, hence concurrency and related problems were not put
  into consideration.

### Improvements

- I probably didn't write idiomatic kotlin code, this is something I'm getting better in as I do more Kotlin projects.
- Retrying is instant, this will not play out in a prod environment, a cool off period to allow systems to recover will
  be ideal.
- In the real world, more than two instances of this service would exist for high availability sake , with this design,
  an external cron service would be making a request to the endpoint that is exposed
  on  [Initiate billing invoice endpoint](http://localhost:7000/rest/v1/invoices/charge/all)
- Added a [charge a single invoice endpoint](http://localhost:7000/rest/v1/invoices/{1}/charge) to charge a single
  invoice,
- Added a [update invoice status endpoint](http://localhost:7000/rest/v1/invoices/{1}/{paid}/update) to update a single
  invoice, this occurs if the customer performs a manual payment and tender the receipt of payment, the invoice can be
  updated with endpoint
- Added a [fetch an invoice by status endpoint](http://localhost:7000/rest/v1/invoices/{pending}) this helps to fetch an
  invoice by it status

### Time Duration of Project
About 8hrs spread over 4 days due to availability.