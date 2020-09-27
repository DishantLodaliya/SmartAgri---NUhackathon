package com.example.smartagri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.com.example.smartagri.AnswerAdapter
import com.example.smartagri.com.example.smartagri.AnswerClass
import com.example.smartagri.com.example.smartagri.SuggestionClass
import com.example.smartagri.com.example.smartagri.SuggestionViewAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_crop_details.*
import kotlinx.android.synthetic.main.activity_get_suggetion.*
import kotlinx.android.synthetic.main.activity_suggestion_details.*
import kotlinx.android.synthetic.main.activity_suggestions.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SuggestionDetailsActivity : AppCompatActivity() {

    lateinit var mainpref :Mainpref
    lateinit var reminderAdapter : AnswerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestion_details)

        mainpref = Mainpref(this)

        val intent = intent
        val args = intent.getBundleExtra("bundle")
        var data = args!!.getSerializable("data") as SuggestionClass
        var ckey = data.key


        Log.e("MAP DATA : ", data.toString())

        var suggestionlist = ArrayList<AnswerClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("answer")


        databaseReference?.child(ckey).orderByChild("answer_timestamp").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                suggestionlist.clear()
                if (p0.hasChildren()) {
                    var map = HashMap<String, String>()
                    p0.children.forEach {
                        map = it.value as HashMap<String, String>
                        Log.e("MAP DATA :", "$map")

                        var suggestionClass = AnswerClass()
                        suggestionClass.answer = map.get("answer_answer").toString()
                        suggestionClass.username = map.get("answer_username").toString()
                        suggestionClass.key = map.get("answer_key").toString()
                        suggestionlist.add(suggestionClass)
                    }

                    Collections.reverse(suggestionlist);

                    val mLayoutManager = LinearLayoutManager(this@SuggestionDetailsActivity, RecyclerView.VERTICAL, false)
                    suggestionsdetails_listview.layoutManager = mLayoutManager
                    reminderAdapter = AnswerAdapter(this@SuggestionDetailsActivity, suggestionlist)
                    suggestionsdetails_listview.adapter = reminderAdapter
                }
            }})






        suggestionsdetails_submitanswer.setOnClickListener {

                if (suggestiondetails_answer.text.length != 0) {
                    var firebaseDatabase: FirebaseDatabase? = null
                    var databaseReference: DatabaseReference? = null
                    firebaseDatabase = FirebaseDatabase.getInstance()
                    databaseReference = firebaseDatabase?.getReference("answer")

                    var key: String = databaseReference?.push()!!.key.toString()
                    var map = HashMap<String, String>()

                    map.put("answer_key", key)
                    map.put("answer_answer", suggestiondetails_answer.text.toString())
                    map.put("answer_account_key", mainpref.getkey().toString())
                    map.put("answer_username", mainpref.getusername().toString())

                    val timeStamp: String =  java.lang.String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))
                    map.put("answer_timestamp", timeStamp)
                    databaseReference!!.child(ckey).child(key).setValue(map)
                    suggestiondetails_answer.text.clear()
                }

        }
    }
}
