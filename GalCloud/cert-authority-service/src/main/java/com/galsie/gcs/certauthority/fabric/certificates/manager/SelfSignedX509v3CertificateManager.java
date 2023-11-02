package com.galsie.gcs.certauthority.fabric.certificates.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.io.IOException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SelfSignedX509v3CertificateManager {

    KeyPair keyPair;
    SomeX509v3CertificateHolder x509CertificateHolder;


    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }

    public PublicKey getPublicKey(){
        return keyPair.getPublic();
    }

    public String encodeBase64() throws IOException {
        return this.getX509CertificateHolder().toBase64Encoded();
    }

    public X509CertificateHolder signCertificate(X509v3CertificateBuilder x509v3CertificateBuilder) throws OperatorCreationException {
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withECDSA").build(getPrivateKey()); // signing with the root's private key
       return x509v3CertificateBuilder.build(contentSigner);
    }


}
