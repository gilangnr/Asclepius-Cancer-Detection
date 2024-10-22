package com.dicoding.asclepius.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CancerDao {
    @Query("SELECT * FROM cancers_history")
    fun getCancers(): LiveData<List<CancerEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCancer(cancer: List<CancerEntity>)
}