package io.pleo.antaeus.core.services.processor

import io.pleo.antaeus.core.services.processor.requestadapter.RequestAdapter

interface BillingProcessor {
    fun process(requestAdapter: RequestAdapter)
}