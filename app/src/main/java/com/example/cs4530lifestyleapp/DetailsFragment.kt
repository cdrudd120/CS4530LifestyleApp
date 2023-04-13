package com.example.cs4530lifestyleapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import android.graphics.BitmapFactory
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider


class DetailsFragment : Fragment(), View.OnClickListener {
    private lateinit var mDetailsViewModel: DetailsViewModel

    private var mWeight: NumberPicker? = null
    private var mHeightFeet: NumberPicker? = null
    private var mHeightInches: NumberPicker? = null
    private var mSex: RadioGroup? = null
    private var rbSexMale: RadioButton? = null
    private var rbSexFemale: RadioButton? = null
    private var mAge: NumberPicker? = null
    private var mLocation: EditText? = null

    private var mEtFullName: EditText? = null
    private var mBtSubmit: Button? = null
    private var mBtBack: Button? = null
    private var mCameraButton: Button? = null
    private var mStringFullName: String? = null
    private var spActivityLevel: Spinner? = null

    private var mDataPasser: DetailsPassing? = null
    private var DataArray: Array<String?> = Array(9, {null})

    //variable that holds filepath to profilePic
    private var filePathString: String? =null

    //imageView that holds pic
    private var mPicView: ImageView? = null

    //Callback interface
    interface DetailsPassing {
        fun detailsCallback()
    }

    //Associate the callback with this Fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDataPasser = try {
            context as DetailsPassing
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

        mDetailsViewModel = ViewModelProvider(requireActivity())[DetailsViewModel::class.java]
        mDetailsViewModel!!.data.observe(viewLifecycleOwner, dataObserver)

        mEtFullName = view.findViewById(R.id.et_fullname) as EditText
        mWeight = view.findViewById(R.id.npWeight) as NumberPicker
        mSex = view.findViewById(R.id.rgSex) as RadioGroup
        rbSexMale = view.findViewById(R.id.rbSexMale) as RadioButton
        rbSexFemale = view.findViewById(R.id.rbSexFemale) as RadioButton
        mAge = view.findViewById(R.id.npAge) as NumberPicker
        mHeightFeet = view.findViewById(R.id.npHeightFeet) as NumberPicker
        mHeightInches = view.findViewById(R.id.npHeightInches) as NumberPicker
        mLocation = view.findViewById(R.id.etLocation) as EditText
        mPicView = view.findViewById<View>(R.id.profilePic) as ImageView

        // Setup age number picker
        mAge!!.minValue = 0
        mAge!!.maxValue = 120
        mAge!!.wrapSelectorWheel = true
        mAge!!.value = 30

        //Setup height number pickers
        mHeightFeet!!.minValue = 2
        mHeightFeet!!.maxValue = 8
        mHeightFeet!!.wrapSelectorWheel = true
        mHeightFeet!!.value = 5
        mHeightInches!!.minValue = 0
        mHeightInches!!.maxValue = 11
        mHeightInches!!.wrapSelectorWheel = true
        mHeightInches!!.value = 8

        // Setup weight number picker
        mWeight!!.minValue = 0
        mWeight!!.maxValue = 600
        mWeight!!.wrapSelectorWheel = true
        mWeight!!.value = 170

        mBtSubmit = view.findViewById(R.id.button_submit) as Button
        mBtBack = view.findViewById(R.id.button_back) as Button
        spActivityLevel = view.findViewById(R.id.spActivityLevel) as Spinner
        mBtSubmit!!.setOnClickListener(this)
        mBtBack!!.setOnClickListener(this)

        mCameraButton = view.findViewById(R.id.photoButton)
        mCameraButton!!.setOnClickListener(this)

        val spArray = arrayOf("Sedentary: little or no exercise", "Exercise 1-3 times/week", "Moderate Exercise 3-5 times/week", "Very Active 6-7 days/wk", "Extremely active (intense exercise/physical job)")
        val adapter: ArrayAdapter<CharSequence> =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spActivityLevel!!.adapter = adapter

