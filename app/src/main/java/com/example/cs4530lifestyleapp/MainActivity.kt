package com.example.cs4530lifestyleapp

import androidx.appcompat.app.AppCompatActivity
import com.example.cs4530lifestyleapp.DetailsFragment.DataPassingInterface
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), DataPassingInterface, View.OnClickListener {
    // View Elements
    // TODO: instantiate the rest of the view elements
    private var btnShowDetails: Button? = null
    private var btnEditDetails: Button? = null

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

        btnEditDetails = findViewById<View>(R.id.btnEditDetails) as Button
        btnShowDetails = findViewById<View>(R.id.btnShowDetails) as Button

        btnEditDetails!!.setOnClickListener(this)
        btnShowDetails!!.setOnClickListener(this)
    }

    override fun passData(data: Array<String?>?) {
        // TODO: read all form data, not just the name. Save data to local variables like we do with the name already
        mStringFirstName = data!![0]
        mStringLastName = data[1]
        mSex = data[2]
        mWeight = data[3]
        mAge = data[4]

        btnEditDetails!!.setEnabled(true);
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnEditDetails -> {
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
                fTrans.replace(R.id.fl_frag_container, detailsFragment, "submit_frag")
                fTrans.commit()
            }
            R.id.btnShowDetails -> {
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
                fTrans.replace(R.id.fl_frag_container, showDetailsFragment, "submit_frag")
                fTrans.commit()
            }
        }
    }

    fun killFragment() {
        getSupportFragmentManager().findFragmentByTag("submit_frag")?.let {
            getSupportFragmentManager().beginTransaction().remove(it).commit()
        };
    }
}