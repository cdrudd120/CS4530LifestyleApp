package com.example.cs4530lifestyleapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4530lifestyleapp.DetailsFragment.DetailsPassing
import com.example.cs4530lifestyleapp.ListFragment.ListPassing
import com.example.cs4530lifestyleapp.ShowDetailsFragment.ShowDetailsPassing
import com.example.cs4530lifestyleapp.WeatherFragment.WeatherPassing
import com.example.cs4530lifestyleapp.BMIFragment.BMIPassing
import com.google.android.gms.location.FusedLocationProviderClient

import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity(), DetailsPassing, ListPassing, ShowDetailsPassing, WeatherPassing, BMIPassing {
    // Variables to store data in so we can keep it.
    private var mStringFirstName: String? = null
    private var mStringLastName: String? = null
    private var mWeight: String? = null
    private var mHeight: String? = null
    private var mActivityLevel: String? = null
    private var mAge: String? = null
    private var mLocation: String? = null
    private var mSex: String? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude: String? = null
    private var longtitude: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        displayButtonFragment()
    }

    private fun displayButtonFragment() {
        killFragment()

        val buttonFragment = ListFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, buttonFragment, "main_tag")
        fTrans.commit()
    }

    private fun displayEditDetailsFragment() {
        killFragment()

        val detailsFragment = DetailsFragment()

        val sentData = Bundle()
        sentData.putString("FN_DATA", mStringFirstName)
        sentData.putString("LN_DATA", mStringLastName)
        sentData.putString("AGE_DATA", mAge)
        sentData.putString("SEX_DATA", mSex)
        sentData.putString("HEIGHT_DATA", mHeight)
        sentData.putString("WEIGHT_DATA", mWeight)
        sentData.putString("LOCATION_DATA", mLocation)
        sentData.putString("ACTIVITYLEVEL_DATA", mActivityLevel)
        detailsFragment.arguments = sentData

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, detailsFragment, "main_tag")
        fTrans.commit()
    }

    private fun displayDetailsFragment() {
        killFragment()

        val showDetailsFragment = ShowDetailsFragment()

        val sentData = Bundle()
        sentData.putString("FN_DATA", mStringFirstName)
        sentData.putString("LN_DATA", mStringLastName)
        sentData.putString("AGE_DATA", mAge)
        sentData.putString("SEX_DATA", mSex)
        sentData.putString("HEIGHT_DATA", mHeight)
        sentData.putString("WEIGHT_DATA", mWeight)
        sentData.putString("LOCATION_DATA", mLocation)
        sentData.putString("ACTIVITYLEVEL_DATA", mActivityLevel)
        showDetailsFragment.arguments = sentData

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, showDetailsFragment, "main_tag")
        fTrans.commit()
    }

    private fun displayHikes() {
        getLocation()

        val longAndLatString = "geo:$longtitude,$latitude?q=hikes"
        val searchUri = Uri.parse(longAndLatString)
        //Create the implicit intent
        val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)
        //If there's an activity associated with this intent, launch it
        try{
            startActivity(mapIntent)
        }catch(ex: ActivityNotFoundException){
            //handle errors here
        }
    }

    private fun getLocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            !=PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "We need location permission.", Toast.LENGTH_SHORT)
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)
            return
        }

        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it == null) {
                Toast.makeText(this, "Sorry, can't get location.", Toast.LENGTH_SHORT)
            }
            else {
                latitude = it.latitude.toString()
                longtitude = it.longitude.toString()
            }

        }
    }

    private fun displayWeatherFragment() {
        killFragment()

        val weatherFragment = WeatherFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, weatherFragment, "main_tag")
        fTrans.commit()
    }

    private fun displayBMIFragment() {
        killFragment()

        val bmiFragment = BMIFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, bmiFragment, "main_tag")
        fTrans.commit()
    }

    override fun detailsCallback(data: Array<String?>?) {
        mStringFirstName = data!![0]
        mStringLastName = data[1]
        mSex = data[2]
        mWeight = data[3]
        mAge = data[4]
        mActivityLevel = data[5]
        mHeight = data[6]
        mLocation = data[7]

        displayButtonFragment()
    }

    override fun showDetailsCallback() {
        displayButtonFragment()
    }

    override fun buttonsCallback(data: String?) {
        if (data == "Details") {
            displayDetailsFragment()
        } else if (data == "Hikes") {
            displayHikes()
        } else if (data == "Edit") {
            displayEditDetailsFragment()
        } else if (data == "Weather") {
            displayWeatherFragment()
        } else if (data == "BMI") {
            displayBMIFragment()
        }
    }

    override fun bmiCallback() {
        displayButtonFragment()
    }

    override fun weatherCallback() {
        displayButtonFragment()
    }

    fun killFragment() {
        getSupportFragmentManager().findFragmentByTag("main_tag")?.let {
            getSupportFragmentManager().beginTransaction().remove(it).commit()
        };
    }
}