        return view
    }

    private val dataObserver: Observer<DetailsData> =
        Observer { detailsData -> // Update the UI if this data variable changes
            if (detailsData != null) {
                mEtFullName!!.setText(detailsData.firstName + " " + detailsData.lastName)
                if (detailsData.age != null) {
                    mAge!!.setValue(detailsData.age!!)
                }
                if (detailsData.weight != null) {
                    mWeight!!.setValue(detailsData.weight!!)
                }
                if (detailsData.heightFeet != null) {
                    mHeightFeet!!.setValue(detailsData.heightFeet!!)
                }
                if (detailsData.heightInches != null) {
                    mHeightInches!!.setValue(detailsData.heightInches!!)
                }
                mLocation!!.setText(detailsData.location)

                rbSexMale!!.setChecked(true)
                if (detailsData.sex == "Female") {
                    rbSexFemale!!.setChecked(true)
                }

                filePathString = detailsData.imageFilepath
                if (detailsData.imageFilepath != null) {
                    val profilePicture = BitmapFactory.decodeFile(detailsData.imageFilepath)
                    if (profilePicture != null) {
                        mPicView!!.setImageBitmap(profilePicture)
                    }
                }

                val spArray = arrayOf("Sedentary: little or no exercise", "Exercise 1-3 times/week", "Moderate Exercise 3-5 times/week", "Very Active 6-7 days/wk", "Extremely active (intense exercise/physical job)")
                val adapter: ArrayAdapter<CharSequence> =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spArray)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spActivityLevel!!.adapter = adapter
                if (detailsData.activityLevel != null) {
                    val spinnerPosition: Int = adapter.getPosition(detailsData.activityLevel)
                    spActivityLevel!!.setSelection(spinnerPosition)
                }
            }
        }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_submit -> {
                mStringFullName = mEtFullName!!.text.toString()

                //Check if the Name is empty, it is the only required input field
                if (mStringFullName.isNullOrBlank() || filePathString.isNullOrBlank()) {
                    //Complain that there's no text
                    Toast.makeText(activity, "Enter a name and photo first!", Toast.LENGTH_SHORT).show()
                    return
                }

                val splitStrings: Array<String?> = mStringFullName!!.split("\\s+".toRegex()).toTypedArray()
                if (splitStrings.size == 1) {
                    Toast.makeText(
                        activity,
                        "Enter both first and last name!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                } else if (splitStrings.size > 2) {
                    Toast.makeText(
                        activity,
                        "Enter only first and last name!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }

                var data: DetailsData = DetailsData()
                data.firstName = splitStrings[0]
                data.lastName = splitStrings[1]
                data.location = mLocation!!.text.toString()
                data.activityLevel = spActivityLevel!!.selectedItem.toString()
                data.imageFilepath = filePathString

                if (rbSexMale!!.isChecked) {
                    data.sex = "Male"
                }
                else if (rbSexFemale!!.isChecked) {
                    data.sex = "Female"
                }
                else {
                    data.sex = "NA"
                }

                data.age = mAge!!.value
                data.weight = mWeight!!.value
                data.heightFeet = mHeightFeet!!.value
                data.heightInches = mHeightInches!!.value

                mDetailsViewModel!!.setDetailsData(data)

                mDataPasser!!.detailsCallback()
            }
            R.id.button_back -> {
                mDataPasser!!.detailsCallback()
            }
            //get photo
            R.id.photoButton -> {
                //The button press should open a camera
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try{
                    displayCameraThumbnail.launch(cameraIntent)
                }catch(ex: ActivityNotFoundException){
                    //Do error handling here
                }
            }
        }
    }

    private val displayCameraThumbnail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            var thumbnailImage: Bitmap? = null
            if (Build.VERSION.SDK_INT >= 33) {
                thumbnailImage = result.data!!.getParcelableExtra("data", Bitmap::class.java)
                mPicView!!.setImageBitmap(thumbnailImage)
            }
            else{
                thumbnailImage = result.data!!.getParcelableExtra<Bitmap>("data")
                mPicView!!.setImageBitmap(thumbnailImage)
            }
            //Open a file and write to it
            if (isExternalStorageWritable) {
                filePathString = savePic(thumbnailImage)
            } else {
                Toast.makeText(activity, "External storage not writable.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val isExternalStorageWritable: Boolean
        get() {
            val state = Environment.getExternalStorageState()
            return Environment.MEDIA_MOUNTED == state
        }

    private fun savePic(finalBitmap: Bitmap?): String {
        val root = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Thumbnail_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file.absolutePath
    }
}