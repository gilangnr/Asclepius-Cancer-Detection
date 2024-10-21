package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.remote.ApiConfig
import com.dicoding.asclepius.data.remote.Repository

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}