package com.example.smartagri.com.example.smartagri

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.SuggestionsActivity
import com.example.smartagri.R

class ChatViewAdapter(private var leaderBoardActivity: Activity, private var newMap: ArrayList<ChatClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var chatviewmessage: TextView = view.findViewById(R.id.chatviewmessage)
        var chatviewusername : TextView = view.findViewById(R.id.chatviewusername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.chatview, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val leaderViewHolder = holder as ChatViewHolder
        var reminderModel = newMap[position]
        leaderViewHolder.chatviewmessage.text = reminderModel.message
        leaderViewHolder.chatviewusername.text = reminderModel.username

    }

    override fun getItemCount(): Int {
        Log.e(TAG, "getItemCount: " + newMap.size)
        return newMap.size
    }

    companion object {
        private const val TAG = "LeaderAdapter"
    }
}