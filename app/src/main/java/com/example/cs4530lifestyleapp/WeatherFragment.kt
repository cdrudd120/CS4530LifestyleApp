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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


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

        btnBack!!.setOnClickListener(this)

        mFetchWeather.setWeakReference(this)

        mFetchWeather.execute(this, "Salt&Lake&City,us")


        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonBack -> {
                mDataPasser!!.weatherCallback()
            }
        }
    }

    private inner class FetchWeather {
        var weatherFragmentWeakReference: WeakReference<WeatherFragment>? = null
        var executorService: ExecutorService = Executors.newSingleThreadExecutor()
        var mainThreadHandler: Handler = HandlerCompat.createAsync(Looper.getMainLooper())

        fun setWeakReference(ref: WeatherFragment) {
            weatherFragmentWeakReference = WeakReference<WeatherFragment>(ref)
        }
        fun execute(location1: WeatherFragment, location: String?) {
            executorService.execute(Runnable {
                var jsonWeatherData: String?
                val weatherDataURL = NetworkUtils.buildURLFromString(location)
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
                        localRef!!.mTemperature!!.text = "Temperature: " + temperatureObj.getString("temp")
                        localRef!!.mFeelsLike!!.text = "Feels Like: " + temperatureObj.getString("feels_like")
                        localRef!!.mMin!!.text = "Min: " + temperatureObj.getString("temp_min")
                        localRef!!.mMax!!.text = "Max: " + temperatureObj.getString("temp_max")
                        localRef!!.mHumidity!!.text = "Humidity: " + temperatureObj.getString("humidity")
                        localRef!!.mWindSpeed!!.text = "Wind Speed: " + jsonObj.getJSONObject("wind").getString("speed")
                        localRef!!.mSunrise!!.text = "Sunrise: " + jsonObj.getJSONObject("sys").getString("sunrise")
                        localRef!!.mSunset!!.text = "Sunset: " + jsonObj.getJSONObject("sys").getString("sunset")


                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            })
        }

    }
}