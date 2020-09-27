package com.example.smartagri

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.regex.Pattern

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var firebaseDatabase: FirebaseDatabase? = null
        var databaseReference: DatabaseReference? = null
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("account")

        var PASSWORD_PATTERN: Pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,24}\$")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        registration_btnsubmit.setOnClickListener {


            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Signing Up")
            progressDialog.setMessage("Please Wait")
            progressDialog.show()

            var error: Int = 0
            var registered = 0


            if (registration_etxtname.text.toString().length == 0) {
                registration_etxtname.setError("Field Can Not Be Empty")
                error = 1
            }

            if (registration_etxtname.text.toString().length == 0) {
                registration_etxtname.setError("Field Can Not Be Empty")
                error = 1
            }

            if (registration_etxtaddress.text.toString().length == 0) {
                registration_etxtaddress.setError("Field Can Not Be Empty")
                error = 1
            }

            if (registration_etxtmobile.text.toString().length != 10) {
                registration_etxtmobile.setError("Mobile Number Must Be Of 10 Digit")
                error = 1
            }

            if (registration_etxtemail.text.toString().length == 0) {
                registration_etxtemail.setError("Field Can Not Be Empty")
                error = 1
            }

            if (!registration_etxtemail.text.toString().matches(Patterns.EMAIL_ADDRESS.toRegex())) {
                registration_etxtemail.setError("Email Is Not Valid")
                error = 1
            }
            if (!registration_etxtpassword.text.toString().matches(PASSWORD_PATTERN.toRegex())) {
                registration_etxtpassword.setError("Password Must Follow Criteria")
                Toast.makeText(this@RegistrationActivity, "Password Must Conatin (8-24) Character With Including One Lowercase, One Uppercase And One Digit", Toast.LENGTH_LONG).show()
                error = 1
            }

            if (error == 1)
                progressDialog.dismiss()

            if (error == 0) {
                if (registration_etxtpassword.text.toString().equals(registration_etxtcpassword.text.toString())) {
                    databaseReference = firebaseDatabase?.getReference("account")
                    databaseReference!!.orderByChild("account_email").equalTo(registration_etxtemail.text.toString()).addValueEventListener(object :
                        ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            var child = p0.children
                            var map = HashMap<String, String>()

                            child.forEach {
                                map = it.value as HashMap<String, String>
                            }

                            if (map.toString().length.equals(2) && registered == 0) {
                                var key: String = databaseReference?.push()!!.key.toString()
                                var map = HashMap<String, String>()

                                map.put("account_key", key)
                                map.put("account_name", registration_etxtname.text.toString())
                                map.put("account_mobile", registration_etxtmobile.text.toString())
                                map.put("account_email", registration_etxtemail.text.toString())
                                map.put("account_address", registration_etxtaddress.text.toString())
                                map.put("account_password", registration_etxtpassword.text.toString())

                                var type = "Farmer"
                                if(registration_rgtype_merchant.isChecked)
                                    type= "Merchant"

                                map.put("account_type", type)
                                databaseReference!!.child(key).setValue(map)
                                registered = 1


                                Toast.makeText(this@RegistrationActivity, "Registration Successfull", Toast.LENGTH_SHORT).show()
                                progressDialog.dismiss()
                                finish()
                            }
                            if (map.toString().length > 2 && registered == 0) {
                                Toast.makeText(this@RegistrationActivity, "Email Exist", Toast.LENGTH_LONG).show()
                                progressDialog.dismiss()
                            }
                        }
                    })
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(this@RegistrationActivity, "Please Confirm Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}