package com.example.democertificate.certificate
import android.content.Context
import android.util.Base64.*
import android.util.Log
import com.example.democertificate.R
import com.example.democertificate.models.CertificateDetailsEachItem
import com.example.democertificate.models.CertificateList
import com.example.democertificate.models.CertificateListEachItem
import java.io.InputStream
import java.lang.reflect.Field
import java.nio.charset.Charset
import java.security.KeyStore
import java.security.NoSuchAlgorithmException
import java.security.PrivateKey
import java.security.PublicKey
import java.security.cert.CertificateParsingException
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException


class HandleCertificates {

    fun readCert(context: Context) : CertificateList{
        var certificateList = CertificateList()
        var caInput : InputStream
        var p12KeyStoreInstance : KeyStore
        val fields: Array<Field> = R.raw::class.java.fields
        fields.forEach {field ->
            p12KeyStoreInstance =  KeyStore.getInstance("pkcs12")
            caInput= context.resources.openRawResource(field.getInt(field))
            p12KeyStoreInstance.load(caInput, "Password1".toCharArray())
            val e: Enumeration<String> = p12KeyStoreInstance.aliases()
            while (e.hasMoreElements()) {
                val alias: String = e.nextElement()
                val c: X509Certificate = p12KeyStoreInstance.getCertificate(alias) as X509Certificate
                //Log.i("sssssCertificate",c.toString())
                var privateKey : PrivateKey
                if(p12KeyStoreInstance.isKeyEntry(alias))
                {
                    privateKey = p12KeyStoreInstance.getKey(alias,null) as PrivateKey
                    Log.i("ssssss",privateKey.algorithm)
                    Log.i("ssssss",privateKey.toString())
                    Log.i("ssssss",privateKey.encoded.size.toString())

                    val enc = encrypt("sanyam",c.publicKey)

                    val des = enc?.let { decrypt(it,privateKey) }
                    if (enc != null) {
                        Log.i("ssssss",enc)
                    }
                    if (des != null) {
                        Log.i("ssssss",des)
                    }

                }
                if(c.basicConstraints > -1){
                    continue
                }
                certificateList.add(CertificateListEachItem(alias,getCertificateDetails(c)))
            }
        }
        return certificateList
    }


    fun encrypt(string: String,mPublicKey:PublicKey): String? {
        var cipher: Cipher? = null
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }
        cipher!!.init(Cipher.ENCRYPT_MODE, mPublicKey)
        val stringBytes = string.toByteArray(charset("UTF-8"))
        val encryptedBytes: ByteArray = cipher.doFinal(stringBytes)
        return encodeToString(encryptedBytes,DEFAULT)
    }

    fun decrypt(string: String,mPrivateKey:PrivateKey): String? {
        var cipher: Cipher? = null
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }
        cipher!!.init(Cipher.DECRYPT_MODE, mPrivateKey)
        val stringBytes: ByteArray = decode(string.toByteArray(), DEFAULT)
        val decryptedBytes: ByteArray = cipher.doFinal(stringBytes)
        return String(decryptedBytes, Charset.forName("UTF-8"))
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


