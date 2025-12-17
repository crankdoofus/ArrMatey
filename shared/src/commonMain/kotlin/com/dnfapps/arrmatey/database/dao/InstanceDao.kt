package com.dnfapps.arrmatey.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.dnfapps.arrmatey.model.Instance
import com.dnfapps.arrmatey.model.InstanceType
import kotlinx.coroutines.flow.Flow

@Dao
interface InstanceDao {

    @Insert
    suspend fun insert(instance: Instance)

    @Delete
    suspend fun delete(instance: Instance)

    @Update
    suspend fun update(instance: Instance)

    @Query("SELECT * FROM instances")
    suspend fun getAllInstances(): List<Instance>

    @Query("SELECT * FROM instances")
    fun observeAllInstances(): Flow<List<Instance>>

    @Query("""
        UPDATE instances 
        SET selected = CASE 
            WHEN id = :id THEN true
            ELSE false
        END
        WHERE type = :type
    """)
    suspend fun setInstanceAsSelected(id: Long, type: InstanceType)
}
