package com.example.democertificate.certificate

class KeyUsage {

    enum class KeyUsageType(val keyName: String) {
        DIGITAL_SIGNATURE("Digital Signature"),
        NON_REPUDIATION("Non Repudiation"),
        KEY_ENCIPHERMENT("Key Encipherment"),
        DATA_ENCIPHERMENT("Data Encipherment"),
        KEY_AGREEMENT("Key Agreement"),
        KEY_CERT_SIGN("Key Certifcate Sign"),
        CRL_SIGN("CRL Sign"),
        ENCIPHER_ONLY("Encipher Only"),
        DECIPHER_ONLY("Decipher Only")
    }
}
fun getKeyUsage(type: Int): KeyUsage.KeyUsageType {
    return KeyUsage.KeyUsageType.values()[type]
}