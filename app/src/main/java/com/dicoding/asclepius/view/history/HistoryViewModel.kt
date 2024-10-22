package com.dicoding.asclepius.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.local.CancerEntity
import com.dicoding.asclepius.data.local.CancerRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: CancerRepository): ViewModel() {
    val cancers: LiveData<List<CancerEntity>> = repository.getCancers()

    fun insertCancers(cancers: List<CancerEntity>) = viewModelScope.launch {
        repository.insertCancers(cancers)
    }
}