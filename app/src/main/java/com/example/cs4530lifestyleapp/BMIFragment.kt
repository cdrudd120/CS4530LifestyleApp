package com.example.cs4530lifestyleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class BMIFragment: Fragment(), View.OnClickListener{
    private lateinit var mDetailsViewModel: DetailsViewModel
    private var tvBMR: TextView? = null //variable for textView on screen
    private var tvCalorieIntake: TextView? = null //variable for textView on screen
    private var btnBack: Button? = null

    private var mDataPasser: BMIPassing? = null

    interface BMIPassing {
        fun bmiCallback()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = try {
            context as BMIPassing
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SubmitFragment.DataPassingInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //get the text views
        val view = inflater.inflate(R.layout.fragment_bmi, container, false)
        tvBMR = view.findViewById<View>(R.id.bmrText) as TextView //links XML variable to code variable
        tvCalorieIntake = view.findViewById<View>(R.id.calorieIntakeText) as TextView

        btnBack = view.findViewById(R.id.buttonBack) as Button
        btnBack!!.setOnClickListener(this)

        mDetailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
        mDetailsViewModel!!.data.observe(viewLifecycleOwner, dataObserver)
        return view
    }

    private val dataObserver: Observer<DetailsData> =
        Observer { detailsData -> // Update the UI if this data variable changes
            if (detailsData != null) {
                if (detailsData.bmr!=null){
                    tvBMR!!.text = "BMR: "+detailsData.bmr
                }
                if(detailsData.caloricIntake!=null){
                    tvCalorieIntake!!.text = "Calorie Intake: " + detailsData.caloricIntake
                }
            }
        }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonBack -> {
                mDataPasser!!.bmiCallback() //do we even need this if we aren't passing data back?
            }
        }
    }
}