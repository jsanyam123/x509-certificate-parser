package com.example.democertificate.ui

import android.os.Bundle

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.example.democertificate.R
import kotlinx.android.synthetic.main.activity_certificate_details.*

class CertificateDetailsActivity : AppCompatActivity() {
    private val args by navArgs<CertificateDetailsActivityArgs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate_details)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val certificateDetail = args.result
        SubjectData.text = certificateDetail.subject
        issuerData.text = certificateDetail.issuer
        expirationData.text = certificateDetail.expiry
        keyUsageData.text = certificateDetail.keyUsage
        if(certificateDetail.extendedKeyUsage != "") {
            ekuData.text = certificateDetail.extendedKeyUsage
            ekuData.visibility = View.VISIBLE
            extendedKeyUsage.visibility = View.VISIBLE
        }
        if(certificateDetail.san != "") {
            sanData.text = certificateDetail.san
            sanH.visibility = View.VISIBLE
            sanData.visibility = View.VISIBLE
        }

    }
    override fun onBackPressed() {

        super.onBackPressed()
        finish()
    }
}