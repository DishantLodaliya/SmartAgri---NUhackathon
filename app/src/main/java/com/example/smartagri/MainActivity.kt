package com.example.smartagri

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    fun isOnline():Boolean {
        val conMgr = getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = conMgr.getActiveNetworkInfo()
        while (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    lateinit var mainpref: Mainpref

    override fun onCreate(savedInstanceState: Bundle?) {
        mainpref = Mainpref(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        isOnline()

        if (mainpref.getusername() != (null)) {
            var intent = Intent(this, DashboardActivity::class.java)
            var bundle = Bundle()
            bundle.putString("username", mainpref.getusername())
            intent.putExtras(bundle)
            startActivity(intent)
        }
        login_btnlogin.setOnClickListener {

            val progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setTitle("Logging In")
            progressDialog.setMessage("Please Wait")
            progressDialog.show()

            var error: Int = 0

            if (login_etxtemail.text.toString().length == 0) {
                login_etxtemail.setError("Please Enter Email Address")
                error = 1
            }

            if (login_etxtpassword.text.toString().length == 0) {
                login_etxtpassword.setError("Please Enter Password")
                error = 1
            }
            if (error == 1)
                progressDialog.dismiss()

            if (error == 0) {
                var firebaseDatabase: FirebaseDatabase? = null
                var databaseReference: DatabaseReference? = null

                firebaseDatabase = FirebaseDatabase.getInstance()
                databaseReference = firebaseDatabase?.getReference("account")


                databaseReference?.orderByChild("account_email").equalTo(login_etxtemail.text.toString()).addValueEventListener(object :
                    ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Email Invalid", Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.hasChildren()) {
                            var map = HashMap<String, String>()
                            p0.children.forEach {
                                map = it.value as HashMap<String, String>
                            }
                            if (map.get("account_password")!!.equals(login_etxtpassword.text.toString())) {
                                Toast.makeText(this@MainActivity, "Login SuccessFull", Toast.LENGTH_LONG).show()
                                login_etxtemail.setText("")
                                login_etxtpassword.setText("")



                                var intent = Intent(this@MainActivity, DashboardActivity::class.java)
                                var bundle = Bundle()
                                mainpref.setusername(map.get("account_name").toString())
                                mainpref.setusertype(map.get("account_type").toString())
                                mainpref.setemailid(map.get("account_email").toString())
                                mainpref.setkey(map.getValue("account_key").toString())

                                //TOKEN
                                var refreshedToken = FirebaseInstanceId.getInstance().getToken()
                                if (refreshedToken != null) {

                                    var firebaseDatabasefortoken: FirebaseDatabase? = null
                                    var tablereferencefortoken: DatabaseReference? = null
                                    var key: String = databaseReference?.push()!!.key.toString()
                                    var mapfortoken = HashMap<String,String>()

                                    firebaseDatabasefortoken = FirebaseDatabase.getInstance()
                                    tablereferencefortoken = firebaseDatabase?.getReference("tokenid/${mainpref.getkey()}/ots")

                                    mapfortoken.put("account_token",refreshedToken)
                                    mapfortoken.put("account_key",map.getValue("account_key"))

                                    tablereferencefortoken.child(map.getValue("account_key")).setValue(mapfortoken)
                                }

                                //



                                finish()
                                intent.putExtras(bundle)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@MainActivity, "Password Invalid", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Email Invalid", Toast.LENGTH_LONG).show()
                        }
                        progressDialog.dismiss()
                    }
                })
            }
        }


        login_btncreateaccount.setOnClickListener {
            var intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("Forgot Password")
        menu?.add("Exit")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.toString().equals("Forgot Password")) {
            var intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        if (item.toString().equals("Exit")) {
            AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    this.finish()
                })
                .setNegativeButton("No", null)
                .create()
                .show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                this.finish()
            })
            .setNegativeButton("No", null)
            .create()
            .show()
    }
}
