package com.galsie.gcs.certauthority.fabric;

import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair.KeypairBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair.algo.ECDSA;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;

@Component
public class HomeFabricKeyGenerator {

    @Value("${galsie.home.fabric.ellipticCurveAlgo}")
    private String fabricKeypairEllipticCurveAlgorithm;

    public HomeFabricKeyGenerator(){
        Security.addProvider(new BouncyCastleProvider());
    }
    private static SecureRandom SECURE_RANDOM = new SecureRandom();
    /**
     * Generates a new Identity Protection key.
     * - From the protocol: This is a universal group key shared across the fabric.
     *
     * @return A byte array of the generated key
     * @throws NoSuchAlgorithmException

    public byte[] generateNewIPK() {
        byte[] bytes = new byte[HomeFabricInfoEntity.IPK_LENGTH/8];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }*/

    /**
     * Generates a new private/public keypair in accordance with the matter protocol
     * - Uses the Elliptic Curve Algorithm with the curve defined in application.yml, in galsie.home.fabric.ellipticCurveAlgo
     * @return
     * @throws NoSuchAlgorithmException
     */
    public KeyPair generateNewFabricKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        ECNamedCurveParameterSpec exParameterSpec = ECNamedCurveTable.getParameterSpec(fabricKeypairEllipticCurveAlgorithm);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDSA", "BC"); // Elliptic curve algorithm, bouncy castyle provider
        keyPairGenerator.initialize(exParameterSpec);
        return keyPairGenerator.generateKeyPair();
    }

    public PKCS10CertificationRequest generateCertificateSigningRequest () throws Exception {

        KeyPair keyPair = KeypairBuilder.builder().setGenerationAlgorithm(ECDSA.SECP_256_R1).build();

        X500Name subject = new X500Name("CN=Test, O=MyOrganization, L=MyCity, ST=MyState, C=MyCountry");
        JcaPKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(subject, keyPair.getPublic());
        JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder("SHA256withECDSA");
        PKCS10CertificationRequest csr = p10Builder.build(csBuilder.build(keyPair.getPrivate()));
        return csr;
    }


}
