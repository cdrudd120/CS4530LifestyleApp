package com.example.cs4530lifestyleapp

import android.app.Application
import android.support.v4.os.IResultReceiver._Parcel
import android.telecom.Call.Details
import androidx.lifecycle.MutableLiveData

class Repository private constructor(application: Application) {
    var detailsData = MutableLiveData<DetailsData>()

    companion object {
        @Volatile
        private var instance: Repository? = null
        @Synchronized
        fun getInstance(application: Application): Repository {
            if (instance == null) {
                instance = Repository(application)
            }
            return instance as Repository
        }
    }

    fun setDetailsData(data: Array<String?>) {
        var d = DetailsData()
        d.firstName = data[0]
        d.lastName = data[1]
        d.location = data[2]
        d.activityLevel = data[3]
        d.imageFilepath = data[4]
        d.sex = data[5]
        if (data[6] != null) {
            d.age = data[6]!!.toInt()
        }
        if (data[7] != null) {
            d.weight = data[7]!!.toInt()
        }
        if (data[8] != null) {
            d.heightFeet = data[8]!!.toInt()
        }
        if (data[9] != null) {
            d.heightInches = data[9]!!.toInt()
        }

        detailsData.setValue(d)
    }
}