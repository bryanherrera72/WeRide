package com.weride.www.awsmobilehelper.auth;
//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.16
//

/**
 * Interface for calling IdentityManager's doStartupAuth().
 */
public interface StartupAuthResultHandler {
    /**
     * Called when the startup auth flow is complete.
     * For Optional Sign-in one of the following occurred:
     * 1. No identity was obtained.
     * 2. An unauthenticated (guest) identity was obtained.
     * 3. An authenticated identity was obtained (using an identity provider).
     *
     * @param authResults the StartupAuthResult object containing the results for doStartupAuth().
     */
    void onComplete(StartupAuthResult authResults);
}
