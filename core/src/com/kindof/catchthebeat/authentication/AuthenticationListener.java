package com.kindof.catchthebeat.authentication;

public interface AuthenticationListener {
    void onSendEmailSuccessful(String email);

    void onSendEmailFail(Exception e);

    void onSignUpSuccessful();

    void onSignUpFail(Exception e);

    void onSignInSuccessful();

    void onSignInFail(Exception e);

    void onEmailIsNotVerified(String domain);
}
