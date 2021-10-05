package com.example.democertificate.certificate

class ExtendedKeyUsage {

    enum class ExtendedKeyUsageType(val value: String, var names: String) {
        TLS_WEB_SERVER_AUTH("1.3.6.1.5.5.7.3.1", "TLS Web Server Auth"),
        TLS_WEB_CLIENT_AUTH("1.3.6.1.5.5.7.3.2", "TLS Web Client Auth"),
        CODE_SIGNING("1.3.6.1.5.5.7.3.3", "Code Signing"),
        EMAIL_PROTECTION("1.3.6.1.5.5.7.3.4", "Email Protection"),
        TIME_STAMPING("1.3.6.1.5.5.7.3.8", "Time Stamping"),
        OCSP_SIGNING("1.3.6.1.5.5.7.3.9", "OSCP Signing"),
        IPSEC_END_SYSTEM("1.3.6.1.5.5.7.3.5", "IPSEC End System"),
        IPSEC_TUNNEL("1.3.6.1.5.5.7.3.6", "IPSEC Tunnel"),
        IPSPEC_UESR("1.3.6.1.5.5.7.3.7", "IPSPEC Uset"),
        MICROSOFT_SMART_LOGIN("1.3.6.1.4.1.311.20.2.2", "Microsoft Smart Login");
    }
}

fun getExtendedKeyUsageType(derEncodedString: String): ExtendedKeyUsage.ExtendedKeyUsageType? {
    var extendedKeyUsageType: ExtendedKeyUsage.ExtendedKeyUsageType? = null
    for (key in ExtendedKeyUsage.ExtendedKeyUsageType.values()) {
        if (derEncodedString == key.value) {
            extendedKeyUsageType = key
            break
        }
    }
    return extendedKeyUsageType
}