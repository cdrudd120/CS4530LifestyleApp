package com.example.cs4530lifestyleapp

import androidx.appcompat.app.AppCompatActivity
import com.example.cs4530lifestyleapp.SubmitFragment.DataPassingInterface
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), DataPassingInterface, View.OnClickListener {
    // View Elements
    // TODO: instantiate the rest of the view elements
    private var mTvFirstName: TextView? = null
    private var mTvLastName: TextView? = null
    private var btnEditDetails: Button? = null

    // Variables to store data in so we can keep it.
    private var mStringFirstName: String? = null
    private var mStringLastName: String? = null
    private var weight: Int? = null
    private var height: Int? = null
    private var activityLevel: String? = null
    private var age: Int? = null
    private var location: String? = null
    private var sex: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: get rest of view elements
        mTvFirstName = findViewById<View>(R.id.tv_fn_data) as TextView
        mTvLastName = findViewById<View>(R.id.tv_ln_data) as TextView
        btnEditDetails = findViewById<View>(R.id.btnEditDetails) as Button

        btnEditDetails!!.setOnClickListener(this)
    }

    override fun passData(data: Array<String?>?) {
        // TODO: read all form data, not just the name. Save data to local variables like we do with the name already
        mStringFirstName = data!![0]
        mStringLastName = data[1]
        if (mStringFirstName != null) {
            mTvFirstName!!.text = mStringFirstName
        }
        if (mTvLastName != null) {
            mTvLastName!!.text = mStringLastName
        }

        // Kill fragment
        getSupportFragmentManager().findFragmentByTag("submit_frag")?.let {
            getSupportFragmentManager().beginTransaction().remove(it).commit()
        };
        btnEditDetails!!.setEnabled(true);
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnEditDetails -> {
                //Create the fragment
                val submitFragment = SubmitFragment()

                //Replace the fragment container
                // TODO: we should pass the fragment all the data the user has already input so that if they just want to edit one part they dont have to reenter everything
                val fTrans = supportFragmentManager.beginTransaction()
                fTrans.replace(R.id.fl_frag_container, submitFragment, "submit_frag")
                fTrans.commit()
                btnEditDetails!!.setEnabled(false);
            }
        }
    }
}