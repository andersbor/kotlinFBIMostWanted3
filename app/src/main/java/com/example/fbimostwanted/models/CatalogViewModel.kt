package com.example.fbimostwanted.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fbimostwanted.repository.FBIrepository

class CatalogViewModel : ViewModel() {
    private val repository = FBIrepository()
    var currentPage = 1
    val itemsLiveData: LiveData<Catalog> = repository.catalogLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getCatalog(currentPage)
    }

    operator fun get(index: Int): Item? {
        return itemsLiveData.value?.items?.get(index)
    }
}