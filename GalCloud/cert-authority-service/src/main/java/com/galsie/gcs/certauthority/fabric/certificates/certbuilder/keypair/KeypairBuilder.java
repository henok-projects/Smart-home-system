package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair;

import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.SecurityProvider;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair.algo.KeypairGenerationAlgorithm;

import java.security.*;

/**
 * A Builder class for {@link KeyPair} that makes it more convenient to select the algorithm, the algorithm specs, and the provider
 */
public class KeypairBuilder {

    /**
     * Initializes the builder
     */
    private KeypairBuilder() {
    }

    private KeypairGenerationAlgorithm keypairGenerationAlgorithm;

    /**
     * Sets the Keypair Generation Algorithm (with the algorithm specification)
     *
     * @param keypairGenerationAlgorithm An instance of a subclass of KeypairGenerationAlgorithm:
     *                                   - eg: ECDSA.SECP_256_RV1
     *                                   Algorithms may have different providers, to change the provider:
     *                                   - ECDSA.SECP_256_RV1.withProvider(SecurityProvider...)
     */
    public KeypairBuilder setGenerationAlgorithm(KeypairGenerationAlgorithm keypairGenerationAlgorithm) {
        this.keypairGenerationAlgorithm = keypairGenerationAlgorithm;
        return this;
    }

    /**
     * Builds the {@link KeyPair} with the Specified algorithm (including specs) and the security provider
     * @return The generated {@link KeyPair}
     * @throws Exception
     */
    public KeyPair build() throws Exception {
        SecurityProvider securityProvider = keypairGenerationAlgorithm.getSecurityProvider();
        securityProvider.registerProvider();
        KeyPairGenerator g = KeyPairGenerator.getInstance(keypairGenerationAlgorithm.getAlgorithmIdentifier(), securityProvider.getProviderIdentifier());
        g.initialize(keypairGenerationAlgorithm.getAlgorithmParameterSpec(), new SecureRandom());
        return g.generateKeyPair();
    }

    /**
     * Creates a new instance of the builder
     * @return The builder
     */
    public static KeypairBuilder builder() {
        return new KeypairBuilder();
    }
}
