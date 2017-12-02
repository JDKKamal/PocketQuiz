package com.jdkgroup.socialintegration.googleintegration;

public interface GoogleLoginListener {
  void onGoogleAuthSignIn(GoogleLoginModel googleLoginModel);
  void onGoogleAuthSignInFailed(String errorMessage);
  void onGoogleAuthSignOut();
}
