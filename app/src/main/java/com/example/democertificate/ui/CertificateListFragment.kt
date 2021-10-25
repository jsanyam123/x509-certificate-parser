package com.example.democertificate.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.democertificate.certificate.HandleCertificates
import com.example.democertificate.adapter.CertificateAdapter
import com.example.democertificate.certificate.HandleKeychain
import com.example.democertificate.databinding.CertificateListFragmentBinding
import com.example.democertificate.models.CertificateList

class CertificateListFragment : Fragment() {

    private var _binding: CertificateListFragmentBinding? = null
    private val binding get() = _binding
    private val mAdapter: CertificateAdapter by lazy { CertificateAdapter() }
    private var certificateList : CertificateList? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CertificateListFragmentBinding.inflate(inflater, container, false)
        fetchData()
        binding?.certificateDetails?.let { setupRecyclerView(it) }
        certificateList?.let { context?.let { it1 -> mAdapter.setData(it, it1) } }

        binding?.button1?.setOnClickListener{
            val ans = context?.let { activity?.let { it1 -> HandleKeychain().storeCertificateIntoKeychain(it, it1) } }
            Log.i("ssss",ans.toString())
        }

        binding?.button2?.setOnClickListener{
            val ans2 = context?.let { activity?.let { it1 -> HandleKeychain().readCertificate(it, it1) } }
            Log.i("ssss",ans2.toString())
        }
        return binding?.root
    }

    private fun fetchData() {
        certificateList = context?.let { HandleCertificates().readCert(it) }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}