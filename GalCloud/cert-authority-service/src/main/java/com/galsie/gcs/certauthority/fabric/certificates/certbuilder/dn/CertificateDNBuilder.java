package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn;

import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.CertificateBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.CertificateBuilderCommonImpl;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.object.DistinguishedNameObject;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.exception.MaxSupportedRDNCountExceededException;

/**
 * CertificateDNBuilder is used as part of the CertificateBuilder
 * - It holds a {@link DistinguishedNameBuilder} to build the distinguished Name
 * - It holds a {@link CertificateBuilderCommonImpl} to go back to the builder which holds this Distinguished Name
 * @param <T> A builder subclass of {@link CertificateBuilder}
 *           - Used to return to the builder (through calling the {@link CertificateDNBuilder#done()} method
 */
public class CertificateDNBuilder<T extends CertificateBuilder> {
    private T certificateBuilder;
    private DistinguishedNameBuilder distinguishedNameBuilder = new DistinguishedNameBuilder();

    /**
     * Initializes the Distinguished Name Builder
     * - The {@link CertificateBuilder} is expected for convenient chaining operations
     * @param certificateBuilder The {@link CertificateBuilder} holding this {@link CertificateDNBuilder}
     */
    public CertificateDNBuilder(T certificateBuilder){
        this.certificateBuilder = certificateBuilder;
    }
    /**
     * Adds a distinguished name to the subject
     * @param dnObjectIdentifier The ASN1 object identifier (dot joined string)
     * @param value The value, whatever its datatype is based on the requirements of the DN, encoded to a string
     * @return
     */
    public CertificateDNBuilder<T> addRDN(String dnObjectIdentifier, String value) throws MaxSupportedRDNCountExceededException {
        this.distinguishedNameBuilder.addRDN(dnObjectIdentifier, value);
        return this;
    }

    /**
     * Adds a matter distinguished name to the subject
     * - This is equivalent to calling {@link CertificateDNBuilder#addRDN(String, String) with the matter-specific dnObjectIdentifier}
     * @param dn A subclass of {@link DistinguishedNameObject} (dot joined string)
     *           -
     * @param value The value, whatever its datatype is based on the requirements of the DN, encoded to a string
     * @return
     */
    public CertificateDNBuilder<T> addRDN(DistinguishedNameObject dn, String value) throws MaxSupportedRDNCountExceededException {
        this.distinguishedNameBuilder.addRDN(dn, value);
        return this;
    }

    /**
     * Return back to the certificate builder
     * @return The CertificateBuilder which this DNBuilder is part of
     */
    public T done(){
        return this.certificateBuilder;
    }

    /**
     * Not used while building, used to finally build.
     * @return The {@link DistinguishedNameBuilder} used to actually build the DN
     */
    public DistinguishedNameBuilder getDistinguishedNameBuilder(){
        return this.distinguishedNameBuilder;
    }
}
