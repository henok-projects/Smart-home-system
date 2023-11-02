package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair.algo;

import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.SecurityProvider;
import org.bouncycastle.jce.ECNamedCurveTable;

import java.security.spec.AlgorithmParameterSpec;

public class ECDSA extends KeypairGenerationAlgorithm {

    /**
     * ECDSA Algorithms with Specs
     */
    public static ECDSA SECP_256_R1 = new ECDSA(ECNamedCurveTable.getParameterSpec("secp256r1"));

    /**
     * Algorithm Identifier is the same for all instances of the algorithm, what is changing is the parameter spec
     */
    private static String ALGORITHM_IDENTIFIER = "ECDSA";

    /**
     * Creates a new instance of the algorithm with the given parameter spec (such as what type of curve), and an algorithm provider (such as bouncy castle)
     * @param algorithmParameterSpec
     * @param securityProvider
     */
    private ECDSA(AlgorithmParameterSpec algorithmParameterSpec, SecurityProvider securityProvider) {
        super(algorithmParameterSpec, securityProvider);
    }

    private ECDSA(AlgorithmParameterSpec algorithmParameterSpec) {
        this(algorithmParameterSpec, SecurityProvider.BOUNCY_CASTLE);
    }

    public KeypairGenerationAlgorithm withSecurityProvider(SecurityProvider securityProvider) {
        if (securityProvider == this.securityProvider) {
            return this;
        }
        return new ECDSA(this.getAlgorithmParameterSpec(), securityProvider);
    }


    public String getAlgorithmIdentifier() {
        return ALGORITHM_IDENTIFIER;
    }
}