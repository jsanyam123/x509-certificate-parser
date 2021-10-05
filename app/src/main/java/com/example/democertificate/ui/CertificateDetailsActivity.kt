package com.example.democertificate.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.democertificate.R
import com.example.democertificate.models.CertificateListEachItem
import kotlinx.android.synthetic.main.activity_certificate_details.*


class CertificateDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate_details)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val extras = intent.extras
        val certiDetails: CertificateListEachItem?
        if (extras != null) {
            certiDetails = extras.get("certiDetails") as CertificateListEachItem
            Log.i("Sanyam",certiDetails.toString())
            SubjectData.text = certiDetails.certificateDetail.subject
            issuerData.text = certiDetails.certificateDetail.issuer
            expirationData.text = certiDetails.certificateDetail.expiry
            keyUsageData.text = certiDetails.certificateDetail.keyUsage
            if(certiDetails.certificateDetail.extendedKeyUsage != "") {
                ekuData.text = certiDetails.certificateDetail.extendedKeyUsage
                ekuData.visibility = View.VISIBLE
                extendedKeyUsage.visibility = View.VISIBLE
            }

            if(certiDetails.certificateDetail.san != "") {
                sanData.text = certiDetails.certificateDetail.san
                sanH.visibility = View.VISIBLE
                sanData.visibility = View.VISIBLE
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}