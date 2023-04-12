package com.example.cs4530lifestyleapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.lang.ClassCastException

class ShowDetailsFragment : Fragment(), View.OnClickListener{
    private var mDetailsViewModel: DetailsViewModel? = null

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

    private var mDataPasser: ShowDetailsPassing? = null

    //Callback interface
    interface ShowDetailsPassing {
        fun showDetailsCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mDataPasser = try {
            context as ShowDetailsPassing
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DataPassingInterface")
        }

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_details, container, false)

        mDetailsViewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        mDetailsViewModel!!.data.observe(viewLifecycleOwner, dataObserver)

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

        btnBack!!.setOnClickListener(this)

        return view
    }

    private val dataObserver: Observer<DetailsData> =
        Observer { detailsData -> // Update the UI if this data variable changes
            if (detailsData != null) {
                setData(detailsData)
            }
        }

    private fun setData(detailsData: DetailsData) {
        if (detailsData.firstName != null) {
            tvFirstName!!.setText(detailsData.firstName)
        }
        if (detailsData.lastName != null) {
            tvLastName!!.setText(detailsData.lastName)
        }
        tvAge!!.setText(detailsData.age.toString())
        tvWeight!!.setText(detailsData.weight.toString())
        tvHeightFeet!!.setText(detailsData.heightFeet.toString())
        tvHeightInches!!.setText(detailsData.heightInches.toString())
        if (detailsData.location != null) {
            tvLocation!!.setText(detailsData.location)
        }
        if (detailsData.sex != null) {
            tvSex!!.setText(detailsData.sex)
        }
        if (detailsData.activityLevel != null) {
            tvActivityLevel!!.setText(detailsData.activityLevel)
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonBack -> {
                mDataPasser!!.showDetailsCallback()
            }
        }
    }
}