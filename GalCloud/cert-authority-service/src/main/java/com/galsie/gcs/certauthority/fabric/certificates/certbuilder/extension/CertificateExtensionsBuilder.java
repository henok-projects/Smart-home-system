package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.extension;

import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.CertificateBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.CertificateBuilderCommonImpl;
import com.galsie.gcs.serviceutilslibrary.utils.builder.InternalBuilder;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.Extensions;
import org.bouncycastle.asn1.x509.ExtensionsGenerator;
import org.bouncycastle.asn1.x509.KeyUsage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CertificateExtensionsBuilder is a helper used as by subclass builders of {@link CertificateBuilder}
 * - It holds a {@link com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.DistinguishedNameBuilder} to build the distinguished Name
 * - It holds a {@link CertificateBuilderCommonImpl} to go back to the builder which holds this Distinguished Name
 * @param <T> A builder subclass of {@link CertificateBuilder}
 *           - Used to return to the builder (through calling the {@link com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.CertificateDNBuilder#done()} method
 */
public class CertificateExtensionsBuilder<T extends CertificateBuilder> extends InternalBuilder<T> {

    /**
     * Critical Extensions must be processed when present. They need not be present though
     * - The Matter protocol within the context of the X.509 v3 Certificate defines these extensions as critical
     *
     * Note: These are not used by the builder, they are here for reference only
     */
    public static List<ASN1ObjectIdentifier> CRITICAL_EXTENSIONS = Arrays.asList(Extension.basicConstraints, Extension.keyUsage, Extension.extendedKeyUsage);

    /**
     * The {@link CertificateBuilder} Which this extensions builder belong to
     */

    /**
     * A structure used by bouncy castle to build extensions, it was re-used here for convenience
     */
    private ExtensionsGenerator extensionsGenerator = new ExtensionsGenerator();

    /**
     * Initializes the Certificate Extensions Builder
     * - The {@link CertificateBuilder} is expected for convenient chaining operations
     * @param certificateBuilder The {@link CertificateBuilder} holding this {@link CertificateExtensionsBuilder}
     */
    public CertificateExtensionsBuilder(T certificateBuilder) {
        super(certificateBuilder);
    }


    /**
     * Adds an extension
     * @param objectIdentifier The Extensions Object Identifier
     * @param isCritical Whether the Extension is Critical or Not. When 'true', the entity processing the certificate must parse this extension.
     *                   - Note that the X.509 v3 & Matter protocol specify whether extensions are critical or not
     * @param value The value this extension holds, the type of this value is based on the Extensions requirements and must be a subclass of ASN1Encodable
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, boolean isCritical, ASN1Encodable value) throws IOException {
        this.extensionsGenerator.addExtension(objectIdentifier, isCritical, value);
        return this;
    }

    /**
     * Adds an extension
     * - Encodes the value as a DEROctetString
     * @param objectIdentifier The Extensions Object Identifier
     *      * @param isCritical Whether the Extension is Critical or Not. When 'true', the entity processing the certificate must parse this extension.
     *                   - Note that the X.509 v3 & Matter protocol specify whether extensions are critical or not
     * @param value A byte array holding the value
     *              - The value will be encoded as a DEROctetString
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, boolean isCritical, byte[] value) {
        this.extensionsGenerator.addExtension(objectIdentifier, isCritical, value);
        return this;
    }

    /**
     * Adds an extension
     * - Automatically sets the extension as critical if its in {@link CertificateExtensionsBuilder#CRITICAL_EXTENSIONS}
     * @param objectIdentifier The Extensions Object Identifier
     * @param value A subclass of ASN1Encodable
     * @return This builder for chaining operations
     * @throws IOException If the extension failed to add, usually due to a wrong value
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, ASN1Encodable value) throws IOException {
        this.addExtension(objectIdentifier, CRITICAL_EXTENSIONS.contains(objectIdentifier), value);
        return this;
    }

    /**
     * - Encodes the value as a DEROctetString
     * - Automatically sets the extension as critical if its in {@link CertificateExtensionsBuilder#CRITICAL_EXTENSIONS}
     *
     * @param objectIdentifier
     * @param value
     * @return This builder for chaining operations
     */
    public CertificateExtensionsBuilder<T> addExtension(ASN1ObjectIdentifier objectIdentifier, byte[] value) {
        this.addExtension(objectIdentifier, CRITICAL_EXTENSIONS.contains(objectIdentifier), value);
        return this;
    }

    /**
     * The key-usage extension defines the purpose of the key contained in the certificate
     * - This is a Critical Extension
     *
     * @param keyUsageId An identifier for the key usage.
     *                   - Can be constructed with bitwise operations KeyUsage.keyCertSign | KeyUsage.cRLSign
     * @return This builder for chaining operations
     */
    public CertificateExtensionsBuilder<T> setKeyUsage(int keyUsageId) throws IOException {
        this.addExtension(Extension.keyUsage, true, new KeyUsage(keyUsageId));
        return this;
    }


    public List<Extension> getExtensions(){
        Extensions extensions = this.extensionsGenerator.generate();
        return Arrays.stream(extensions.getExtensionOIDs()).map(extensions::getExtension).collect(Collectors.toList());
    }
}