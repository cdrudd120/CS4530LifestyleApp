package com.example.cs4530lifestyleapp

import androidx.annotation.NonNull
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

class DetailsData() {
    var firstName: String? = null
    var lastName: String? = null
    var heightInches: Int? = null
    var heightFeet: Int? = null
    var sex: String? = null
    var age: Int? = null
    var weight: Int? = null
    var location: String? = null
    var imageFilepath: String? = null
    var activityLevel: String? = null
    var bmr: String?=null
    var caloricIntake: String?=null
    var currPage: String?=null
}