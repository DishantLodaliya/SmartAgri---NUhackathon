package com.example.smartagri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_market_place.*

class MyCropActivity : AppCompatActivity() {

    lateinit var mainpref: Mainpref
    lateinit var reminderAdapter: CropViewAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_crop)

        mainpref = Mainpref(this)
        var croplist = ArrayList<CropClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("crop")


        databaseReference?.orderByChild("crop_date")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    croplist.clear()
                    if (p0.hasChildren()) {
                        var map = HashMap<String, String>()
                        p0.children.forEach {
                            map = it.value as HashMap<String, String>
                            Log.e("MAP DATA :", "$map")


                            if(map.get("crop_name").toString() == mainpref.getkey()) {
                                var cropclass = CropClass()
                                cropclass.name = map.get("crop_name").toString()
                                cropclass.quantity = map.get("crop_quantity").toString()
                                cropclass.price = map.get("crop_price").toString()
                                cropclass.date = map.get("crop_date").toString()
                                cropclass.key = map.get("crop_key").toString()
                                cropclass.details = map.get("crop_details").toString()
                                cropclass.account_key = map.get("account_key").toString()
                                croplist.add(cropclass)
                            }
                        }
                        val mLayoutManager = LinearLayoutManager(this@MyCropActivity, RecyclerView.VERTICAL, false)
                        marketplace_listview.layoutManager = mLayoutManager
                        reminderAdapter = CropViewAdapter(this@MyCropActivity, croplist)
                        marketplace_listview.adapter = reminderAdapter
                    }
                }})
    }
    }

