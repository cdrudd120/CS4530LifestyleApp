package com.example.cs4530lifestyleapp

import androidx.appcompat.app.AppCompatActivity
import com.example.cs4530lifestyleapp.SubmitFragment.DataPassingInterface
import android.widget.TextView
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity(), DataPassingInterface, View.OnClickListener {
    private var mStringFirstName: String? = null
    private var mStringLastName: String? = null
    private var mTvFirstName: TextView? = null
    private var mTvLastName: TextView? = null
    private var btnEditDetails: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get stuff
        mTvFirstName = findViewById<View>(R.id.tv_fn_data) as TextView
        mTvLastName = findViewById<View>(R.id.tv_ln_data) as TextView
        btnEditDetails = findViewById<View>(R.id.btnEditDetails) as Button

        btnEditDetails!!.setOnClickListener(this)
    }

    override fun passData(data: Array<String?>?) {
        mStringFirstName = data!![0]
        mStringLastName = data[1]
        if (mStringFirstName != null) {
            mTvFirstName!!.text = mStringFirstName
        }
        if (mTvLastName != null) {
            mTvLastName!!.text = mStringLastName
        }
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
                val fTrans = supportFragmentManager.beginTransaction()
                fTrans.replace(R.id.fl_frag_container, submitFragment, "submit_frag")
                fTrans.commit()
                btnEditDetails!!.setEnabled(false);
            }
        }
    }
}