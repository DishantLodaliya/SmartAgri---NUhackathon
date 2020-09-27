package com.example.smartagri

import java.io.Serializable

data class CropClass(var name: String = "Name", var quantity: String = "Description", var price: String = "0",var date: String = "0/0/0000", var details : String = "Details Of Data", var key : String = "KEY",var account_key : String = "ACCKEY",var imagekey : String = "imakekey") : Serializable