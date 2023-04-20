package com.example.cs4530lifestyleapp

import android.os.Handler
import android.os.Looper
import androidx.core.os.HandlerCompat
import androidx.lifecycle.MutableLiveData
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt

class FetchWeather() {
    var executorService: ExecutorService = Executors.newSingleThreadExecutor()
    var mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

    fun execute(latitude: String?, longitude: String?, defaultLocation: String, repository: Repository) {
        executorService.execute(Runnable {
            var jsonWeatherData: String?

            val weatherDataURL: URL?
            if(latitude == null || longitude == null){
                weatherDataURL = NetworkUtils.buildURLFromString(defaultLocation)
            }
            else{
                weatherDataURL = NetworkUtils.buildURLFromCoords(latitude, longitude)
            }
            try {
                jsonWeatherData = NetworkUtils.getDataFromURL(weatherDataURL)
                postToMainThread(jsonWeatherData, repository)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }

    fun postToMainThread(jsonWeatherData: String?, repository: Repository) {
        mainThreadHandler.post(Runnable {
            var weatherData = WeatherData()
            if (jsonWeatherData != null) {
                try {
                    var jsonObj = JSONObject(jsonWeatherData);
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
                    weatherData.mTemperature = "Temperature: " + (temperatureObj.getString("temp").toDouble() - 273.15).roundToInt().toString() + "C"
                    weatherData.mFeelsLike = "Feels Like: " + (temperatureObj.getString("feels_like").toDouble() - 273.15).roundToInt().toString() + "C"
                    weatherData.mMin = "Min: " + (temperatureObj.getString("temp_min").toDouble() - 273.15).roundToInt().toString() + "C"
                    weatherData.mMax = "Max: " + (temperatureObj.getString("temp_max").toDouble() - 273.15).roundToInt().toString() + "C"
                    weatherData.mHumidity = "Humidity: " + temperatureObj.getString("humidity")
                    weatherData.mWindSpeed = "Wind Speed: " + jsonObj.getJSONObject("wind").getString("speed")

                    val date1 = java.util.Date(jsonObj.getJSONObject("sys").getString("sunrise").toLong() * 1000)
                    weatherData.mSunrise = "Sunrise: " + date1

                    val date2 = java.util.Date(jsonObj.getJSONObject("sys").getString("sunset").toLong() * 1000)
                    weatherData.mSunset = "Sunset: " + date2

                    repository.setWeatherData(weatherData)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

}