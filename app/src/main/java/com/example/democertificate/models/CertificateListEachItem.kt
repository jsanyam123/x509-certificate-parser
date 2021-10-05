package com.example.democertificate.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class CertificateListEachItem(
    @SerializedName("Label")
    val label: String,
    @SerializedName("CertificateDetail")
    val certificateDetail: @RawValue CertificateDetailsEachItem

    ) : Parcelable