package com.example.smartagri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.com.example.smartagri.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_suggestion_details.*
import kotlinx.android.synthetic.main.activity_suggestions.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    lateinit var mainpref : Mainpref
    lateinit var reminderAdapter : ChatViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mainpref = Mainpref(this)
        var chatlist = ArrayList<ChatClass>()


        val intent = intent
        val args = intent.getBundleExtra("bundle")
        var data = args!!.getSerializable("data") as AccountClass

        Log.e("CHAT DATA : ", data.toString())


        var suggestionlist = ArrayList<ChatClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("chat")

        databaseReference?.child(data.key).child(mainpref.getkey().toString()).orderByChild("chat_timestamp").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    var map = HashMap<String, String>()
                    p0.children.forEach {
                        map = it.value as HashMap<String, String>
                        Log.e("MAP DATA :", "$map")

                        var chatClass = ChatClass()
                        chatClass.message = map.get("chat_message").toString()
                        chatClass.username = map.get("chat_username").toString()
                        chatClass.key = map.get("chat_key").toString()
                        chatClass.timestamp = map.get("chat_timestamp").toString()
                        chatlist.add(chatClass)
                    }

                    val mLayoutManager = LinearLayoutManager(this@ChatActivity, RecyclerView.VERTICAL, false)
                    chat_listview.layoutManager = mLayoutManager
                    reminderAdapter = ChatViewAdapter(this@ChatActivity, chatlist)
                    chat_listview.adapter = reminderAdapter
                }
            }})




        databaseReference?.child(mainpref.getkey().toString()).child(data.key).orderByChild("chat_timestamp").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.hasChildren()) {
                    var map = HashMap<String, String>()
                    p0.children.forEach {
                        map = it.value as HashMap<String, String>
                        Log.e("MAP DATA :", "$map")

                        var chatClass = ChatClass()
                        chatClass.message = map.get("chat_message").toString()
                        chatClass.username = map.get("chat_username").toString()
                        chatClass.key = map.get("chat_key").toString()
                        chatClass.timestamp = map.get("chat_timestamp").toString()
                        chatlist.add(chatClass)
                    }

                    val mLayoutManager = LinearLayoutManager(this@ChatActivity, RecyclerView.VERTICAL, false)
                    chat_listview.layoutManager = mLayoutManager
                    reminderAdapter = ChatViewAdapter(this@ChatActivity, chatlist)
                    chat_listview.adapter = reminderAdapter
                }
            }})


        chat_send.setOnClickListener{
            if (chat_chat.text.length != 0) {
                var firebaseDatabase: FirebaseDatabase? = null
                var databaseReference: DatabaseReference? = null
                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase?.getReference("chat")

                var ckey = mainpref.getkey().toString()
                var key: String = databaseReference?.push()!!.key.toString()
                var map = HashMap<String, String>()

                map.put("chat_key", key)
                map.put("chat_message", chat_chat.text.toString())
                map.put("chat_username", mainpref.getusername().toString())

                val timeStamp: String =  java.lang.String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))
                map.put("chat_timestamp", timeStamp)
                databaseReference!!.child(ckey).child(data.key).child(key).setValue(map)
                chat_chat.text.clear()
            }
        }
    }
}
