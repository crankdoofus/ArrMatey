package com.dnfapps.arrmatey.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.dnfapps.arrmatey.api.arr.model.ArrMovie
import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.database.dao.InstanceDao
import com.dnfapps.arrmatey.database.dao.MovieDao
import com.dnfapps.arrmatey.database.dao.SeriesDao
import com.dnfapps.arrmatey.model.Instance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Database(
    entities = [Instance::class, ArrSeries::class, ArrMovie::class],
    version = 1
)
@TypeConverters(Converters::class)
@ConstructedBy(ArrMateyDatabaseConstructor::class)
abstract class ArrMateyDatabase : RoomDatabase() {
    abstract fun getInstanceDao(): InstanceDao
    abstract fun getSeriesDao(): SeriesDao
    abstract fun getMoviesDao(): MovieDao
}

@Suppress("KotlinNoActualForExpect")
expect object ArrMateyDatabaseConstructor : RoomDatabaseConstructor<ArrMateyDatabase> {
    override fun initialize(): ArrMateyDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<ArrMateyDatabase>
): ArrMateyDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}