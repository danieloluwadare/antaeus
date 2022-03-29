package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice

interface AfterStateChangeService {
    fun process()
}