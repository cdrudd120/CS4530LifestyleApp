package com.example.cs4530lifestyleapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.example.cs4530lifestyleapp.DetailsFragment.DetailsDataPassingInterface
import com.example.cs4530lifestyleapp.ListFragment.ListDataPassingInterface
import com.example.cs4530lifestyleapp.ShowDetailsFragment.ShowDetailsDataPassingInterface

import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), DetailsDataPassingInterface, ListDataPassingInterface, ShowDetailsDataPassingInterface {
    // Variables to store data in so we can keep it.
    private var mStringFirstName: String? = null
    private var mStringLastName: String? = null
    private var mWeight: String? = null
    private var mHeight: String? = null
    private var mActivityLevel: String? = null
    private var mAge: String? = null
    private var mLocation: String? = null
    private var mSex: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showButtonFragment()
    }

    private fun showButtonFragment() {
        killFragment()

        val buttonFragment = ListFragment()

        val fTrans = supportFragmentManager.beginTransaction()
        fTrans.replace(R.id.fl_frag_container, buttonFragment, "main_tag")
        fTrans.commit()
    }

    private fun showEditDetailsFragment() {
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

    private fun showDetailsFragment() {
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

    private fun showHikes() {
        val searchUri = Uri.parse("geo:40.767778,-111.845205?q=hikes")
        //Create the implicit intent
        val mapIntent = Intent(Intent.ACTION_VIEW, searchUri)
        //If there's an activity associated with this intent, launch it
        try{
            startActivity(mapIntent)
        }catch(ex: ActivityNotFoundException){
            //handle errors here
        }
    }

    private fun showWeatherFragment() {

    }

    private fun showBMIFragment() {

    }

    override fun passDataDetails(data: Array<String?>?) {
        mStringFirstName = data!![0]
        mStringLastName = data[1]
        mSex = data[2]
        mWeight = data[3]
        mAge = data[4]
        mActivityLevel = data[5]
        mHeight = data[6]
        mLocation = data[7]

        showButtonFragment()
    }

    override fun backFromShowDetails() {
        showButtonFragment()
    }

    override fun passDataList(data: Array<String?>?) {
        val buttonClicked = data!![0]
        if (buttonClicked == "Details") {
            showDetailsFragment()
        } else if (buttonClicked == "Hikes") {
            showHikes()
        } else if (buttonClicked == "Edit") {
            showEditDetailsFragment()
        } else if (buttonClicked == "Weather") {
            showWeatherFragment()
        } else if (buttonClicked == "BMI") {
            showBMIFragment()
        }
    }

    fun killFragment() {
        getSupportFragmentManager().findFragmentByTag("main_tag")?.let {
            getSupportFragmentManager().beginTransaction().remove(it).commit()
        };
    }
}