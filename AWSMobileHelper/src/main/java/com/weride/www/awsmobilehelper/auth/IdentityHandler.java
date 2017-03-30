package com.weride.www.awsmobilehelper.auth;

/**
 * Allows the application to get an asynchronous response with user's
 * unique identifier.
 */
public interface IdentityHandler {
    /**
     * Handles the user's unique identifier.
     * @param identityId Amazon Cognito Identity ID which uniquely identifies
     *                   the user.
     */
    void onIdentityId(final String identityId);

    /**
     * Handles any error that might have occurred while getting the user's
     * unique identifier from Amazon Cognito.
     * @param exception exception
     */
    void handleError(final Exception exception);
}
