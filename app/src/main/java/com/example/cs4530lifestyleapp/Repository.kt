package com.example.cs4530lifestyleapp

import androidx.lifecycle.MutableLiveData
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.jvm.Synchronized
import kotlin.math.roundToInt

class Repository private constructor(dao: DetailsDao) {
    val detailsData = MutableLiveData<DetailsData>()

    private var details: DetailsData? = null
    private var mDao: DetailsDao = dao

    fun setDetailsData(data: DetailsData) {
        mScope.launch(Dispatchers.IO){
            parseDetails(data)
            detailsData.postValue(details!!);

            insert()
        }
    }

    fun setCurrPage(data: String?) {
        if (details != null) {
            details!!.currPage = data
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

        //calculate BMR and caloric Intake
        var mHeight = ((d.heightFeet!! * 12) + d.heightInches!!).toString()
        if (d.sex=="Female"){
            var BMR = 447.593 + (9.247*(d.weight?.toInt()!!)*0.453592) + (3.098*(mHeight?.toInt()!!)*2.54) + (-4.330*(d.age?.toInt()!!))
            //Log.d("BMR",BMR.toString())
            d.bmr= BMR.roundToInt().toString()
        }
        else if (d.sex=="Male"){
            var BMR = 88.362 + (13.397*(d.weight?.toInt()!!)*0.453592) + (4.799*(mHeight?.toInt()!!)*2.54) + (-5.677*(d.age?.toInt()!!))
            //Log.d("BMR",BMR.toString())
            d.bmr= BMR.roundToInt().toString()
        }
        if (d.activityLevel=="Sedentary: little or no exercise"){
            d.caloricIntake=(d.bmr!!.toInt()*1.2).roundToInt().toString()
        }
        else if (d.activityLevel=="Exercise 1-3 times/week"){
            d.caloricIntake=(d.bmr!!.toInt()*1.375).roundToInt().toString()
        }
        else if (d.activityLevel=="Moderate Exercise 3-5 times/week"){
            d.caloricIntake=(d.bmr!!.toInt()*1.55).roundToInt().toString()
        }
        else if (d.activityLevel=="Very Active 6-7 days/wk"){
            d.caloricIntake=(d.bmr!!.toInt()*1.725).roundToInt().toString()
        }
        else if (d.activityLevel=="Extremely active (intense exercise/physical job)"){
            d.caloricIntake=(d.bmr!!.toInt()*1.9).roundToInt().toString()
        }
        details = d
    }

    @WorkerThread
    suspend fun insert() {
        if (details != null) {
            mDao.insert(
                DetailsTable(
                    details!!.firstName!!, details!!.lastName!!, details!!.heightInches!!, details!!.heightFeet!!,
            details!!.sex!!, details!!.age!!, details!!.weight!!, details!!.location!!, details!!.activityLevel!!,
                details!!.bmr!!, details!!.caloricIntake!!)
            )
        }
    }

    companion object {
        private var mInstance: Repository? = null
        private lateinit var mScope: CoroutineScope
        @Synchronized
        fun getInstance(dao: DetailsDao, scope: CoroutineScope): Repository {
            mScope = scope
            return mInstance?: synchronized(this){
                val instance = Repository(dao)
                mInstance = instance
                instance
            }
        }
    }
}