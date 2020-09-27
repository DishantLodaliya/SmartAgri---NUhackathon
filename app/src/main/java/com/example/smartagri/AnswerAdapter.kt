package com.example.smartagri.com.example.smartagri

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartagri.MarketPlaceActivity
import com.example.smartagri.R

class AnswerAdapter(private var leaderBoardActivity: Activity, private var newMap: ArrayList<AnswerClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class AnswerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvsuggestionanswer: TextView = view.findViewById(R.id.tvsuggestionanswer)
        var tvsuggestionanswerusername: TextView = view.findViewById(R.id.tvsuggestionanswerusername)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AnswerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.answerview, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val leaderViewHolder = holder as AnswerViewHolder
        var reminderModel = newMap[position]
        leaderViewHolder.tvsuggestionanswer.text = reminderModel.answer
        leaderViewHolder.tvsuggestionanswerusername.text = reminderModel.username

//        leaderViewHolder.cvleader.setOnClickListener {
//            (leaderBoardActivity as MarketPlaceActivity).itemclick(reminderModel)
//        }

    }

    override fun getItemCount(): Int {
        Log.e(TAG, "getItemCount: " + newMap.size)
        return newMap.size
    }

    companion object {
        private const val TAG = "LeaderAdapter"
    }
}