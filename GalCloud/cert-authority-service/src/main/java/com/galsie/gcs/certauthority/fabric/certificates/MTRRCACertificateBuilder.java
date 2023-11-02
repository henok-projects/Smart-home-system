package com.galsie.gcs.certauthority.fabric.certificates;


import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.CertificateDNBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.object.DistinguishedNameObject;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.object.MTRCertDistinguishedNameObject;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.exception.MaxSupportedRDNCountExceededException;
/**
 * Matter Root Certificate Authority Certificate (RCAC) builder.
 * - Generally, there should only be one RCAC
 *
 *
 * A root certificate optionally may:
 * - Encode a Fabric Id
 * - Encoded an RCAC Id
 */
public class MTRRCACertificateBuilder extends MTRCertificateBuilder<MTRRCACertificateBuilder> {


    /**
     * The 'rcac-id' is encoded in an RDN as part of the Subject
     * - The identifier of the 'rcac-id' DN-Object is defined in {@link MTRCertDistinguishedNameObject}
     * - This method is for convenience, you can equivalently get the subject DN and do {@link CertificateDNBuilder#addRDN(DistinguishedNameObject, String)} }
     * <p>
     * Only an RCAC certificate may hold the rcac-id in its Subject DN
     * - The id is optional and is useful for identifying the CA and debugging purposes.
     * - The identifier must be 64-bits
     *
     * @param id A unique identifier (within Galsie) for the RCAC
     */
    public MTRRCACertificateBuilder setRCACIdDN(Long id) throws MaxSupportedRDNCountExceededException {
        return (MTRRCACertificateBuilder) this.subjectDN().addRDN(MTRCertDistinguishedNameObject.MATTER_RCAC_ID, String.valueOf(id)).done();
    }

}
