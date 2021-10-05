package com.example.democertificate.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CertificateDetailsEachItem(
    @SerializedName("Subjects")
    val subject: String,
    @SerializedName("Issuer")
    val issuer: String,
    @SerializedName("Expiry")
    val expiry: String,
    @SerializedName("KeyUsage")
    val keyUsage: String,
    @SerializedName("ExtendedKeyUSage")
    val extendedKeyUsage: String,
    @SerializedName("SAN")
    val san: String,
) : Parcelable
