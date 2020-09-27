package com.example.smartagri

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_crop.*
import kotlinx.android.synthetic.main.activity_market_place.*

class MarketPlaceActivity : AppCompatActivity(),CropViewAdapter.RecyclerViewCallback {


    lateinit var reminderAdapter: CropViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market_place)

        var croplist = ArrayList<CropClass>()
        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("crop")


        databaseReference?.orderByChild("crop_date")
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

                            var cropclass = CropClass()
                            cropclass.name = map.get("crop_name").toString()
                            cropclass.quantity = map.get("crop_quantity").toString()
                            cropclass.price = map.get("crop_price").toString()
                            cropclass.date = map.get("crop_date").toString()
                            cropclass.key = map.get("crop_key").toString()
                            cropclass.details = map.get("crop_details").toString()
                            cropclass.account_key = map.get("account_key").toString()
                            cropclass.imagekey = map.get("crop_imagekey").toString()
                            croplist.add(cropclass)
                        }
                        val mLayoutManager = LinearLayoutManager(
                            this@MarketPlaceActivity,
                            RecyclerView.VERTICAL,
                            false
                        )
                        marketplace_listview.layoutManager = mLayoutManager
                        reminderAdapter = CropViewAdapter(this@MarketPlaceActivity, croplist)
                        marketplace_listview.adapter = reminderAdapter

                        reminderAdapter.setOnCallbackListener(this@MarketPlaceActivity)
                    }
                }

            })
    }

    fun itemclick(reminderModel: CropClass) {
        var intent = Intent(this, CropDetailsActivity::class.java)
        val args = Bundle()
        args.putSerializable("data", reminderModel)
        intent.putExtra("bundle", args)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("Add Crop")
        //menu?.add("My Crop")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.toString().equals("Add Crop")) {
            var intent = Intent(this, AddCropActivity::class.java)
            startActivity(intent)
        }
        if (item.toString().equals("My Crop")) {
            var intent = Intent(this, MyCropActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRecycleViewLongPress(contactData: CropClass, position: Int, view: View) {

            val wrapper: Context = ContextThemeWrapper(this@MarketPlaceActivity, R.style.PopupMenu)
            val popup = PopupMenu(wrapper, view)
            popup.menuInflater.inflate(R.menu.cropviewmenu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete -> {
                        removedata(contactData)
                    }

                    R.id.action_done -> {
                        donedata(contactData)
                    }

                }
                true
            }
            popup.show()
    }

     fun donedata(cropdata: CropClass) {
        var firebaseDatabasenew: FirebaseDatabase? = null
        var databaseReferencenew: DatabaseReference? = null

        firebaseDatabasenew = FirebaseDatabase.getInstance()
        databaseReferencenew = firebaseDatabasenew?.getReference("crop_history")

        var map = HashMap<String, String>()

        map.put("crop_key", cropdata.key)
        map.put("account_key", cropdata.account_key)
        map.put("crop_name",cropdata.name)
        map.put("crop_details",cropdata.details)
        map.put("crop_quantity",cropdata.quantity)
        map.put("crop_price",cropdata.price)
        map.put("crop_date",cropdata.date)
        map.put("crop_imagekey",cropdata.imagekey)
        databaseReferencenew!!.child(cropdata.key).setValue(map)



        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("crop")
        if(databaseReference?.child(cropdata.key)!!.removeValue().isComplete)
            Toast.makeText(this,"Crop Deleted", Toast.LENGTH_LONG).show()
    }

    fun removedata(cropdata: CropClass) {

        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("crop")
        if(databaseReference?.child(cropdata.key)!!.removeValue().isComplete)
            Toast.makeText(this,"Crop Deleted", Toast.LENGTH_LONG).show()
    }
}
