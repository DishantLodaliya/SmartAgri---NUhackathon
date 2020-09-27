package com.example.smartagri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.com.example.smartagri.BidClass
import com.example.smartagri.com.example.smartagri.BidViewAdapter
import com.example.smartagri.com.example.smartagri.SuggestionClass
import com.example.smartagri.com.example.smartagri.SuggestionViewAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_crop_details.*
import kotlinx.android.synthetic.main.activity_suggestions.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SuggestionsActivity : AppCompatActivity() {

    lateinit var reminderAdapter : SuggestionViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_suggestions)

        var suggestionlist = ArrayList<SuggestionClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("suggestions")


        databaseReference?.orderByChild("suggetions_timestamp").addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    suggestionlist.clear()
                    if (p0.hasChildren()) {
                        var map = HashMap<String, String>()
                        p0.children.forEach {
                            map = it.value as HashMap<String, String>
                            Log.e("MAP DATA :", "$map")

                            var suggestionClass = SuggestionClass()
                            suggestionClass.question = map.get("suggestions_question").toString()
                            suggestionClass.username = map.get("suggestions_username").toString()
                            suggestionClass.key = map.get("suggestions_key").toString()
                            suggestionlist.add(suggestionClass)
                        }

                        Collections.reverse(suggestionlist);

                        val mLayoutManager = LinearLayoutManager(this@SuggestionsActivity, RecyclerView.VERTICAL, false)
                        suggestions_listview.layoutManager = mLayoutManager
                        reminderAdapter = SuggestionViewAdapter(this@SuggestionsActivity, suggestionlist)
                        suggestions_listview.adapter = reminderAdapter
                    }
                }})



        suggestions_getsuggestion.setOnClickListener {
            var intent = Intent(this, GetSuggetionActivity::class.java)
            startActivity(intent)
        }
    }

    fun itemclick(reminderModel: SuggestionClass) {
        var intent = Intent(this, SuggestionDetailsActivity::class.java)
        val args = Bundle()
        args.putSerializable("data", reminderModel)
        intent.putExtra("bundle", args)
        startActivity(intent)
    }
}
