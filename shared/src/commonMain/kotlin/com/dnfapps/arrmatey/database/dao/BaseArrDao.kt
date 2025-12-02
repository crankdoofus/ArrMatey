package com.dnfapps.arrmatey.database.dao

interface BaseArrDao<T> {

    suspend fun insertAll(items: List<T>)

    suspend fun clearAll()

    suspend fun getAll(instanceId: Long): List<T>

}