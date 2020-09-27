package com.example.smartagri

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btnmarketplace.setOnClickListener {
            var intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }

        btnreminders.setOnClickListener {
            var intent = Intent(this, ReminderActivity::class.java)
            startActivity(intent)
        }

        btnsuggestions.setOnClickListener {
            var intent = Intent(this, SuggestionsActivity::class.java)
            startActivity(intent)
        }

        btngovtprice.setOnClickListener {
            var intent = Intent(this, GovtPriceActivity::class.java)
            startActivity(intent)
        }

        btnnetwork.setOnClickListener {
            var intent = Intent(this, NetworkActivity::class.java)
            startActivity(intent)
        }

        btnlogout.setOnClickListener {
            var mainpref: Mainpref = Mainpref(this)
            mainpref.logout()
            finish()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
