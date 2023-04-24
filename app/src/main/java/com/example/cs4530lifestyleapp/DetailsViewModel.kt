package com.example.cs4530lifestyleapp

import androidx.lifecycle.*

class DetailsViewModel(repository: Repository) : ViewModel(){
    private val detailsData: LiveData<DetailsData> = repository.detailsData
    private val weatherData: LiveData<WeatherData> = repository.weatherData

    private val repository: Repository = repository

    fun setDetailsData(data: DetailsData) {
        repository.setDetailsData(data)
    }


    fun setWeatherLocation(location: String) {
        repository.setWeatherLocation(location)
    }

    val data: LiveData<DetailsData>
        get() = detailsData

    val wData: LiveData<WeatherData>
        get() = weatherData

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