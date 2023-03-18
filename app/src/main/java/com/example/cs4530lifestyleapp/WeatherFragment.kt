package com.example.cs4530lifestyleapp

import NetworkUtils
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.HandlerCompat
import androidx.fragment.app.Fragment
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.roundToInt


class WeatherFragment: Fragment(), View.OnClickListener{

    private var mDescription: TextView? = null
    private var mTemperature: TextView? = null
    private var mFeelsLike: TextView? = null
    private var mMin: TextView? = null
    private var mMax: TextView? = null
    private var mHumidity: TextView? = null
    private var mWindSpeed: TextView? = null
    private var mSunrise: TextView? = null
    private var mSunset: TextView? = null

    private var btnBack: Button? = null

    private var mDataPasser: WeatherPassing? = null

    private val mFetchWeather: FetchWeather = FetchWeather()

    private var latitude: String? = null
    private var longitude: String? = null
    private var defaultLocation:String? = "Salt&Lake&City,us"

    interface WeatherPassing {
        fun weatherCallback()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = try {
            context as WeatherPassing
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SubmitFragment.DataPassingInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        //Get the data that was sent in
        val incomingBundle = arguments
        latitude = incomingBundle!!.getString("LAT")
        longitude = incomingBundle!!.getString("LONG")

        mDescription = view.findViewById(R.id.description) as TextView
        mTemperature = view.findViewById(R.id.temperature) as TextView
        mFeelsLike = view.findViewById(R.id.feelsLike) as TextView
        mMin = view.findViewById(R.id.min) as TextView
        mMax = view.findViewById(R.id.max) as TextView
        mHumidity = view.findViewById(R.id.humidity) as TextView
        mWindSpeed = view.findViewById(R.id.windSpeed) as TextView
        mSunrise = view.findViewById(R.id.sunrise) as TextView
        mSunset = view.findViewById(R.id.sunset) as TextView

        btnBack = view.findViewById(R.id.buttonBack) as Button

        if(savedInstanceState != null){
            mDescription!!.setText(savedInstanceState.getString("DESCRIPTION"))
            mTemperature!!.setText(savedInstanceState.getString("TEMPERATURE"))
            mMin!!.setText(savedInstanceState.getString("MIN"))
            mMax!!.setText(savedInstanceState.getString("MAX"))
            mHumidity!!.setText(savedInstanceState.getString("HUMIDITY"))
            mWindSpeed!!.setText(savedInstanceState.getString("WIND"))
            mSunrise!!.setText(savedInstanceState.getString("SUNRISE"))
            mSunset!!.setText(savedInstanceState.getString("SUNSET"))
        }

        btnBack!!.setOnClickListener(this)

        mFetchWeather.setWeakReference(this)

        mFetchWeather.execute(this)


        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonBack -> {
                mDataPasser!!.weatherCallback()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("DESCRIPTION", mDescription!!.text.toString())
        outState.putString("TEMPERATURE", mTemperature!!.text.toString())
        outState.putString("FEELS", mFeelsLike!!.text.toString())
        outState.putString("MIN", mMin!!.text.toString())
        outState.putString("MAX", mMax!!.text.toString())
        outState.putString("HUMIDITY", mHumidity!!.text.toString())
        outState.putString("WIND", mWindSpeed!!.text.toString())
        outState.putString("SUNRISE", mSunrise!!.text.toString())
        outState.putString("SUNSET", mSunset!!.text.toString())
        outState.putString("LATITUDE", latitude)
        outState.putString("LONGITUDE", longitude)

    }

    private inner class FetchWeather {
        var weatherFragmentWeakReference: WeakReference<WeatherFragment>? = null
        var executorService: ExecutorService = Executors.newSingleThreadExecutor()
        var mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

        fun setWeakReference(ref: WeatherFragment) {
            weatherFragmentWeakReference = WeakReference<WeatherFragment>(ref)
        }
        fun execute(location1: WeatherFragment) {
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
                    postToMainThread(jsonWeatherData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }

        fun postToMainThread(jsonWeatherData: String?) {
            val localRef = weatherFragmentWeakReference!!.get()

            mainThreadHandler.post(Runnable {
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
                        localRef!!.mDescription!!.text = myMap.getValue("\"description\"").removeSurrounding("\"")
                        var temperatureObj = jsonObj.getJSONObject("main")
                        localRef!!.mTemperature!!.text = "Temperature: " + (temperatureObj.getString("temp").toDouble() - 273.15).roundToInt().toString() + "C"
                        localRef!!.mFeelsLike!!.text = "Feels Like: " + (temperatureObj.getString("feels_like").toDouble() - 273.15).roundToInt().toString() + "C"
                        localRef!!.mMin!!.text = "Min: " + (temperatureObj.getString("temp_min").toDouble() - 273.15).roundToInt().toString() + "C"
                        localRef!!.mMax!!.text = "Max: " + (temperatureObj.getString("temp_max").toDouble() - 273.15).roundToInt().toString() + "C"
                        localRef!!.mHumidity!!.text = "Humidity: " + temperatureObj.getString("humidity")
                        localRef!!.mWindSpeed!!.text = "Wind Speed: " + jsonObj.getJSONObject("wind").getString("speed")

                        val date1 = java.util.Date(jsonObj.getJSONObject("sys").getString("sunrise").toLong() * 1000)
                        localRef!!.mSunrise!!.text = "Sunrise: " + date1

                        val date2 = java.util.Date(jsonObj.getJSONObject("sys").getString("sunset").toLong() * 1000)
                        localRef!!.mSunset!!.text = "Sunset: " + date2


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        }

    }
}