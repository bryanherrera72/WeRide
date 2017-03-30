package  com.weride.www.awsmobilehelper.auth.signin;


import  com.weride.www.awsmobilehelper.auth.IdentityProvider;

/**
 *  Implement this interface to get callbacks for the results to a sign-in operation.
 */
public interface SignInProviderResultsHandler {

    /**
     * Sign-in was successful.
     *
     * @param provider sign-in identity provider
     */
    void onSuccess(IdentityProvider provider);

    /**
     * Sign-in was cancelled by the user.
     *
     * @param provider sign-in identity provider
     */
    void onCancel(IdentityProvider provider);

    /**
     * Sign-in failed.
     *
     * @param provider sign-in identity provider
     * @param ex exception that occurred
     */
    void onError(IdentityProvider provider, Exception ex);
}
