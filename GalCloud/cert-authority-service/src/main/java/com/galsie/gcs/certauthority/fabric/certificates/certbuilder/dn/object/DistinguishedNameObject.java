package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.dn.object;

/**
 * A DN-Object (Distinguished Name Object) is an object that could be an RDN in a DN (Distinguished Name)
 * - A Distinguished Name is a set of RDNs (Relative Distinguished Names)
 *    - The Subject & Issuer fields of the certificate hold a Distinguished Name
 * - An RDN is a key value pair where the key is a DN-Object, and the value is of some type depending on that object
 */
public interface DistinguishedNameObject {

    String getASN1OID();
}
