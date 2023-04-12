package com.example.cs4530lifestyleapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val detailsData: MutableLiveData<DetailsData>
    private val repository: Repository

    fun setDetailsData(data: Array<String?>) {
        repository.setDetailsData(data)
    }

    val data: LiveData<DetailsData>
        get() = detailsData

    init {
        repository = Repository.getInstance(application)
        detailsData = repository.detailsData
    }
}