package com.example.democertificate.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

import com.example.democertificate.databinding.ActivityCertificateDetailsBinding

class CertificateDetailsActivity : Fragment() {

    private val args by navArgs<CertificateDetailsActivityArgs>()
    private lateinit var _binding: ActivityCertificateDetailsBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityCertificateDetailsBinding.inflate(inflater, container, false)
        val certifiacteDetails = args.result

        binding.SubjectData.text = certifiacteDetails.subject
        binding.issuerData.text = certifiacteDetails.issuer
        binding.expirationData.text = certifiacteDetails.expiry
        binding.keyUsageData.text = certifiacteDetails.keyUsage
        if(certifiacteDetails.extendedKeyUsage != "") {
            binding.ekuData.text = certifiacteDetails.extendedKeyUsage
            binding.ekuData.visibility = View.VISIBLE
            binding.extendedKeyUsage.visibility = View.VISIBLE
        }

        if(certifiacteDetails.san != "") {
            binding.sanData.text = certifiacteDetails.san
            binding.sanH.visibility = View.VISIBLE
            binding.sanData.visibility = View.VISIBLE
        }
        return binding.root
    }


}
