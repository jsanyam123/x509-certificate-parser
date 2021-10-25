package com.example.democertificate.certificate

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.security.KeyChain
import android.security.KeyChainAliasCallback
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.democertificate.R
import java.io.InputStream
import java.lang.reflect.Field
import java.security.Signature

class HandleKeychain : KeyChainAliasCallback{

    lateinit var currentContext: Context
    lateinit var currentActivity: Activity

    fun storeCertificateIntoKeychain(context: Context, activity: Activity) : String{
        var caInput : InputStream
        val fields: Array<Field> = R.raw::class.java.fields
        fields.forEach {field ->
            caInput= context.resources.openRawResource(field.getInt(field))
            val keychain = ByteArray(caInput.available())
            caInput.read(keychain)
            val installIntent = KeyChain.createInstallIntent()
            installIntent.putExtra(KeyChain.EXTRA_PKCS12, keychain)
            installIntent.putExtra(KeyChain.EXTRA_NAME, field.name)
            ActivityCompat.startActivityForResult(activity, installIntent, 1, null)
        }
        return "Successful"
    }

    override fun alias(alias: String?) {
        if (alias != null) {
            Log.i("ssssalias",alias)
            FetchFlag(currentContext,alias).execute()
        }
    }

    fun readCertificate(context: Context, activity: Activity) : String {
        currentContext = context
        currentActivity = activity
        KeyChain.choosePrivateKeyAlias(activity, this, null, null, null, -1, null)
        return "readCertificateSuccessful"
    }


    class FetchFlag(ctx: Context,alias:String) : AsyncTask<Any, Void, Boolean>()
    {
        var ctx = ctx
        var alias = alias
        override fun doInBackground(vararg params: Any): Boolean {
            val pk = KeyChain.getPrivateKey(ctx, alias)
            //Log.i("sssspkkkpk", "pkkey: $pk")
            val chain = KeyChain.getCertificateChain(ctx, alias)
            Log.i("ssss", "chain length: " + chain?.size)

            if (chain != null) {
                for (cert in chain) {
                    Log.i("ssssSubject", "Subject DN: " + cert.subjectDN.name)
                    Log.i("ssssIssueer", "Issuer DN: " + cert.issuerDN.name)
                }
            }
            val pubk = chain?.get(0)?.publicKey
            val data = "foobar".toByteArray(charset("ASCII"))
            val sig = Signature.getInstance("SHA1withRSA")
            sig.initSign(pk)
            sig.update(data)
            val signed = sig.sign()


            //Log.i("ssspublickey",pubk.toString())
            sig.initVerify(pubk)
            sig.update(data)
            val valid = sig.verify(signed)
            //Log.i("ssssvalid", "signature is valid: $valid")
            return valid
        }

        override fun onPostExecute(result: Boolean) {
            super.onPostExecute(result)
            if(result)
                Toast.makeText(ctx, "$alias is loaded successfully", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(ctx, "$alias failed ", Toast.LENGTH_SHORT).show()
        }
    }



}