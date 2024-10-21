package com.dicoding.asclepius.view.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.remote.ArticlesItem
import com.dicoding.asclepius.data.remote.Repository
import com.dicoding.asclepius.data.remote.Result

class NewsViewModel(private val repository: Repository): ViewModel(){
    private val _listNews = MutableLiveData<List<ArticlesItem>>()
    val listNews: MutableLiveData<List<ArticlesItem>> = _listNews

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getNews()
    }

    private fun getNews() {
        _isLoading.value = true
        repository.getNews().observeForever() { result ->
            when(result) {
                is Result.Loading -> _isLoading.value = true
                is Result.Success -> {
                    _isLoading.value = false
                    _listNews.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    Log.e(TAG, "Error: ${result.error}")
                }
            }
        }
    }

    companion object {
        const val TAG = "NewsViewModel"
    }
}