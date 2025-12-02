package com.dnfapps.arrmatey.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dnfapps.arrmatey.api.arr.model.ArrMovie

@Dao
interface MovieDao: BaseArrDao<ArrMovie> {

    @Insert()
    override suspend fun insertAll(items: List<ArrMovie>)

    @Query("DELETE FROM arr_movies")
    override suspend fun clearAll()

    @Query("SELECT * FROM arr_movies WHERE instanceId = :instanceId")
    override suspend fun getAll(instanceId: Long): List<ArrMovie>

}