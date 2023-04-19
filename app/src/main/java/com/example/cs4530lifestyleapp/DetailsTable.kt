package com.example.cs4530lifestyleapp

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "details_table")
data class DetailsTable(
    @field:ColumnInfo(name = "first_name")
    @field:PrimaryKey
    var firstName: String,
    @field:ColumnInfo(name = "last_name")
    var lastName: String,
    @field:ColumnInfo(name = "height_inches")
    var heightInches: Int,
    @field:ColumnInfo(name = "height_feet")
    var heightFeet:Int,
    @field:ColumnInfo(name = "sex")
    var sex: String,
    @field:ColumnInfo(name = "age")
    var age: Int,
    @field:ColumnInfo(name = "weight")
    var weight: Int,
    @field:ColumnInfo(name = "location")
    var location: String,
//    @field:ColumnInfo(name = "image")
//    var image: String,
    @field:ColumnInfo(name = "activity_level")
    var activityLevel: String,
    @field:ColumnInfo(name = "bmr")
    var bmr: String,
    @field:ColumnInfo(name = "caloric_intake")
    var caloricIntake: String

)