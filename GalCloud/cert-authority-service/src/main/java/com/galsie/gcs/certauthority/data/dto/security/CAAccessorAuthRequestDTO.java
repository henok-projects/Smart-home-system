package com.galsie.gcs.certauthority.data.dto.security;

import com.galsie.gcs.microservicecommon.lib.galsecurity.authenticator.GalSecurityAuthenticator;
import com.galsie.gcs.microservicecommon.lib.galsecurity.data.dto.authentication.request.AuthenticationRequestDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Certificate Authority Accessor Authentication DTO:
 * - An accessor to a certificate authority is an entity that performs requests to the authority
 * - An accessor must undergo some form of authentication so that they can access the certificate authority service
 *
 * Accessors would have different access privileges.
 * - Some have the permission to request certificate signing
 * - Some have permission to authenticate certificates
 *
 * This would be implemented:
 * - By:
 *  - Having different request paths for each accessor type, having as many different accessor authenticator services as needed
 *  - OR having different roles/privileges associated with accessor types & using a single accessor service
 *  - Go wild! Do whatever works, but be sure to work according to the security architecture imposed by micro-service-common
 *
 *  Implementation:
 *  - Used by {@link AccessorAuthenticatorService} to produce {@link CAAccessorAuthResponseDTO}
 *
 * {@inheritDoc}
 * Inherits AuthenticationRequestDTO for using in {@link AccessorAuthenticatorService} subclass of  {@link GalSecurityAuthenticator}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CAAccessorAuthRequestDTO implements AuthenticationRequestDTO {
    // nothing currently
}
