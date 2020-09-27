package com.example.smartagri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.com.example.smartagri.BidClass
import com.example.smartagri.com.example.smartagri.BidViewAdapter
import kotlinx.android.synthetic.main.activity_crop_details.*
import kotlinx.android.synthetic.main.activity_govt_price.*

class GovtPriceActivity : AppCompatActivity() {

    lateinit var reminderAdapter : BidViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_govt_price)

        var pricelist = ArrayList<BidClass>()

        var bidclass = BidClass()
        bidclass.name = "WHEAT"
        bidclass.price = "2500"
        pricelist.add(bidclass)

        bidclass = BidClass()
        bidclass.name = "COCONUT"
        bidclass.price = "4500"
        pricelist.add(bidclass)

        bidclass = BidClass()
        bidclass.name = "POTATO"
        bidclass.price = "1800"
        pricelist.add(bidclass)

        bidclass = BidClass()
        bidclass.name = "ONION"
        bidclass.price = "4400"
        pricelist.add(bidclass)

        bidclass = BidClass()
        bidclass.name = "GINGER"
        bidclass.price = "9800"
        pricelist.add(bidclass)

        bidclass = BidClass()
        bidclass.name = "GARLIC"
        bidclass.price = "14000"
        pricelist.add(bidclass)

        bidclass = BidClass()
        bidclass.name = "TAMATO"
        bidclass.price = "6500"
        pricelist.add(bidclass)

        Log.e("LIST",pricelist.toString())

        val mLayoutManager = LinearLayoutManager(this@GovtPriceActivity, RecyclerView.VERTICAL, false)
        govtprice_listview.layoutManager = mLayoutManager
        reminderAdapter = BidViewAdapter(this@GovtPriceActivity, pricelist)
        govtprice_listview.adapter = reminderAdapter

    }
}
