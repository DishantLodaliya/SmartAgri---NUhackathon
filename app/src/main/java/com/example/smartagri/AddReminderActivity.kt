package com.example.smartagri

import android.app.DatePickerDialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_crop.*
import kotlinx.android.synthetic.main.activity_add_reminder.*
import java.util.*
import kotlin.collections.HashMap

class AddReminderActivity : AppCompatActivity() {

    lateinit var mainpref : Mainpref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        mainpref = Mainpref(this)

        var c = Calendar.getInstance()
        var mcurrentTime = Calendar.getInstance()
        var year = mcurrentTime.get(Calendar.YEAR)
        var month = mcurrentTime.get(Calendar.MONTH)
        val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                reminderdate.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year))
            }
        }, year, month, day)


        reminderdate.setOnClickListener {
            datePicker.show()
        }


        remindersubmit.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Signing Up")
            progressDialog.setMessage("Please Wait")
            progressDialog.show()

            var error: Int = 0

            if (remindertitle.text.toString().length == 0) {
                remindertitle.setError("Field Can Not Be Empty")
                error = 1
            }

            if (reminderdescription.text.toString().length == 0) {
                reminderdescription.setError("Field Can Not Be Empty")
                error = 1
            }


            if (reminderdate.text.toString().length == 0) {
                reminderdate.setError("Field Can Not Be Empty")
                error = 1
            }


            if (error == 1)
                progressDialog.dismiss()

            if (error == 0) {
                var firebaseDatabase: FirebaseDatabase? = null
                var databaseReference: DatabaseReference? = null
                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase?.getReference("reminder")

                var key: String = databaseReference?.push()!!.key.toString()

                var map = HashMap<String, String>()

                map.put("reminder_key", key)
                map.put("reminder_account_key", mainpref.getkey().toString())
                map.put("reminder_title",remindertitle.text.toString())
                map.put("reminder_description",reminderdescription.text.toString())
                map.put("reminder_date",reminderdate.text.toString())
                databaseReference!!.child(mainpref.getkey().toString()).child(key).setValue(map)
                progressDialog.dismiss()
                finish()
            }
        }
        }
    }
