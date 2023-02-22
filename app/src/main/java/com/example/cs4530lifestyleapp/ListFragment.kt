package com.example.cs4530lifestyleapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ListFragment : Fragment(), View.OnClickListener {

    private var mDataPasser: ListDataPassingInterface? = null
    private var DataArray: Array<String?> = Array(1, {null})

    private var btnEditDetails: Button? = null
    private var btnDetails: Button? = null
    private var btnHikes: Button? = null
    private var btnWeather: Button? = null
    private var btnBMI: Button? = null

    interface ListDataPassingInterface {
        fun passDataList(data: Array<String?>?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = try {
            context as ListDataPassingInterface
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
                DataArray[0] = "Details"
                mDataPasser!!.passDataList(DataArray)
            }
            R.id.btnEditDetails -> {
                DataArray[0] = "Edit"
                mDataPasser!!.passDataList(DataArray)
            }
            R.id.btnHikes -> {
                DataArray[0] = "Hikes"
                mDataPasser!!.passDataList(DataArray)
            }
            R.id.btnWeather -> {
                DataArray[0] = "Weather"
                mDataPasser!!.passDataList(DataArray)
            }
            R.id.btnBMI -> {
                DataArray[0] = "BMI"
                mDataPasser!!.passDataList(DataArray)
            }
        }
    }
}