package com.example.cs4530lifestyleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class ShowDetailsFragment : Fragment(), View.OnClickListener{
    private var tvFirstName: TextView? = null
    private var tvLastName: TextView? = null
    private var tvAge: TextView? = null
    private var tvSex: TextView? = null
    private var tvWeight: TextView? = null
    private var tvHeightFeet: TextView? = null
    private var tvHeightInches: TextView? = null
    private var tvLocation: TextView? = null
    private var tvActivityLevel: TextView? = null
    private var btnBack: Button? = null

    private var mDataPasser: ShowDetailsDataPassingInterface? = null

    //Callback interface
    interface ShowDetailsDataPassingInterface {
        fun backFromShowDetails()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mDataPasser = try {
            context as ShowDetailsDataPassingInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DataPassingInterface")
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_details, container, false)

        //Get the text views
        tvFirstName = view.findViewById<View>(R.id.tv_fn_data) as TextView
        tvLastName = view.findViewById<View>(R.id.tv_ln_data) as TextView
        tvAge = view.findViewById<View>(R.id.tv_age_data) as TextView
        tvSex = view.findViewById<View>(R.id.tv_sex_data) as TextView
        tvWeight = view.findViewById<View>(R.id.tv_weight_data) as TextView
        tvHeightFeet = view.findViewById<View>(R.id.tv_height_feet_data) as TextView
        tvHeightInches = view.findViewById<View>(R.id.tv_height_inches_data) as TextView
        tvLocation = view.findViewById<View>(R.id.tv_location_data) as TextView
        tvActivityLevel = view.findViewById<View>(R.id.tv_activityLevel_data) as TextView
        btnBack = view.findViewById<View>(R.id.buttonBack) as Button

        //Get the data that was sent in
        val incomingBundle = arguments
        val firstName = incomingBundle!!.getString("FN_DATA")
        val lastName = incomingBundle!!.getString("LN_DATA")
        val age = incomingBundle!!.getString("AGE_DATA")
        val sex = incomingBundle!!.getString("SEX_DATA")
        val weight = incomingBundle!!.getString("WEIGHT_DATA")
        val height = incomingBundle!!.getString("HEIGHT_DATA")
        val location = incomingBundle!!.getString("LOCATION_DATA")
        val activityLevel = incomingBundle!!.getString("ACTIVITYLEVEL_DATA")


        if (firstName != null) {
            tvFirstName!!.text = firstName
        }
        if (lastName != null) {
            tvLastName!!.text = lastName
        }
        if (age != null) {
            tvAge!!.text = age
        }
        if (sex != null) {
            tvSex!!.text = sex
        }
        if (weight != null) {
            tvWeight!!.text = weight
        }
        if (height != null) {
            tvHeightFeet!!.text = (Math.floorDiv(height!!.toInt(), 12)).toString()
        }
        if (height != null) {
            tvHeightInches!!.text = (height!!.toInt() % 12).toString()
        }
        if (location != null) {
            tvLocation!!.text = location
        }
        if (activityLevel != null) {
            tvActivityLevel!!.text = activityLevel
        }

        btnBack!!.setOnClickListener(this);

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonBack -> {
                mDataPasser!!.backFromShowDetails()
            }
        }
    }
}