package com.weride.www.awsmobilehelper.auth;


/**
 * Supported Provider Types
 */
public enum IdentityProviderType {

    /**
     * Facebook
     */
    FACEBOOK,

    /**
     * Google
     */
    GOOGLE,

    /**
     * Twitter
     */
    TWITTER,

    /**
     * Amazon
     */
    AMAZON,

    /**
     * Custom SAML Provider
     */
    CUSTOM_SAML,

    /**
     * Cognito User Pool
     */
    COGNITO_USER_POOL;
}
