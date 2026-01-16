package com.dnfapps.arrmatey.api.client

sealed interface OperationStatus {
    object Idle: OperationStatus
    object InProgress: OperationStatus
    data class Success(val message: String? = null): OperationStatus
    data class Error(val message: String? = null): OperationStatus
}