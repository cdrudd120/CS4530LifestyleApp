package com.example.cs4530lifestyleapp

import android.content.Context
import com.example.cs4530lifestyleapp.DetailsFragment.DataPassingInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.cs4530lifestyleapp.R
import androidx.fragment.app.Fragment
import java.lang.ClassCastException


class DetailsFragment : Fragment(), View.OnClickListener {
    // TODO: create variables for all form elements
    private var mWeight: EditText? = null
    private var mSex: RadioGroup? = null
    private var rbSexMale: RadioButton? = null
    private var rbSexFemale: RadioButton? = null
    private var mAge: EditText? = null

    private var mEtFullName: EditText? = null
    private var mBtSubmit: Button? = null
    private var mStringFullName: String? = null
    private var spActivityLevel: Spinner? = null

    private var mDataPasser: DataPassingInterface? = null
    private var DataArray: Array<String?> = Array(7, {null})

    //Callback interface
    interface DataPassingInterface {
        fun passData(data: Array<String?>?)
    }

    //Associate the callback with this Fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = try {
            context as DataPassingInterface
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SubmitFragment.DataPassingInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)


        mEtFullName = view.findViewById(R.id.et_fullname) as EditText
        mWeight = view.findViewById(R.id.etWeight) as EditText
        mSex = view.findViewById(R.id.rgSex) as RadioGroup
        rbSexMale = view.findViewById(R.id.rbSexMale) as RadioButton
        rbSexFemale = view.findViewById(R.id.rbSexFemale) as RadioButton
        mAge = view.findViewById(R.id.etAge) as EditText

        mBtSubmit = view.findViewById(R.id.button_submit) as Button
        spActivityLevel = view.findViewById(R.id.spActivityLevel) as Spinner
        mBtSubmit!!.setOnClickListener(this)

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

        //Set the data
        if (firstName != null) {
            mEtFullName!!.setText(firstName + " " + lastName)
        }

        val spArray = arrayOf("Sedentary: little or no exercise", "Exercise 1-3 times/week", "Exercise 4-5 times/week", "Daily exercise or intense exercise 3-4 times/week", "Intense exercise 6-7 times/week", "Very intense exercise daily, or physical job")
        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spActivityLevel!!.adapter = adapter
        if (activityLevel != null) {
            val spinnerPosition: Int = adapter.getPosition(activityLevel)
            spActivityLevel!!.setSelection(spinnerPosition)
        }

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {
                // TODO: Get all other data from the form. This only gets the name field.
                mStringFullName = mEtFullName!!.text.toString()

                //Check if the Name is empty, it is the only required input field
                if (mStringFullName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(activity, "Enter a name first!", Toast.LENGTH_SHORT).show()
                }

                else {
                    checkFirstAndLast()
                    checkSex()
                    checkWeight()
                    checkAge()
                    mDataPasser!!.passData(DataArray)
                }
            }
        }
    }

    private fun checkFirstAndLast() {
        //Remove any leading spaces or tabs
        mStringFullName = mStringFullName!!.replace("^\\s+".toRegex(), "")
        //Separate the string into first and last name using simple Java stuff
        val splitStrings: Array<String?> = mStringFullName!!.split("\\s+".toRegex()).toTypedArray()
        if (splitStrings.size == 1) {
            Toast.makeText(
                activity,
                "Enter both first and last name!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (splitStrings.size == 2) {
            //Reward them for submitting their names
            Toast.makeText(activity, "Good job!", Toast.LENGTH_SHORT).show()
            DataArray[0] = splitStrings[0]
            DataArray[1] = splitStrings[1]
            // TODO: When calling passdata method, we need to pass all of the form data, not just the name
            //mDataPasser!!.passFirstAndLast(splitStrings)
        } else {
            Toast.makeText(
                activity,
                "Enter only first and last name!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private  fun checkSex(){
        if (rbSexMale!!.isChecked) {
            DataArray[2] = "Male"
        }
        else if (rbSexFemale!!.isChecked) {
            DataArray[2] = "Female"
        }
        else {
            DataArray[2] = "NA"
        }
    }

    private fun checkWeight() {
        val weightString: String? =  mWeight!!.text.toString()
        if (weightString != null) {
            DataArray[3] = weightString
        }
    }

    private fun checkAge() {
        val ageString: String? = mAge!!.text.toString()
        if (ageString != null) {
            DataArray[4] = ageString
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
}