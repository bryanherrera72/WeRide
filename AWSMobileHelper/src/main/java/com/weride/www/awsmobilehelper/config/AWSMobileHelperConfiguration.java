package com.weride.www.awsmobilehelper.config;

/**
 * Created by Francis on 3/26/2017.
 */

import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Map;

/**
 * Common configuration for the AWSMobileHelper.
 */
public class AWSMobileHelperConfiguration {
    private static final String CONFIG_KEY_COGNITO_REGION = "cognitoRegion";
    private static final String CONFIG_KEY_COGNITO_IDENTITY_POOL_ID = "cognitoPoolId";
    private static final String CONFIG_KEY_COGNITO_USER_POOL_ID = "cognitoUserPoolId";
    private static final String CONFIG_KEY_COGNITO_USER_POOL_CLIENT_ID = "cognitoUserPoolClientId";
    private static final String CONFIG_KEY_COGNITO_USER_POOL_CLIENT_SECRET = "cognitoUserPoolClientSecret";

    /**
     * Mapping for configuration values.
     */
    private final Map<String, Object> config;

    private AWSMobileHelperConfiguration(final Map<String, Object> config) {
        this.config = config;
    }

    public Regions getCognitoRegion() {
        return (Regions) config.get(CONFIG_KEY_COGNITO_REGION);
    }

    /**
     * @return the Cognito Identity Pool ID.
     */
    public String getCognitoIdentityPoolId() {
        return (String) config.get(CONFIG_KEY_COGNITO_IDENTITY_POOL_ID);
    }

    /**
     * @return the Cognito Identity Pool ID.
     */
    public String getCognitoUserPoolId() {
        return (String) config.get(CONFIG_KEY_COGNITO_USER_POOL_ID);
    }

    public String getCognitoUserPoolClientId() {
        return (String) config.get(CONFIG_KEY_COGNITO_USER_POOL_CLIENT_ID);
    }

    public String getCognitoUserPoolClientSecret() {
        return (String) config.get(CONFIG_KEY_COGNITO_USER_POOL_CLIENT_SECRET);
    }

    /**
     * Builder to aid in creating an AWSMobileHelperConfiguration.
     */
    public static class Builder {
        final Map<String, Object> config = new HashMap<>();

        /**
         * Sets the Cognito Region for the configuration to be built.
         *
         * @param region the region.
         * @return the builder for chaining.
         */
        public Builder withCognitoRegion(final Regions region) {
            config.put(CONFIG_KEY_COGNITO_REGION, region);
            return this;
        }

        /**
         * Sets the Cognito Identity Pool ID for the configuration to be built.
         * @param identityPoolId the identity pool id.
         * @return the builder for chaining.
         */
        public Builder withCognitoIdentityPoolId(final String identityPoolId) {
            config.put(CONFIG_KEY_COGNITO_IDENTITY_POOL_ID, identityPoolId);
            return this;
        }

        public Builder withCognitoUserPool(final String userPoolId,
                                           final String userPoolClientId,
                                           final String userPoolClientSecret) {
            config.put(CONFIG_KEY_COGNITO_USER_POOL_ID, userPoolId);
            config.put(CONFIG_KEY_COGNITO_USER_POOL_CLIENT_ID, userPoolClientId);
            config.put(CONFIG_KEY_COGNITO_USER_POOL_CLIENT_SECRET, userPoolClientSecret);
            return this;
        }

        /**
         * Builds the Configuration.
         *
         * @return the configuration.
         */
        public AWSMobileHelperConfiguration build() {
            return new AWSMobileHelperConfiguration(config);
        }
    }
}
