package com.galsie.gcs.certauthority.fabric.certificates.certbuilder;


import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.CertificateDNBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.extension.CertificateExtensionsBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair.CertificateKeypairBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.manager.SelfSignedX509v3CertificateManager;
import com.galsie.gcs.certauthority.fabric.certificates.manager.SomeX509v3CertificateHolder;
import com.sun.istack.NotNull;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import reactor.util.annotation.Nullable;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Date;


/**
 * Common Certificate builder Implementation, implement parameters and methods for:
 * - Creating the Subject DN
 * - Setting the Extensions
 * - Setting the Serial Number
 * - Setting the Certificate Validity
 * - Building to {@link SelfSignedX509v3CertificateManager} as a self-signed certificate
 * - Building to {@link }
 *
 * @param <T> A type reference to the class that extends this class.
 *            - This aims at matching the return types of the methods to the inheriting class
 *
 */
public abstract class CertificateBuilderCommonImpl<T extends CertificateBuilder> implements CertificateBuilder {

    /**
     * The Subject Name field of an X.509 certificate holds a Distinguished name (DN.
     */
    protected final CertificateDNBuilder<T> subjectDNBuilder = new CertificateDNBuilder<T>((T) this);

    /**
     * The Extension field X.509 certificate holds a list of extensions
     */
    private CertificateExtensionsBuilder<T> extensionsBuilder = new CertificateExtensionsBuilder<T>((T) this);

    /**
     * The Keypair (private & public keys) of an X.509 certificate can be generated through:
     * - Different algorithms & algorithm parameters
     * - Different Security Providers
     */
    private CertificateKeypairBuilder<T> keypairBuilder = new CertificateKeypairBuilder<T>((T) this);

    /**
     * Corresponds to the 'serial-num' tag of the certificate
     * - Can use serial numbers up to 20 octets in length
     */
    @NotNull
    private byte[] serialNumber; // A 20 octet string

    /**
     * Corresponds to the 'not-before' tag of the certificate
     * - The certificate would not be valid before that time
     */
    @NotNull
    private Date validFrom;

    /**
     * Corresponds to the 'not-after' tag of the certificate
     * - The certificate would not be valid after that time
     * - If null, this corresponds to the X.509/RFC 5280 defined special time value 99991231235959Z which implies a no defined expiry
     */
    @Nullable
    private Date validTo;


    /**
     * The 'Subject' field of the certificate holds a Distinguished Name
     * - Through this method, you access the Subject Distinguished name Builder
     *
     * @return The Subject DN Builder
     */
    public CertificateDNBuilder<T> subjectDN() {
        return this.subjectDNBuilder;
    }

    /**
     * The 'Extensions' field of the certificate holds a set of extensions
     * - Through this method, you access the Extensions builder
     *
     * @return The Subject DN Builder
     */
    public CertificateExtensionsBuilder<T> extensions() {
        return this.extensionsBuilder;
    }

    /**
     * The Certificate has a private & public key, these can be generated trough different algorithms with different providers
     * - The keypair builder allows configuring this.
     * - Through this method, you access the Keypair builder
     *
     * @return The Subject DN Builder
     */
    public CertificateKeypairBuilder<T> keypair() {
        return this.keypairBuilder;
    }

    /**
     * Sets the 'Serial Number' of the Certificate
     * - Must be a maximum of 20 bytes
     *
     * @param serialNumber The serial number.
     *                     - If longer than 20 octets, it is trimmed down to 20 octets.
     *                     - If shorter, its extended by zeros until it reaches 20 octets.
     */
    public T setSerialNumber(byte[] serialNumber) {
        byte[] fixedSerialNum = new byte[20];
        for (int i = 0; i < fixedSerialNum.length; i++) {
            if (i < serialNumber.length) {
                fixedSerialNum[i] = serialNumber[i];
                continue;
            }
            fixedSerialNum[i] = 0b00;
        }
        this.serialNumber = fixedSerialNum;
        return (T) this;
    }

    /**
     * Sets the 'Serial Number' of the Certificate
     * - Must be an 20 byte string
     *
     * @param serialNumber The serial number.
     *                     - If longer than 20 octets, it is trimmed down to 20 octets.
     *                     - If shorter, its extended by zeros until it reaches 20 octets.
     */
    public T setSerialNumber(String serialNumber) {
        return (T) this.setSerialNumber(serialNumber.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Sets the 'Not Before' field of the certificate, this sets the date from which the certificate is valid from
     *
     * @param date The date from which the certificate is valid from
     */
    public T setValidFrom(@NotNull Date date) {
        this.validFrom = date;
        return (T) this;
    }

    /**
     * Sets the 'Not After' field of the certificate, this sets the date after which the certificate is no longer valid
     * - If null, this corresponds to the X.509/RFC 5280 defined special time value 99991231235959Z which implies a no defined expiry
     *
     * @param date The date from which the certificate is valid from
     */
    public T setValidTo(@Nullable Date date) {
        this.validTo = date;
        return (T) this;
    }



    /**
     * Generates a KeyPair using the ECDSA algorithm with the secp256r1 curve.
     * - This requirement is based on matters certificate requirements.
     *
     * @return The generated keyPair
     * @throws NoSuchAlgorithmException           if the ECDSA algorithm wasn't found for some reason
     * @throws NoSuchProviderException            If the Bouncy Castle provider wasn't found
     * @throws InvalidAlgorithmParameterException If the Elliptic curve specified 'secp256r1' wasn't found
     */
    protected KeyPair generateKeypair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1");
        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");
        g.initialize(ecSpec, new SecureRandom());
        return g.generateKeyPair();
    }


    /**
     * Builds as a Self Signed Certificate where:
     * - The subject & issuer hold the same Distinguished Name
     * - The Keypair is generated through {@link CertificateBuilderCommonImpl#generateKeypair()}
     * - Most importantly, the certificate is signed using the generated keypair
     * <p>
     * Usually, The Root Certificate Authority Certificate is Self-Signed
     * - Note: This itself does not make the certificate a Certificate Authority, to do so, the BasicConstraints extension must be used.
     *
     * @return {@link SelfSignedX509v3CertificateManager}
     * @throws NoSuchAlgorithmException           if the ECDSA algorithm wasn't found for some reason
     * @throws NoSuchProviderException            If the Bouncy Castle provider wasn't found
     * @throws InvalidAlgorithmParameterException If the Elliptic curve specified 'secp256r1' wasn't found
     * @throws CertIOException                    If one of the extensions failed to be added
     * @throws OperatorCreationException          If the certificate signing failed
     */
    public SelfSignedX509v3CertificateManager buildAsSelfSigned() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CertIOException, OperatorCreationException {
        KeyPair keyPair = this.generateKeypair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        X500Name subjectName = this.subjectDNBuilder.getDistinguishedNameBuilder().build();
        X500Name issuerName = subjectName; // Self signed
        // Build the certificate
        X509v3CertificateBuilder certBuilder = new JcaX509v3CertificateBuilder(
                issuerName,
                new BigInteger(1, this.serialNumber),
                this.validFrom,
                this.validTo,
                subjectName,
                publicKey
        );

        for (Extension extension : this.extensions().getExtensions()) {
            certBuilder.addExtension(extension);
        }
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withECDSA").setProvider("BC").build(privateKey);
        SomeX509v3CertificateHolder certificateHolder = new SomeX509v3CertificateHolder(certBuilder.build(signer));
        return new SelfSignedX509v3CertificateManager(keyPair, certificateHolder);
    }
}
