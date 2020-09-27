package com.example.smartagri.com.example.smartagri

import android.app.Activity
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.MarketPlaceActivity
import com.example.smartagri.NetworkActivity
import com.example.smartagri.R

class AccountViewAdapter(private var leaderBoardActivity: Activity, private var newMap: ArrayList<AccountClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var personviewusername: TextView = view.findViewById(R.id.personview_username)
        var personViewLeader : CardView = view.findViewById(R.id.personViewLeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AccountViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.personview, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val leaderViewHolder = holder as AccountViewHolder
        var reminderModel = newMap[position]
        leaderViewHolder.personviewusername.text = reminderModel.username
        if(reminderModel.type=="Merchant")
            leaderViewHolder.personviewusername.setTextColor(Color.RED)
        leaderViewHolder.personViewLeader.setOnClickListener {
            (leaderBoardActivity as NetworkActivity).itemclick(reminderModel)
        }
    }

    override fun getItemCount(): Int {
        Log.e(TAG, "getItemCount: " + newMap.size)
        return newMap.size
    }

    companion object {
        private const val TAG = "LeaderAdapter"
    }
}