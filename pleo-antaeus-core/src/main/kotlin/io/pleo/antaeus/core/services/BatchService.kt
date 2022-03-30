package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice

interface BatchService {
    fun getNextBatch(): List<Invoice>
    fun nextBatchExist(): Boolean
    fun setTotalNumberOfRecords(totalNumberOfRecords: Int)
}