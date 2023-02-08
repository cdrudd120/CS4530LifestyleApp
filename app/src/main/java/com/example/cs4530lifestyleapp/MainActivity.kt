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
    private var weight: String? = null
    private var height: String? = null
    private var activityLevel: String? = null
    private var age: String? = null
    private var location: String? = null
    private var sex: String? = null

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
        mStringLastName = data!![1]
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnEditDetails -> {
                killFragment()

                val detailsFragment = DetailsFragment()

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
                sentData.putString("AGE_DATA", age)
                sentData.putString("SEX_DATA", sex)
                sentData.putString("HEIGHT_DATA", height)
                sentData.putString("WEIGHT_DATA", weight)
                sentData.putString("LOCATION_DATA", location)
                sentData.putString("ACTIVITYLEVEL_DATA", activityLevel)
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

    fun enableButtons() {
        btnShowDetails!!.setEnabled(true)
        btnEditDetails!!.setEnabled(true)
    }

    fun disableButtons() {
        btnShowDetails!!.setEnabled(false)
        btnEditDetails!!.setEnabled(false)
    }
}