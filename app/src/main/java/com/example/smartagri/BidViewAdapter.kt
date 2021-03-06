package com.example.smartagri.com.example.smartagri

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.CropDetailsActivity
import com.example.smartagri.MarketPlaceActivity
import com.example.smartagri.R

class BidViewAdapter(private var leaderBoardActivity: Activity, private var newMap: ArrayList<BidClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BidViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bidusername: TextView = view.findViewById(R.id.bidview_username)
        var bidprice: TextView = view.findViewById(R.id.bidview_price)
        var bidViewLeader: CardView = view.findViewById(R.id.bidViewLeader)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BidViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bidview, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val leaderViewHolder = holder as BidViewHolder
        var reminderModel = newMap[position]
        leaderViewHolder.bidprice.text = reminderModel.price
        leaderViewHolder.bidusername.text = reminderModel.name


    }

    override fun getItemCount(): Int {
        Log.e(TAG, "getItemCount: " + newMap.size)
        return newMap.size
    }

    companion object {
        private const val TAG = "LeaderAdapter"
    }
}