package com.dicoding.asclepius.data.local

import androidx.lifecycle.LiveData

class CancerRepository private constructor(
    private val cancerDao: CancerDao
){

    fun getCancers(): LiveData<List<CancerEntity>> = cancerDao.getCancers()

    suspend fun insertCancers(cancers: List<CancerEntity>) {
        cancerDao.insertCancer(cancers)
    }

    companion object {
        @Volatile
        private var instance: CancerRepository? = null
        fun getInstance(
            cancerDao: CancerDao
        ): CancerRepository =
            instance ?: synchronized(this) {
                instance ?: CancerRepository(cancerDao)
            }.also { instance = it }
    }
}