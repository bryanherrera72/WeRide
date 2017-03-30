package com.weride.www.awsmobilehelper.auth;


/**
 * The result for IdentityManager's doStartupAuth().
 */
public class StartupAuthResult {
    private final IdentityManager identityManager;
    private final StartupAuthErrorDetails errors;

    /* package */ StartupAuthResult(final IdentityManager identityManager,
                                    final StartupAuthErrorDetails startupAuthErrorDetails) {
        this.identityManager = identityManager;
        this.errors = startupAuthErrorDetails;
    }

    /**
     * return true if signed in with an identity provider, otherwise false if signed in as an
     * unauthenticated (guest) identity or not signed in at all.
     */
    public boolean isUserSignedIn() {
        return identityManager.isUserSignedIn();
    }
    /**
     * @return true if an unauthenticated (guest) identity was obtained, otherwise false.
     */
    public boolean isUserAnonymous() {
        return didObtainIdentity() && !isUserSignedIn();
    }

    /**
     * @return true if an identity was obtained, either unauthenticated (guest) or authenticated with a provider.
     */
    private boolean didObtainIdentity() {
        return identityManager.getCachedUserID() != null;
    }

    /**
     * @return the identity manager.
     */
    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    /**
     * @return StartupAuthErrorDetails object if errors occurred during the StartupAuthResult flow, otherwise null.
     */
    public StartupAuthErrorDetails getErrorDetails() {
        return errors;
    }
}
