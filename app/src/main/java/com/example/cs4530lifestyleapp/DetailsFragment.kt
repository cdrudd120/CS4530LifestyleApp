package com.example.cs4530lifestyleapp

import android.content.Context
import android.widget.EditText
import com.example.cs4530lifestyleapp.SubmitFragment.DataPassingInterface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.cs4530lifestyleapp.R
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.lang.ClassCastException

class SubmitFragment : Fragment(), View.OnClickListener {
    private var mEtFullName: EditText? = null
    private var mBtSubmit: Button? = null
    private var mStringFullName: String? = null
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

        //Get stuff
        mEtFullName = view.findViewById(R.id.et_fullname) as EditText
        mBtSubmit = view.findViewById(R.id.button_submit) as Button
        mBtSubmit!!.setOnClickListener(this)
        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {
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