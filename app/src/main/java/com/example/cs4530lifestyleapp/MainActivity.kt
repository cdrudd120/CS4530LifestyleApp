package com.example.cs4530lifestyleapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
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

        currPage = "Main"

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
                mBMR=detailsData.bmr
                mCalorieIntake=detailsData.caloricIntake
                updateHeader()
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("PAGE", currPage)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        currPage = savedInstanceState!!.getString("PAGE")
        buttonsCallback(currPage)
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

        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                if (location == null) {
                    Toast.makeText(this, "Sorry, can't get location.", Toast.LENGTH_SHORT)
                } else {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
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

    private fun updateHeader() {
//        calcBMR()
//        calcCaloricIntake()

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

    override fun detailsCallback() {
        currPage = "Main"
        displayButtonFragment()
        updateHeader()
    }

    override fun showDetailsCallback() {
        currPage = "Main"
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
            displayButtonFragment()
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