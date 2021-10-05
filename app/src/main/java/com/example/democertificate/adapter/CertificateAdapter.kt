package com.example.democertificate.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.democertificate.ui.CertificateDetailsActivity
import com.example.democertificate.databinding.CertificateRowBinding
import com.example.democertificate.models.CertificateList
import com.example.democertificate.models.CertificateListEachItem


class CertificateAdapter : RecyclerView.Adapter<CertificateAdapter.MyViewHolder>() {
    private var states = CertificateList()
    lateinit var context: Context


    class MyViewHolder(private val binding: CertificateRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentCerti: CertificateListEachItem,context: Context){
            binding.Label.text = currentCerti.label
            binding.expiry.text = currentCerti.certificateDetail.expiry
            binding.executePendingBindings()
            binding.certificateRowLayout.setOnClickListener {
                val intent = Intent(context, CertificateDetailsActivity::class.java)
                intent.putExtra("certiDetails", currentCerti)
                context.startActivity(intent)
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CertificateRowBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCertificate = states[position]
        holder.bind(currentCertificate,context)



    }

    override fun getItemCount(): Int {
        return states.size
    }

    fun setData(newData: CertificateList,contex: Context){
        states = newData
        context = contex
    }

}