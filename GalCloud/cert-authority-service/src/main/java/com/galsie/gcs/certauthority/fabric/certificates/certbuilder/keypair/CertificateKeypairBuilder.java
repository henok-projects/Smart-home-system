package com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair;

import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.CertificateBuilder;
import com.galsie.gcs.certauthority.fabric.certificates.certbuilder.keypair.algo.KeypairGenerationAlgorithm;
import com.galsie.gcs.serviceutilslibrary.utils.builder.InternalBuilder;

import java.security.KeyPair;

public class CertificateKeypairBuilder<T extends CertificateBuilder> extends InternalBuilder<T> {

    /**
     * The KeypairBuilder used to actually build the keypair
     * - It is wrapped with this class so we use it as an InternalBuilder
     */
    private KeypairBuilder keypairBuilder = KeypairBuilder.builder();



    public CertificateKeypairBuilder(T outerBuilder) {
        super(outerBuilder);
    }

    /**
     * Sets the Keypair Generation Algorithm (with the algorithm specification)
     *
     * @param keypairGenerationAlgorithm An instance of a subclass of KeypairGenerationAlgorithm:
     *                                   - eg: ECDSA.SECP_256_RV1
     *                                   Algorithms may have different providers, to change the provider:
     *                                   - ECDSA.SECP_256_RV1.withProvider(SecurityProvider...)
     */
    public CertificateKeypairBuilder<T> setGenerationAlgorithm(KeypairGenerationAlgorithm keypairGenerationAlgorithm) {
        this.keypairBuilder.setGenerationAlgorithm(keypairGenerationAlgorithm);
        return this;
    }

    /**
     * Builds the {@link KeyPair} with the Specified algorithm (including specs) and the security provider
     *
     * @return The generated {@link KeyPair}
     * @throws Exception
     */
    public KeyPair build() throws Exception {
        return keypairBuilder.build();
    }

}