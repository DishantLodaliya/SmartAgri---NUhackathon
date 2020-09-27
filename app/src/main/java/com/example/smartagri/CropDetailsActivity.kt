package com.example.smartagri

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.com.example.smartagri.BidClass
import com.example.smartagri.com.example.smartagri.BidViewAdapter
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_crop_details.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class CropDetailsActivity : AppCompatActivity() {
    lateinit var mainpref: Mainpref
    lateinit var reminderAdapter: BidViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_details)

        mainpref = Mainpref(this)

        val intent = intent
        val args = intent.getBundleExtra("bundle")
        var data = args!!.getSerializable("data") as CropClass

        Log.e("MAP DATA : ", data.toString())

        cropdetails_crop.text = data.name
        cropdetails_date.text = data.date
        cropdetails_details.text = data.details
        cropdetails_price.text = data.price
        cropdetails_quantity.text = data.quantity
        var imagekey = data.imagekey


        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.getReferenceFromUrl("gs://smartagri-hackathon.appspot.com")
        val imagesRef = storageRef.child("images/${imagekey}")

        val localFile = File.createTempFile("images", "jpeg")
        imagesRef.getFile(localFile).addOnSuccessListener(object:OnSuccessListener<FileDownloadTask.TaskSnapshot> {
            override fun onSuccess(taskSnapshot: FileDownloadTask.TaskSnapshot) {
                val bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath())
                cropimage.setImageBitmap(bitmap)
                Log.e("FFFF","FFFFF")

            }
        }).addOnFailureListener(object: OnFailureListener {
            override fun onFailure(@NonNull exception:Exception) {
                Log.e("FFFF","$exception")
            }
        })








        var bidlist = ArrayList<BidClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("bid")


        databaseReference?.child(data.key).orderByChild("bid_price")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    bidlist.clear()
                    if (p0.hasChildren()) {
                        var map = HashMap<String, String>()
                        p0.children.forEach {
                            map = it.value as HashMap<String, String>
                            Log.e("MAP DATA :", "$map")

                            var bidclass = BidClass()
                            bidclass.name = map.get("bid_username").toString()
                            bidclass.price = map.get("bid_price").toString()
                            bidlist.add(bidclass)
                        }

                        Collections.reverse(bidlist);

                        val mLayoutManager = LinearLayoutManager(this@CropDetailsActivity, RecyclerView.VERTICAL, false)
                        cropdetails_bidlist.layoutManager = mLayoutManager
                        reminderAdapter = BidViewAdapter(this@CropDetailsActivity, bidlist)
                        cropdetails_bidlist.adapter = reminderAdapter
                    }
                }})


        cropdetails_makebid.setOnClickListener {
            if (crop_details_makeprice.text.length != 0) {
                var firebaseDatabase: FirebaseDatabase? = null
                var databaseReference: DatabaseReference? = null
                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase?.getReference("bid")

                var key: String = databaseReference?.push()!!.key.toString()
                var map = HashMap<String, String>()

                map.put("bid_key", key)
                map.put("bid_account_key", data.account_key)
                map.put("bid_username", mainpref.getusername().toString())
                map.put("bid_price",crop_details_makeprice.text.toString())

                var ckey = data.key
                databaseReference!!.child(ckey).child(key).setValue(map)
                finish()
            }
        }
    }


}
