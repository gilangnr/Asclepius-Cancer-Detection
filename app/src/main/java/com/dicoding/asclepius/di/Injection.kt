package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.local.CancerDatabase
import com.dicoding.asclepius.data.local.CancerRepository
import com.dicoding.asclepius.data.remote.ApiConfig
import com.dicoding.asclepius.data.remote.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }

    fun provideCancerRepository(context: Context): CancerRepository {
        val database = CancerDatabase.getDatabase(context)
        val dao = database.cancerDao()

        return CancerRepository.getInstance(dao)
    }
}