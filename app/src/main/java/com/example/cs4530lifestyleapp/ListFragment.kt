package com.example.cs4530lifestyleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ListFragment : Fragment(), View.OnClickListener {

    private var mDataPasser: ListPassing? = null

    private var btnEditDetails: Button? = null
    private var btnDetails: Button? = null
    private var btnHikes: Button? = null
    private var btnWeather: Button? = null
    private var btnBMI: Button? = null

    interface ListPassing {
        fun buttonsCallback(data: String?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = try {
            context as ListPassing
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement SubmitFragment.DataPassingInterface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        btnDetails = view.findViewById(R.id.btnShowDetails)
        btnEditDetails = view.findViewById(R.id.btnEditDetails)
        btnHikes = view.findViewById(R.id.btnHikes)
        btnBMI = view.findViewById(R.id.btnBMI)
        btnWeather = view.findViewById(R.id.btnWeather)

        btnDetails!!.setOnClickListener(this)
        btnHikes!!.setOnClickListener(this)
        btnEditDetails!!.setOnClickListener(this)
        btnBMI!!.setOnClickListener(this)
        btnWeather!!.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnShowDetails -> {
                mDataPasser!!.buttonsCallback("Details")
            }
            R.id.btnEditDetails -> {
                mDataPasser!!.buttonsCallback("Edit")
            }
            R.id.btnHikes -> {
                mDataPasser!!.buttonsCallback("Hikes")
            }
            R.id.btnWeather -> {
                mDataPasser!!.buttonsCallback("Weather")
            }
            R.id.btnBMI -> {
                mDataPasser!!.buttonsCallback("BMI")
            }
        }
    }
}