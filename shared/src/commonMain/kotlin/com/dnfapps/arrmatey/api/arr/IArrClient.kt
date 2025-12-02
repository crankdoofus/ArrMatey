package com.dnfapps.arrmatey.api.arr

interface IArrClient<T> {

    suspend fun getLibrary(): List<T>

}