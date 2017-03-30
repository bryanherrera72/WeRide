package com.weride.www.awsmobilehelper.auth;



/**
 * Encapsulates errors that may have happened during doStartupAuth().
 */
public class StartupAuthErrorDetails {
    private final IdentityProvider errorProvider;
    private final Exception providerException;
    private final Exception unauthException;

    /* package */ StartupAuthErrorDetails(final IdentityProvider errorProvider,
                                          final Exception providerException,
                                          final Exception unauthException) {
        this.errorProvider = errorProvider;
        this.providerException = providerException;
        this.unauthException = unauthException;
    }

    /**
     * @return true if an error occurred refreshing a previously signed in provider, otherwise false.
     */
    public boolean didErrorOccurRefreshingProvider() {
        return errorProvider != null;
    }

    /**
     * @return the identity provider that encountered an error, otherwise null.
     */
    public IdentityProvider getErrorProvider() {
        return errorProvider;
    }

    /**
     * @return the exception that occurred while refreshing a provider, otherwise null.
     */
    public Exception getProviderErrorException() {
        return providerException;
    }

    /**
     * @return true if an error occurred obtaining an unauthenticated identity, otherwise false.
     */
    public boolean didErrorOccurObtainingUnauthenticatedIdentity() {
        return unauthException != null;
    }

    /**
     * @return the exception that occurred while trying to obtain an unauthenticated (guest) identity.
     */
    public Exception getUnauthenticatedErrorException() {
        return unauthException;
    }
}
