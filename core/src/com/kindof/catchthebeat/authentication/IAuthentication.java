package com.kindof.catchthebeat.authentication;

public interface IAuthentication {
    void signUp(String email, String password, String nickname);

    void signIn(String email, String password);

    void signOut();

    boolean currentUserNotExist();

    String getCurrentUserUid();
}
