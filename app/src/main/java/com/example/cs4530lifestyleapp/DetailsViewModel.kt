package com.example.cs4530lifestyleapp

import androidx.lifecycle.*

class DetailsViewModel(repository: Repository) : ViewModel(){
    private val detailsData: LiveData<DetailsData> = repository.detailsData

    private val repository: Repository = repository

    fun setDetailsData(data: DetailsData) {
        repository.setDetailsData(data)
    }

    fun setCurrPage(data: String?) {
        repository.setCurrPage(data)
    }

    val data: LiveData<DetailsData>
        get() = detailsData

}

class DetailsViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}