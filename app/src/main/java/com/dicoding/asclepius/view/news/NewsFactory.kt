package com.dicoding.asclepius.view.news

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.remote.Repository
import com.dicoding.asclepius.di.Injection

class NewsFactory private constructor(private val repository: Repository): ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsViewModel::class.java)){
            return NewsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: NewsFactory? = null
        fun getInstance(context: Context): NewsFactory =
            instance ?: synchronized(this) {
                instance ?: NewsFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}