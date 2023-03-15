package com.example.cs4530lifestyleapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
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
import java.util.*




class DetailsFragment : Fragment(), View.OnClickListener {
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
    private var mCameraButton: Button? = null
    private var mStringFullName: String? = null
    private var spActivityLevel: Spinner? = null

    private var mDataPasser: DetailsPassing? = null
    private var DataArray: Array<String?> = Array(8, {null})

    //variable that holds filepath to profilePic
    private var filePathString: String? =null

    //imageView that holds pic
    private var mPicView: ImageView? = null

    //Callback interface
    interface DetailsPassing {
        fun detailsCallback(data: Array<String?>?)
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

        mEtFullName = view.findViewById(R.id.et_fullname) as EditText
        mWeight = view.findViewById(R.id.npWeight) as NumberPicker
        mSex = view.findViewById(R.id.rgSex) as RadioGroup
        rbSexMale = view.findViewById(R.id.rbSexMale) as RadioButton
        rbSexFemale = view.findViewById(R.id.rbSexFemale) as RadioButton
        mAge = view.findViewById(R.id.npAge) as NumberPicker
        mHeightFeet = view.findViewById(R.id.npHeightFeet) as NumberPicker
        mHeightInches = view.findViewById(R.id.npHeightInches) as NumberPicker
        mLocation = view.findViewById(R.id.etLocation) as EditText

        // Setup age number picker
        mAge!!.minValue = 0
        mAge!!.maxValue = 120
        mAge!!.wrapSelectorWheel = true
        mAge!!.setValue(30)

        //Setup height number pickers
        mHeightFeet!!.minValue = 2
        mHeightFeet!!.maxValue = 8
        mHeightFeet!!.wrapSelectorWheel = true
        mHeightInches!!.minValue = 0
        mHeightInches!!.maxValue = 11
        mHeightInches!!.wrapSelectorWheel = true
        mHeightFeet!!.setValue(5)
        mHeightInches!!.setValue(8)

        // Setup weight number picker
        mWeight!!.minValue = 0
        mWeight!!.maxValue = 600
        mWeight!!.wrapSelectorWheel = true
        mWeight!!.setValue(170)

        mBtSubmit = view.findViewById(R.id.button_submit) as Button
        spActivityLevel = view.findViewById(R.id.spActivityLevel) as Spinner
        mBtSubmit!!.setOnClickListener(this)

        mCameraButton = view.findViewById(R.id.photoButton)
        mCameraButton!!.setOnClickListener(this)

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
        if (age != null) {
            mAge!!.setValue(age.toInt())
        }
        rbSexMale!!.setChecked(true)
        if (sex == "Female") {
            rbSexFemale!!.setChecked(true)
        }
        if (weight != null) {
            mWeight!!.setValue(weight.toInt())
        }
        if (height != null) {
            mHeightFeet!!.setValue(Math.floorDiv(height.toInt(), 12))
            mHeightInches!!.setValue(height.toInt() % 12)
        }
        if (location != null) {
            mLocation!!.setText(location)
        }

        val spArray = arrayOf("Sedentary: little or no exercise", "Exercise 1-3 times/week", "Moderate Exercise 3-5 times/week", "Very Active 6-7 days/wk", "Extremely active (intense exercise/physical job)")
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
                    checkHeight()
                    checkLocation()
                    checkAge()
                    checkActivityLevel()
                    mDataPasser!!.detailsCallback(DataArray) //any way to make this a bundle? --need to pass filepath to mainActivity
                }
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
        val weightString: String? =  mWeight!!.getValue().toString()
        if (weightString != null) {
            DataArray[3] = weightString
        }
    }

    private fun checkAge() {
        val ageString: String? = mAge!!.getValue().toString()
        if (ageString != null) {
            DataArray[4] = ageString
        }
    }

    private fun checkActivityLevel() {
        val activityLevel = spActivityLevel!!.getSelectedItem().toString();
        DataArray[5] = activityLevel
    }

    private fun checkHeight() {
        val intHeight = (mHeightFeet!!.getValue() * 12) + mHeightInches!!.getValue()
        val height = intHeight.toString()
        if (height != null) {
            DataArray[6] = height
        }
    }

    private fun checkLocation() {
        val location = mLocation!!.text.toString()
        if (location != null) {
            DataArray[7] = location
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }
    private val displayCameraThumbnail = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            mPicView = view?.findViewById<View>(R.id.profilePic) as ImageView
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
                Log.i("filePath", filePathString!!)
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