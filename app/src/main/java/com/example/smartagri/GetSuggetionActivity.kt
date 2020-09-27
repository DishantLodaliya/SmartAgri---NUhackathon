package com.example.smartagri

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_get_suggetion.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.concurrent.TimeUnit


class GetSuggetionActivity : AppCompatActivity() {

    lateinit var mainpref : Mainpref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_suggetion)

        mainpref = Mainpref(this)
        getsuggestion_btnsubmit.setOnClickListener {
            if(getsuggestion_question.text.length>0)
            {
                var firebaseDatabase: FirebaseDatabase? = null
                var databaseReference: DatabaseReference? = null
                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase?.getReference("suggestions")

                var key: String = databaseReference?.push()!!.key.toString()
                var map = HashMap<String, String>()

                map.put("suggestions_key", key)
                map.put("suggestions_question", getsuggestion_question.text.toString())
                map.put("suggestions_account_key", mainpref.getkey().toString())
                map.put("suggestions_username", mainpref.getusername().toString())

                val timeStamp: String =  java.lang.String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))
                map.put("suggestions_timestamp", timeStamp)

                databaseReference!!.child(key).setValue(map)
                finish()
            }
            else
            {
                registration_etxtmobile.setError("Please Write Your Question")
            }
        }
    }
}
