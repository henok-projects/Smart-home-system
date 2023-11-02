package com.galsie.gcs.certauthority.fabric.certificates;

import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.CertificateBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.CertificateBuilderCommonImpl;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.object.MTRCertDistinguishedNameObject;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.exception.FabricIdNotSupportedException;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.exception.MaxSupportedRDNCountExceededException;
public abstract class MTRCertificateBuilder<T extends CertificateBuilder> extends CertificateBuilderCommonImpl<T> {

    /**
     * The 'fabric-id' is encoded in an RDN as part of the Subject
     * - The identifier of the 'fabric-id' DN-Object is defined in {@link MTRCertDistinguishedNameObject}
     * - This method is for convenience, you can equivalently get the subject DN and do {@link CertificateDNBuilder#addRDN(MTRCertDistinguishedNameObject, String)}
     * <p>
     * Any Matter-Certificate may hold a fabric ID:
     * - The NOC must hold the fabric ID
     * - The ICAC may hold the fabric ID
     * - If it does, NOCs signed by the Intermediate Certificate Authority must match the fabric ID
     * - The RCAC may hold the fabric ID
     * - If it does, ICACs/NOCs signed by the Root Certificate Authority must match the fabric ID
     * <p>
     * Note:
     * - The fabricId is a 64-bit integer
     * - Fabric ID 0 is reserved by the protoocol
     *
     * @param fabricId The id of the fabric. CANT BE 0, considered unsigned
     */
    public T setFabricIdRDN(Long fabricId) throws MaxSupportedRDNCountExceededException, FabricIdNotSupportedException {
        if (fabricId == 0){
            throw new FabricIdNotSupportedException(fabricId);
        }
        return (T) this.subjectDN().addRDN(MTRCertDistinguishedNameObject.MATTER_FABRIC_ID, Long.toUnsignedString(fabricId)).done();
    }
}
