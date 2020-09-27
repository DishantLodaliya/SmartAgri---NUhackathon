package com.example.smartagri

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_add_crop.*
import kotlinx.android.synthetic.main.activity_crop_details.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.HashMap

class AddCropActivity : AppCompatActivity() {

    lateinit var mainpref : Mainpref
    private lateinit var filePath: Uri
    var imagekey = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_crop)

        mainpref = Mainpref(this)

        var c = Calendar.getInstance()
        var mcurrentTime = Calendar.getInstance()
        var year = mcurrentTime.get(Calendar.YEAR)
        var month = mcurrentTime.get(Calendar.MONTH)
        val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                addcrop_date.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year))
            }
        }, year, month, day)


        addcrop_date.setOnClickListener {
            datePicker.show()
        }

        addcrop_btnphotofromcamera.setOnClickListener {
            Log.e("TAG","FROMCAMERA")
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }

        addcrop_btnphotofromgalary.setOnClickListener {
            Log.e("TAG","FROMGAARY")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_GALLERY)
        }


        addcrop_btnsubmit.setOnClickListener {

            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Signing Up")
            progressDialog.setMessage("Please Wait")
            progressDialog.show()

            var error: Int = 0

            if (addcrop_name.text.toString().length == 0) {
                addcrop_name.setError("Field Can Not Be Empty")
                error = 1
            }

            if (addcrop_details.text.toString().length == 0) {
                addcrop_details.setError("Field Can Not Be Empty")
                error = 1
            }

            if (addcrop_quantity.text.toString().length == 0) {
                addcrop_quantity.setError("Field Can Not Be Empty")
                error = 1
            }

            if (addcrop_initialprice.text.toString().length == 0) {
                addcrop_initialprice.setError("Mobile Number Must Be Of 10 Digit")
                error = 1
            }

            if (addcrop_date.text.toString().length == 0) {
                addcrop_date.setError("Field Can Not Be Empty")
                error = 1
            }

            if (image.visibility == View.GONE) {
                Toast.makeText(this,"Please Select Image",Toast.LENGTH_SHORT).show()

                error = 1
            }

            if (error == 1)
                progressDialog.dismiss()

            if (error == 0) {
                var firebaseDatabase: FirebaseDatabase? = null
                var databaseReference: DatabaseReference? = null
                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase?.getReference("crop")

                var key: String = databaseReference?.push()!!.key.toString()
                imagekey = UUID.randomUUID().toString()

                var map = HashMap<String, String>()

                map.put("crop_key", key)
                map.put("account_key", mainpref.getkey().toString())
                map.put("crop_name",addcrop_name.text.toString())
                map.put("crop_details",addcrop_details.text.toString())
                map.put("crop_quantity",addcrop_quantity.text.toString())
                map.put("crop_price",addcrop_initialprice.text.toString())
                map.put("crop_date",addcrop_date.text.toString())
                map.put("crop_imagekey",imagekey)
                databaseReference!!.child(key).setValue(map)
                uploadImage()
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            saveImage(data!!.extras!!.get("data") as Bitmap)
            image.setImageBitmap(data.extras!!.get("data") as Bitmap)
            image.visibility = View.VISIBLE

        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_GALLERY) {
            filePath = data!!.data!!
            image.setImageURI(data.data)
            image.visibility = View.VISIBLE

        }
    }

    private fun uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            // Defining the child of storageReference
            val ref: StorageReference = FirebaseStorage.getInstance().reference.child(
                "images/" + imagekey
            )


            ref.putFile(filePath).addOnSuccessListener(
                OnSuccessListener<Any?> {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
                    finish()

                })
                .addOnFailureListener(OnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                    finish()

                })
                .addOnProgressListener(object : OnProgressListener<UploadTask.TaskSnapshot?> {
                    // Progress Listener for loading
                    // percentage on the dialog box
                    override fun onProgress(taskSnapshot: UploadTask.TaskSnapshot?) {
                        val progress: Double = (100.0 * taskSnapshot!!.bytesTransferred / taskSnapshot!!.totalByteCount)
                        progressDialog.setMessage(
                            ("Uploaded " + progress.toInt() + "%")
                        )
                    }
                })

        }
    }
    private fun saveImage(bitmap: Bitmap) {
        val f3 = File(Environment.getExternalStorageDirectory().toString() + "/SmartAgri/")
        if (!f3.exists()) f3.mkdirs()
        var outStream: OutputStream? = null
        var file = File(
            Environment.getExternalStorageDirectory()
                .toString() + "/SmartAgri/" + "contact_image" + ".jpeg"
        )
        try {
            outStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outStream)
            outStream.close()
            Toast.makeText(applicationContext, "Saved", Toast.LENGTH_LONG).show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        filePath = Uri.fromFile(file)
    }
    companion object {
        private const val CAMERA_REQUEST = 10
        private const val REQUEST_CODE_GALLERY = 20
    }
}
