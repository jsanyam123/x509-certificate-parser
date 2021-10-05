package com.example.democertificate.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democertificate.certificate.HandleCertificates
import com.example.democertificate.R
import com.example.democertificate.adapter.CertificateAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mAdapter: CertificateAdapter by lazy { CertificateAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var certificateList = HandleCertificates().readCert(this)
        setupRecyclerView(certificateDetails)
        mAdapter.setData(certificateList,this)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}