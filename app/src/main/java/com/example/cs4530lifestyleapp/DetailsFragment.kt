package com.example.cs4530lifestyleapp

import android.content.Context
import com.example.cs4530lifestyleapp.SubmitFragment.DataPassingInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.cs4530lifestyleapp.R
import androidx.fragment.app.Fragment
import java.lang.ClassCastException


class SubmitFragment : Fragment(), View.OnClickListener {
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

        //TODO: get all form elements from the view
        mEtFullName = view.findViewById(R.id.et_fullname) as EditText
        mBtSubmit = view.findViewById(R.id.button_submit) as Button
        spActivityLevel = view.findViewById(R.id.spActivityLevel) as Spinner
        mBtSubmit!!.setOnClickListener(this)

        // Add spinner array to activity level spinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.array_activity_level,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spActivityLevel!!.adapter = adapter
        }

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {
                // TODO: Get all other data from the form. This only gets the name field.
                mStringFullName = mEtFullName!!.text.toString()

                //Check if the EditText string is empty
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
                        // TODO: When calling passdata method, we need to pass all of the form data, not just the name
                        mDataPasser!!.passData(splitStrings)
                    } else {
                        Toast.makeText(
                            activity,
                            "Enter only first and last name!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
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