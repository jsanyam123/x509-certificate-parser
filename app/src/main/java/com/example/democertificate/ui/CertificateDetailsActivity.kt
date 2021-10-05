package com.example.democertificate.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.democertificate.R
import com.example.democertificate.models.CertificateListEachItem


class CertificateDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate_details)

        val extras = intent.extras
        val certiDetails: CertificateListEachItem?

        if (extras != null) {
            certiDetails = extras.get("certiDetails") as CertificateListEachItem
            Log.i("Sanyam",certiDetails.toString())
        }


    }
}