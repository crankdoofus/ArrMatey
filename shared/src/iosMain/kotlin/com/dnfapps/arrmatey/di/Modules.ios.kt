package com.dnfapps.arrmatey.di

import androidx.room.RoomDatabase
import com.dnfapps.arrmatey.database.ArrMateyDatabase
import com.dnfapps.arrmatey.database.getDatabaseBuilder
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<RoomDatabase.Builder<ArrMateyDatabase>> {
        getDatabaseBuilder()
    }
}