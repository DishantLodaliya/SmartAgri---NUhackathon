package com.example.smartagri

import android.content.Context
import java.nio.channels.spi.AbstractSelectionKey

class Mainpref(internal var context: Context)
{
    var sharedPreferences = context.getSharedPreferences("mainpref",Context.MODE_PRIVATE)

    var editor = sharedPreferences.edit()
    var key_username ="USERNAME"
    var key_emailid="EMAILID"
    var key_key="KEY"

    fun setemailid(emailid : String)
    {
        editor.putString(key_emailid, emailid)
        editor.commit()
    }
    fun setusername(username : String)
    {
        editor.putString(key_username,username)
        editor.commit()
    }

    fun setkey(key : String)
{
    editor.putString(key_key,key)
    editor.commit()
}

    fun getkey() : String?
    {
        return sharedPreferences.getString(key_key, null)
    }

    fun setusertype(key : String)
    {
        editor.putString("usertype",key)
        editor.commit()
    }

    fun getusertype() : String?
    {
        return sharedPreferences.getString("usertype", null)
    }

    fun getemailid() : String?
    {
        return sharedPreferences.getString(key_emailid, null)
    }

    fun getusername() : String?
    {
        return sharedPreferences.getString(key_username, null)
    }

    fun logout()
    {
        editor.clear()
        editor.commit()
    }
}