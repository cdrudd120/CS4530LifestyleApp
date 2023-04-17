package com.example.cs4530lifestyleapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.cs4530lifestyleapp.BMIFragment.BMIPassing
import com.example.cs4530lifestyleapp.DetailsFragment.DetailsPassing
import com.example.cs4530lifestyleapp.ListFragment.ListPassing
import com.example.cs4530lifestyleapp.ShowDetailsFragment.ShowDetailsPassing
import com.example.cs4530lifestyleapp.WeatherFragment.WeatherPassing
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(), DetailsPassing, ListPassing, ShowDetailsPassing, WeatherPassing, BMIPassing {
    // Variables to store data in so we can keep it.
    //private var mDetailsViewModel: ViewModel? = null
    private var mWeight: String? = null
    private var mHeight: String? = null
    private var mActivityLevel: String? = null
    private var mAge: String? = null
    private var mSex: String? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude: String? = null
    private var longitude: String? = null
    private var mBMR: String? = null
    private var mCalorieIntake: String? = null
    private var mImageFilepath: String? = null

    private var currPage: String? = null

    private var tablet: Boolean = false;

    private var PERMISSION_ID: Int = 1000;

    private val mViewModel: DetailsViewModel by viewModels {
        DetailsViewModelFactory((application as LifestyleApplication).repository)
    }


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if(getResources().getBoolean(R.bool.isTablet)) {
            tablet = true;
        }

//        mDetailsViewModel = ViewModelProvider(this)[ViewModel::class.java]
        mViewModel!!.data.observe(this, dataObserver)

        updateHeader()
        displayButtonFragment()
    }

    private val dataObserver: Observer<DetailsData> =
        Observer { detailsData -> // Update the UI if this data variable changes
            if (detailsData != null) {
                mImageFilepath = detailsData.imageFilepath
                if (detailsData.weight != null) {
                    mWeight = detailsData.weight.toString()
                }
                if (detailsData.heightInches != null) {
                    mHeight = ((detailsData.heightFeet!! * 12) + detailsData.heightInches!!).toString()
                }
                if (detailsData.age != null) {
                    mAge = detailsData.age.toString()
                }
                mSex = detailsData.sex
                mActivityLevel = detailsData.activityLevel
                updateHeader()
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("CURRPAGE_DATA", currPage)
        outState.putString("BMR_DATA", mBMR)
        outState.putString("CALORIEINTAKE_DATA", mCalorieIntake)
        outState.putString("LAT", latitude)
        outState.putString("LONG", longitude)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        mBMR = savedInstanceState!!.getString("BMR_DATA")
        mCalorieIntake = savedInstanceState!!.getString("CALORIEINTAKE_DATA")
        latitude = savedInstanceState!!.getString("LAT")
        longitude = savedInstanceState!!.getString("LONG")

        currPage = savedInstanceState!!.getString("CURRPAGE_DATA")
        buttonsCallback(currPage)

        updateHeader()
    }

    private fun displayButtonFragment() {
        killFragment()

        val buttonFragment = ListFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        if (tablet) {
            fTrans.replace(R.id.fragList, buttonFragment, "button_tag")
        } else {
            fTrans.replace(R.id.fl_frag_container, buttonFragment, "main_tag")
        }
        fTrans.commit()
    }

    private fun displayEditDetailsFragment() {
        killFragment()

        val detailsFragment = DetailsFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, detailsFragment, "main_tag")
        fTrans.commit()
    }

    private fun displayDetailsFragment() {
        killFragment()

        val showDetailsFragment = ShowDetailsFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, showDetailsFragment, "main_tag")
        fTrans.commit()
    }

    private fun displayHikes() {
        getLocation()

        val longAndLatString = "geo:$longitude,$latitude?q=hikes"
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
        }

        val location = fusedLocationProviderClient.lastLocation
        location.addOnSuccessListener {
            if(it == null) {
                Toast.makeText(this, "Sorry, can't get location.", Toast.LENGTH_SHORT)
            }
            else {
                latitude = it.latitude.toString()
                longitude = it.longitude.toString()
            }

        }
    }

    private fun displayWeatherFragment() {
        killFragment()

        val weatherFragment = WeatherFragment()

        getLocation()

        val sentData = Bundle()
        sentData.putString("LAT", latitude)
        sentData.putString("LONG", longitude)
        weatherFragment.arguments = sentData

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, weatherFragment, "main_tag")
        fTrans.commit()
    }

    private fun displayBMIFragment() {
        killFragment()

        val bmiFragment = BMIFragment()

        val sentData = Bundle()
        sentData.putString("BMR_DATA", mBMR)
        sentData.putString("CALORIEINTAKE_DATA", mCalorieIntake)
        bmiFragment.arguments=sentData

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, bmiFragment, "main_tag")
        fTrans.commit()
    }

    private fun updateHeader() {
        calcBMR()
        calcCaloricIntake()

        val actionBar: ActionBar? = supportActionBar
        actionBar!!.setDisplayOptions(
            actionBar.getDisplayOptions()
                    or ActionBar.DISPLAY_SHOW_CUSTOM
        )
        actionBar!!.setTitle("Lifestyle App");
        if (mCalorieIntake != null) {
            actionBar!!.setSubtitle("Calories: " + mCalorieIntake);
        }

        val imageView = ImageView(actionBar.getThemedContext())
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER)
        val profilePicture = BitmapFactory.decodeFile(mImageFilepath)
        if (profilePicture != null) {
            imageView.setImageBitmap(profilePicture)
        }
        val layoutParams: ActionBar.LayoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, (Gravity.RIGHT
                    or Gravity.CENTER_VERTICAL)
        )
        layoutParams.rightMargin = 0
        imageView.setLayoutParams(layoutParams)
        actionBar.setCustomView(imageView)
    }

    private fun calcBMR() {
        if (mSex=="Female"){
            var BMR = 447.593 + (9.247*(mWeight?.toInt()!!)*0.453592) + (3.098*(mHeight?.toInt()!!)*2.54) + (-4.330*(mAge?.toInt()!!))
            //Log.d("BMR",BMR.toString())
            mBMR= BMR.roundToInt().toString()
        }
        if (mSex=="Male"){
            var BMR = 88.362 + (13.397*(mWeight?.toInt()!!)*0.453592) + (4.799*(mHeight?.toInt()!!)*2.54) + (-5.677*(mAge?.toInt()!!))
            //Log.d("BMR",BMR.toString())
            mBMR= BMR.roundToInt().toString()
        }
    }

    private fun calcCaloricIntake() {
        if (mActivityLevel=="Sedentary: little or no exercise"){
            mCalorieIntake=(mBMR!!.toInt()*1.2).roundToInt().toString()
        }
        else if (mActivityLevel=="Exercise 1-3 times/week"){
            mCalorieIntake=(mBMR!!.toInt()*1.375).roundToInt().toString()
        }
        else if (mActivityLevel=="Moderate Exercise 3-5 times/week"){
            mCalorieIntake=(mBMR!!.toInt()*1.55).roundToInt().toString()
        }
        else if (mActivityLevel=="Very Active 6-7 days/wk"){
            mCalorieIntake=(mBMR!!.toInt()*1.725).roundToInt().toString()
        }
        else if (mActivityLevel=="Extremely active (intense exercise/physical job)"){
            mCalorieIntake=(mBMR!!.toInt()*1.9).roundToInt().toString()
        }
    }

    override fun detailsCallback() {
        currPage = "Main"

        displayButtonFragment()

        updateHeader()
    }

    override fun showDetailsCallback() {
        displayButtonFragment()
    }

    override fun buttonsCallback(data: String?) {
        if (data == "Details") {
            currPage = "Details"
            displayDetailsFragment()
        } else if (data == "Hikes") {
            displayHikes()
        } else if (data == "Edit") {
            currPage = "Edit"
            displayEditDetailsFragment()
        } else if (data == "Weather") {
            currPage = "Weather"
            displayWeatherFragment()
        } else if (data == "BMI") {
            currPage = "BMI"
            displayBMIFragment()
        } else if (data == "Main") {
            currPage = "Main"
        }
    }

    override fun bmiCallback() {
        currPage = "Main"
        displayButtonFragment()
    }

    override fun weatherCallback() {
        currPage = "Main"
        displayButtonFragment()
    }

    fun killFragment() {
        getSupportFragmentManager().findFragmentByTag("main_tag")?.let {
            getSupportFragmentManager().beginTransaction().remove(it).commit()
        };
    }
}