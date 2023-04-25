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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private var mLocation: TextView? = null

    private var btnBack: Button? = null

    private var mDataPasser: WeatherPassing? = null

    private lateinit var mDetailsViewModel: DetailsViewModel

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

        mDetailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
        mDetailsViewModel.wData.observe(viewLifecycleOwner, dataObserver)

        mDescription = view.findViewById(R.id.description) as TextView
        mTemperature = view.findViewById(R.id.temperature) as TextView
        mFeelsLike = view.findViewById(R.id.feelsLike) as TextView
        mMin = view.findViewById(R.id.min) as TextView
        mMax = view.findViewById(R.id.max) as TextView
        mHumidity = view.findViewById(R.id.humidity) as TextView
        mWindSpeed = view.findViewById(R.id.windSpeed) as TextView
        mSunrise = view.findViewById(R.id.sunrise) as TextView
        mSunset = view.findViewById(R.id.sunset) as TextView
        mLocation = view.findViewById(R.id.location) as TextView

        btnBack = view.findViewById(R.id.buttonBack) as Button

        btnBack!!.setOnClickListener(this)

        if (mDetailsViewModel.data.value != null && mDetailsViewModel.data.value!!.location != null) {
            mDetailsViewModel.setWeatherLocation(mDetailsViewModel.data.value!!.location!!.replace(' ', '&'))
        } else {
            mDetailsViewModel.setWeatherLocation("")
        }
        return view
    }

    private val dataObserver: Observer<WeatherData> =
        Observer { weatherData -> // Update the UI if this data variable changes
            updateData(weatherData)
        }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonBack -> {
                mDataPasser!!.weatherCallback()
            }
        }
    }

    fun updateData(wData: WeatherData) {
        mDescription!!.setText(wData.mDescription)
        mTemperature!!.setText(wData.mTemperature)
        mMin!!.setText(wData.mMin)
        mMax!!.setText(wData.mMax)
        mFeelsLike!!.setText(wData.mFeelsLike)
        mHumidity!!.setText(wData.mHumidity)
        mWindSpeed!!.setText(wData.mWindSpeed)
        mSunrise!!.setText(wData.mSunrise)
        mSunset!!.setText(wData.mSunset)
        if (wData.mDefaultLoc) {
            mLocation!!.setText("Salt Lake City,US (Invalid)")
        } else {
            mLocation!!.setText("" + mDetailsViewModel.data.value!!.location!!)
        }
    }
}