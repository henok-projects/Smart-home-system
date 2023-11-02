package com.galsie.gcs.certauthority.fabric.certificates.manager;

import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.cert.X509CertificateHolder;

import java.io.IOException;
import java.util.Base64;

/**
 * Holds an X509v3 Certificate
 * Extends X509CertificateHolder with methods to encode and decode from a base64 string
 *
 */
public class SomeX509v3CertificateHolder extends X509CertificateHolder {
    public SomeX509v3CertificateHolder(byte[] bytes) throws IOException {
        super(bytes);
    }

    public SomeX509v3CertificateHolder(Certificate certificate) {
        super(certificate);
    }

    public SomeX509v3CertificateHolder(X509CertificateHolder certificateHolder){
        super(certificateHolder.toASN1Structure());
    }

    public String toBase64Encoded() throws IOException {
        return Base64.getEncoder().encodeToString(this.getEncoded());
    }
    public static SomeX509v3CertificateHolder fromBase64Encoded(String base64) throws IOException {
        byte[] data = Base64.getDecoder().decode(base64);
        return new SomeX509v3CertificateHolder(data);
    }
}
