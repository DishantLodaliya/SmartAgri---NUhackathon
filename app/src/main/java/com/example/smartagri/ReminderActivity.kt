package com.example.smartagri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.com.example.smartagri.ChatClass
import com.example.smartagri.com.example.smartagri.ChatViewAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_market_place.*
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    lateinit var mainPref : Mainpref
lateinit var reminderAdapter : ChatViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)

        mainPref = Mainpref(this)

        var croplist = ArrayList<ChatClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("reminder")


        databaseReference?.child(mainPref.getkey().toString()).orderByChild("reminder_date")
            .addValueEventListener(object : ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    croplist.clear()
                    if (p0.hasChildren()) {
                        var map = HashMap<String, String>()
                        p0.children.forEach {
                            map = it.value as HashMap<String, String>
                            Log.e("MAP DATA :", "$map")

                            var chatclass = ChatClass()

                            chatclass.message = (map.get("reminder_title").toString() + "\n" + map.get("reminder_description").toString())
                            chatclass.username = map.get("reminder_date").toString()
                            croplist.add(chatclass)
                        }
                        val mLayoutManager = LinearLayoutManager(
                            this@ReminderActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        reminder_listview.layoutManager = mLayoutManager
                        reminderAdapter = ChatViewAdapter(this@ReminderActivity, croplist)
                        reminder_listview.adapter = reminderAdapter
                    }
                }

            })





    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            menu.add("Add Reminder")
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.toString().equals("Add Reminder")) {
            var intent = Intent(this, AddReminderActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }
}
