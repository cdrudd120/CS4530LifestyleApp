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
    private var mEtFullName: EditText? = null
    private var mBtSubmit: Button? = null
    private var mStringFullName: String? = null
    private var spActivityLevel: Spinner? = null

    var mDataPasser: DataPassingInterface? = null

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
        spActivityLevel = view.findViewById(R.id.spActivityLevel) as Spinner

        mBtSubmit = view.findViewById(R.id.button_submit) as Button
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
                val activityLevel = spActivityLevel!!.getSelectedItem().toString();

                var firstName: String? = null
                var lastName: String? = null
                mStringFullName = mEtFullName!!.text.toString()
                if (mStringFullName.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(activity, "Enter a name first!", Toast.LENGTH_SHORT).show()
                } else {
                    //Reward them for submitting their names
                    Toast.makeText(activity, "Good job!", Toast.LENGTH_SHORT).show()

                    //Remove any leading spaces or tabs
                    mStringFullName = mStringFullName!!.replace("^\\s+".toRegex(), "")

                    //Separate the string into first and last name using simple Java stuff
                    val splitStrings: Array<String?> =
                        mStringFullName!!.split("\\s+".toRegex()).toTypedArray()
                    if (splitStrings.size == 1) {
                        Toast.makeText(
                            activity,
                            "Enter both first and last name!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (splitStrings.size == 2) {
                        firstName = splitStrings[0]
                        lastName = splitStrings[1]
                    } else {
                        Toast.makeText(
                            activity,
                            "Enter only first and last name!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                var stringArray = arrayOf(firstName, lastName, activityLevel)
                mDataPasser!!.passData(stringArray)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
}