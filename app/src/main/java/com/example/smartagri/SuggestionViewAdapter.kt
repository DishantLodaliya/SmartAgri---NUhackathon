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

class SuggestionViewAdapter(private var leaderBoardActivity: Activity, private var newMap: ArrayList<SuggestionClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class SuggestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var suggestionquestion: TextView = view.findViewById(R.id.tvsuggestionquestion)
        var suggestionquestionusername : TextView = view.findViewById(R.id.tvsuggestionquestionusername)
        var suggestionleader : CardView = view.findViewById(R.id.suggestionleader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SuggestionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.questionview, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val leaderViewHolder = holder as SuggestionViewAdapter.SuggestionViewHolder
        var reminderModel = newMap[position]
        leaderViewHolder.suggestionquestion.text = reminderModel.question
        leaderViewHolder.suggestionquestionusername.text = reminderModel.username
        leaderViewHolder.suggestionleader.setOnClickListener {
            (leaderBoardActivity as SuggestionsActivity).itemclick(reminderModel)
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