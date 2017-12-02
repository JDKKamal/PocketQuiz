package com.jdkgroup.socialintegration.facebookintegration;

public interface FacebookLoginListener {
    void onFbSignInFail(String errorMessage);
    void onFbSignInSuccess(FacebookLoginModel facebookLoginModel);
    void onFBSignOut();
}
