package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice

interface BatchProcessor {
    fun getNextBatch(): List<Invoice>
    fun nextBatchExist(): Boolean
}