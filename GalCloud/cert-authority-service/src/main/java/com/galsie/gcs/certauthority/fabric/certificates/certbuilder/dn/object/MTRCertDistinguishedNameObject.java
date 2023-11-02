package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.object;


import lombok.AllArgsConstructor;

/**
 * This enum holds a list of Matter-Specific Distinguished-Name-Object identifiers
 * - Inherits from {@link DistinguishedNameObject}
 */
@AllArgsConstructor
public enum MTRCertDistinguishedNameObject implements DistinguishedNameObject{
    /**
     * Matter Operational Certificate DN attribute for node identifier
     * - TLV Tag: 17
     * - Name: matter-node-id
     * - Allowed Types: UTF8String
     */
    MATTER_NODE_ID("1.3.6.1.4.1.37244.1.1"),

    /**
     * Matter Operational Certificate DN attribute for firmware signing identifier
     * - TLV Tag: 18
     * - Name: matter-firmware-signing-id
     * - Allowed Types: UTF8String
     */
    MATTER_FIRMWARE_SIGNING_ID("1.3.6.1.4.1.37244.1.2"),

    /**
     * Matter Operational Certificate DN attribute for Intermediate CA (ICA) identifier
     * - TLV Tag: 19
     * - Name: matter-icac-id
     * - Allowed Types: UTF8String
     */
    MATTER_ICAC_ID("1.3.6.1.4.1.37244.1.3"),

    /**
     * Matter Operational Certificate DN attribute for Root Certificate Authority (CA) identifier
     * - TLV Tag: 20
     * - Name: matter-rcac-id
     * - Allowed Types: UTF8String
     */
    MATTER_RCAC_ID("1.3.6.1.4.1.37244.1.4"),

    /**
     * Matter Operational Certificate DN attribute for fabric identifier
     * - TLV Tag: 21
     * - Name: matter-fabricid
     * - Allowed Types: UTF8String
     */
    MATTER_FABRIC_ID("1.3.6.1.4.1.37244.1.5"),

    /**
     * Matter Operational Certificate DN attribute for CASE Authenticated Tag
     * - TLV Tag: 22
     * - Name: matter-noc-cat
     * - Allowed Types: UTF8String
     */
    MATTER_NOC_CAT("1.3.6.1.4.1.37244.1.6"),

    /**
     * Matter Device Attestation Certificate DN attribute for the Vendor ID (VID)
     * - TLV Tag: N/A
     * - Name: matter-oid-vid
     * - Allowed Types: UTF8String, PrintableString
     */
    MATTER_OID_VID("1.3.6.1.4.1.37244.2.1"),

    /**
     * Matter Device Attestation Certificate DN attribute for the Product ID (PID)
     * - TLV Tag: N/A
     * - Name: matter-oid-pid
     * - Allowed Types: UTF8String, PrintableString
     */
    MATTER_OID_PID("1.3.6.1.4.1.37244.2.2");


    private final String asn1OID;

    @Override
    public String getASN1OID() {
        return asn1OID;
    }
}
