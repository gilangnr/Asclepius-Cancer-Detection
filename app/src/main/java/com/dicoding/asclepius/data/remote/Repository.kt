package com.dicoding.asclepius.data.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData

class Repository private constructor(
    private val apiService: ApiService
){
    fun getNews(): LiveData<Result<List<ArticlesItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews(
                query = "cancer",
                category = "health",
                language = "en",
                apiKey = "63709063e62649f2ba6ad32e1794a0de"
            )
           emit(Result.Success(response.articles))
        } catch (e : Exception) {
            Log.d(TAG, "Repository error : ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    companion object {
        const val TAG = "Repository"

        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,

            ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}