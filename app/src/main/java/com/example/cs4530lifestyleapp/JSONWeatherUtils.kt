package com.example.cs4530lifestyleapp


import kotlin.Throws
import org.json.JSONException
import org.json.JSONObject
import kotlin.math.roundToInt


//Declare methods as static. We don't want to create objects of this class.
object JSONWeatherUtils {
    @Throws(JSONException::class)
    fun getWeatherData(data: String?, defaultLoc: Boolean): WeatherData {
        val weatherData = WeatherData()
        weatherData.mDefaultLoc = defaultLoc

        var jsonObj = JSONObject(data!!)
        var weatherObj = jsonObj.getJSONArray("weather")
        val myMap: MutableMap<String, String> = HashMap()
        val s = weatherObj.getString(0)
        val pairs =
            s.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in pairs.indices) {
            val pair = pairs[i]
            val keyValue = pair.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            myMap[keyValue[0]] = keyValue[1]
        }
        weatherData.mDescription = myMap.getValue("\"description\"").removeSurrounding("\"")
        var temperatureObj = jsonObj.getJSONObject("main")
        weatherData.mTemperature = "" + (temperatureObj.getString("temp").toDouble() - 273.15).roundToInt().toString() + "C"
        weatherData.mFeelsLike = "" + (temperatureObj.getString("feels_like").toDouble() - 273.15).roundToInt().toString() + "C"
        weatherData.mMin = "" + (temperatureObj.getString("temp_min").toDouble() - 273.15).roundToInt().toString() + "C"
        weatherData.mMax = "" + (temperatureObj.getString("temp_max").toDouble() - 273.15).roundToInt().toString() + "C"
        weatherData.mHumidity = "" + temperatureObj.getString("humidity")
        weatherData.mWindSpeed = "" + jsonObj.getJSONObject("wind").getString("speed")

        val date1 = java.util.Date(jsonObj.getJSONObject("sys").getString("sunrise").toLong() * 1000)
        weatherData.mSunrise = "" + date1

        val date2 = java.util.Date(jsonObj.getJSONObject("sys").getString("sunset").toLong() * 1000)
        weatherData.mSunset = "" + date2

        return weatherData
    }
}