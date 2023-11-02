package com.galsie.gcs.certauthority.fabric.certificates.certbuilder;

import lombok.AllArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * The {@link SecurityProvider} defines a list of providers that could be used
 * - This is because some security features are not directly supported by Javas security framework (For example, ECDSA curve generation)
 * - For that, third party providers are used, like Bouncy Castle
 *
 *
 * This enum defines the different security providers that are currently used, and is referenced by various builders (for certificates) for that sake
 */
@AllArgsConstructor
public enum SecurityProvider {

    BOUNCY_CASTLE(BouncyCastleProvider.PROVIDER_NAME, () -> {
          Security.addProvider(new BouncyCastleProvider()); // NOTE: If the provider is already present, it is not added again
    });


    private final String providerIdentifier;
    private ThrowableRunnable registerAlgorithm;

    public String getProviderIdentifier(){
        return this.providerIdentifier;
    }

    public void registerProvider() throws Exception {
        this.registerAlgorithm.run();
    }
}
