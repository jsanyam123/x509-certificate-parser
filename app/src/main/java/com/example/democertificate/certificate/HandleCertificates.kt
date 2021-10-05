package com.example.democertificate.certificate
import android.content.Context
import android.util.Log
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import java.security.cert.CertificateParsingException
import com.example.democertificate.R
import com.example.democertificate.models.CertificateDetailsEachItem
import com.example.democertificate.models.CertificateList
import com.example.democertificate.models.CertificateListEachItem
import java.lang.reflect.Field


class HandleCertificates {

    fun readCert(context: Context) : CertificateList{
        var certificateList = CertificateList()
        var caInput : InputStream
        var p12KeyStoreInstance : KeyStore
        val fields: Array<Field> = R.raw::class.java.fields
        fields.forEach {field ->
//            Log.d("Sanchin", String.format(
//                "name=\"%s\", id=0x%08x",
//                field.name, field.getInt(field))
//            )
            p12KeyStoreInstance =  KeyStore.getInstance("pkcs12")
            caInput= context.resources.openRawResource(field.getInt(field))
            p12KeyStoreInstance.load(caInput, "Password1".toCharArray())
//            Log.i("Sanchinhello","Hello")
            val e: Enumeration<String> = p12KeyStoreInstance.aliases()
            while (e.hasMoreElements()) {
                val alias: String = e.nextElement()
//                Log.i("San_alias",alias)
                val c: X509Certificate = p12KeyStoreInstance.getCertificate(alias) as X509Certificate
//                Log.i("Sancccp",c.basicConstraints. toString())
//                Log.i("Sancccp",getCertificateDetails(c).toString())
                certificateList.add(CertificateListEachItem(alias,getCertificateDetails(c)))
            }
        }
        return certificateList
    }

        fun readCertTwo(context: Context) {
        val caInput: InputStream = context.resources.openRawResource(R.raw.pivd3_rootca_)
        val p12 = KeyStore.getInstance("pkcs12")
        p12.load(caInput,"Password1".toCharArray())

        val e: Enumeration<String> = p12.aliases()
        while (e.hasMoreElements()) {
            val alias: String = e.nextElement()
            Log.i("San_alias",alias)
            val c: X509Certificate = p12.getCertificate(alias) as X509Certificate
            Log.i("Sancccphpp",getCertificateDetails(c).toString())
        }
    }


    private fun formatExpiryDate(date: Date): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        return formatter.format(date)
    }

    private fun getCertificateDetails(c: X509Certificate): CertificateDetailsEachItem {
        var issuer = ""
        var subject = ""
        var date = ""
        var eku = ""
        var ku = ""
        var san = ""
        try {
            issuer = c.issuerDN.toString()
            subject = c.subjectDN.toString()
            date = formatExpiryDate(c.notAfter)
            eku = getExtendedKeyUsage(c.extendedKeyUsage)
            ku = getKeyUsage(c.keyUsage)
            if(c.subjectAlternativeNames != null) {
                san = getSAN(c.subjectAlternativeNames)
            }
        } catch (e: CertificateParsingException) {
            Log.i("Error", "Error Parsing the certificate")
        }
        return CertificateDetailsEachItem(subject,issuer,date,ku,eku,san)
    }

    private fun getSAN( subjectAltNames: MutableCollection<MutableList<*>>): String {
        val type = 1
        var result = ""
        for (subjectAltName in subjectAltNames) {
            val entry = subjectAltName as List<*>
            if (entry.size < 2) {
                continue
            }
            val altNameType = entry[0] as Int? ?: continue
            if (altNameType == type) {
                result = entry[1] as String
                break
            }
        }
        return result
    }


    private fun getKeyUsage(keyUsageList: BooleanArray): String {
        var resultKeyUsage = ""
        for (i in 0..8) {
            if (keyUsageList[i]) {
                val type: KeyUsage.KeyUsageType = getKeyUsage(i)
                resultKeyUsage += type.keyName + ", "
            }
        }
        if (resultKeyUsage.length > 2) {
            resultKeyUsage = resultKeyUsage.substring(0, resultKeyUsage.length - 2)
        }
        return resultKeyUsage
    }

    private fun getExtendedKeyUsage(extendedUsage: MutableList<String>?): String {
        val extendedKeyUsagesList: MutableList<ExtendedKeyUsage.ExtendedKeyUsageType?> = ArrayList()
        var resultString = ""
        try {
            if (extendedUsage != null) {
                for (type in extendedUsage) {
                    val key: ExtendedKeyUsage.ExtendedKeyUsageType? = getExtendedKeyUsageType(type)
                    if (key != null) {
                        extendedKeyUsagesList.add(key)
                        resultString += key.names + ", "
                    } else {
                        resultString += "$type, "
                    }
                }
                if (resultString.length > 2) {
                    resultString = resultString.substring(0, resultString.length - 2)
                }
            }
        } catch (e: CertificateParsingException) {
            Log.i("Error", "Error Parsing the certificate")
        }
        return resultString
    }
}


