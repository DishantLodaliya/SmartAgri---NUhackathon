package com.example.smartagri

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class CropViewAdapter(private var leaderBoardActivity: Activity, private var newMap: ArrayList<CropClass>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recyclerViewCallback: RecyclerViewCallback? = null

    class CropViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cropname: TextView = view.findViewById(R.id.tvcropname)
        var cropquantity: TextView = view.findViewById(R.id.tvcropquantity)
        var cropprice: TextView = view.findViewById(R.id.tvcropprice)
        var cropdate: TextView = view.findViewById(R.id.tvcropdate)
        var cvleader: CardView = view.findViewById(R.id.cv_leader)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CropViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cropview, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val leaderViewHolder = holder as CropViewHolder
        var reminderModel = newMap[position]
        leaderViewHolder.cropname.text = reminderModel.name
        leaderViewHolder.cropquantity.text = reminderModel.quantity
        leaderViewHolder.cropprice.text = reminderModel.price
        leaderViewHolder.cropdate.text = reminderModel.date
        leaderViewHolder.cvleader.setOnClickListener {
            (leaderBoardActivity as MarketPlaceActivity).itemclick(reminderModel)
        }

        leaderViewHolder.cvleader.setOnLongClickListener { view ->
            this@CropViewAdapter.recyclerViewCallback!!.onRecycleViewLongPress(
                reminderModel,
                position,
                view
            )
            return@setOnLongClickListener true
        }

    }

    private fun itemlongclick(reminderModel: CropClass, it: View): Boolean {

        Log.e("LONGCLICK","DONE")
        return true

    }

    override fun getItemCount(): Int {
        Log.e(TAG, "getItemCount: " + newMap.size)
        return newMap.size
    }

    companion object {
        private const val TAG = "LeaderAdapter"
    }

    interface RecyclerViewCallback {
        fun onRecycleViewLongPress(contactData: CropClass, position: Int, view: View)
    }


    fun setOnCallbackListener(recyclerViewCallback: RecyclerViewCallback) {
        this.recyclerViewCallback = recyclerViewCallback
    }
}