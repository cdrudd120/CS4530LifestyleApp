package com.example.cs4530lifestyleapp

import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized

class Repository private constructor(dao: Dao) {
    val detailsData = MutableLiveData<DetailsData>()

    private var details: DetailsData? = null
    private var mDao: Dao = dao

    fun setDetailsData(data: DetailsData) {
        mScope.launch(Dispatchers.IO){
            parseDetails(data)
            detailsData.postValue(details);
        }
    }
    @WorkerThread
    suspend fun parseDetails(data: DetailsData) {
        var d = DetailsData()
        d.firstName = data.firstName
        d.lastName = data.lastName
        d.location = data.location
        d.activityLevel = data.activityLevel
        d.imageFilepath = data.imageFilepath
        d.sex = data.sex
        if (data.age != null) {
            d.age = data.age!!.toInt()
        }
        if (data.weight != null) {
            d.weight = data.weight!!.toInt()
        }
        if (data.heightFeet != null) {
            d.heightFeet = data.heightFeet!!.toInt()
        }
        if (data.heightInches != null) {
            d.heightInches = data.heightInches!!.toInt()
        }
        details = d
    }

    companion object {
        private var mInstance: Repository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(dao: Dao, scope: CoroutineScope): Repository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = Repository(dao)
                mInstance = instance
                instance
            }
        }
    }
}