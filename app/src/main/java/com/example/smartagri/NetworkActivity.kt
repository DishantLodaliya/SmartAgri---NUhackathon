package com.example.smartagri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.com.example.smartagri.AccountClass
import com.example.smartagri.com.example.smartagri.AccountViewAdapter
import com.example.smartagri.com.example.smartagri.BidClass
import com.example.smartagri.com.example.smartagri.BidViewAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_crop_details.*
import kotlinx.android.synthetic.main.activity_network.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class NetworkActivity : AppCompatActivity() {

    lateinit var reminderAdapter : AccountViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)




        var accountlist = ArrayList<AccountClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("account")


        databaseReference?.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    accountlist.clear()
                    if (p0.hasChildren()) {
                        var map = HashMap<String, String>()
                        p0.children.forEach {
                            map = it.value as HashMap<String, String>
                            Log.e("MAP DATA :", "$map")

                            var accountClass = AccountClass()
                            accountClass.username = map.get("account_name").toString()
                            accountClass.key = map.get("account_key").toString()
                            accountClass.type = map.get("account_type").toString()
                            accountlist.add(accountClass)
                        }

                        val mLayoutManager = LinearLayoutManager(this@NetworkActivity, RecyclerView.VERTICAL, false)
                        networkactivity_listview.layoutManager = mLayoutManager
                        reminderAdapter = AccountViewAdapter(this@NetworkActivity, accountlist)
                        networkactivity_listview.adapter = reminderAdapter
                    }
                }})
    }

    fun itemclick(reminderModel: AccountClass) {
        var intent = Intent(this, ChatActivity::class.java)
        val args = Bundle()
        args.putSerializable("data", reminderModel)
        intent.putExtra("bundle", args)
        startActivity(intent)
    }
}
