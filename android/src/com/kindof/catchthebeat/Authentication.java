package com.kindof.catchthebeat;

import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.authentication.IAuthentication;
import com.kindof.catchthebeat.authentication.AuthenticationListener;
import com.kindof.catchthebeat.screens.authentication.SignInScreen;
import com.kindof.catchthebeat.screens.authentication.SignUpScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.SideBar;
import com.kindof.catchthebeat.ui.intenthandler.AndroidIntentHandler;
import com.kindof.catchthebeat.ui.intenthandler.IIntentHandler;
import com.kindof.catchthebeat.user.User;

public class Authentication implements IAuthentication, AuthenticationListener {
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private AndroidLauncher androidLauncher;
    
    public Authentication(AndroidLauncher androidLauncher) {
        auth = FirebaseAuth.getInstance();
        this.androidLauncher = androidLauncher;
        currentUser = auth.getCurrentUser();
    }

    @Override
    public void signUp(final String email, final String password, final String nickname) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(aVoid -> onSendEmailSuccessful(email)).addOnFailureListener(this::onSendEmailFail);
            auth.signInWithEmailAndPassword(email, password);
            Globals.GAME.getDatabase().setUser(new User(auth.getCurrentUser().getUid(), nickname, 0, 0, 0, 0, 0, 0, 0));
            auth.signOut();
            currentUser = null;
            onSignUpSuccessful();
        }).addOnFailureListener(this::onSignUpFail);
    }

    @Override
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
            currentUser = auth.getCurrentUser();
            if (currentUser.isEmailVerified()) {
                onSignInSuccessful();
            } else {
                onEmailIsNotVerified(email);
            }
        }).addOnFailureListener(this::onSignInFail);
    }

    public void signOut() {
        auth.signOut();
    }

    @Override
    public boolean currentUserNotExist() {
        return currentUser == null || !currentUser.isEmailVerified();
    }

    @Override
    public String getCurrentUserUid() {
        return currentUser.getUid();
    }

    @Override
    public void onSendEmailSuccessful(String email) {
        onEmailIsNotVerified(email.split("@")[1]);
    }

    @Override
    public void onSendEmailFail(Exception e) {
        Toast.makeText(androidLauncher, "Email: Fail to Send.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccessful() {
        Toast.makeText(androidLauncher, "Sign-Up: Successful.", Toast.LENGTH_SHORT).show();
        SignUpScreen signUpScreen = Globals.AUTHENTICATION_SCREEN.getSignUpScreen();
        SignInScreen signInScreen = Globals.AUTHENTICATION_SCREEN.getSignInScreen();
        signInScreen.getEmailTextField().setText(signUpScreen.getEmail());
        Globals.GAME.setScreen(signInScreen);
        signUpScreen.clearAllFields();
    }

    @Override
    public void onSignUpFail(Exception e) {
        Toast.makeText(androidLauncher, "Sign-Up: Fail.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignInSuccessful() {
        Globals.USER = Globals.GAME.getDatabase().getUser(currentUser.getUid());
        Globals.STATISTIC_SCREEN.reInitLabels();
        final SideBar sideBar = Globals.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
        final String pathToUserDirectory = Globals.LOCAL_PATH_TO_USERS_DIRECTORY + Globals.USER.getUid() + "/";
        Gdx.app.postRunnable(() -> {
            sideBar.initFriendScrollPane();
            sideBar.getCurrentUserScreen().getAddDeleteFriendScreen().initUserScrollPaneItems();
            sideBar.initCurrentUserNicknameLabel();
            if (!Gdx.files.local(pathToUserDirectory + "icon").exists()) {
                Globals.CURRENT_FILE_TYPE = FileType.userIcon;
                Globals.GAME.getStorage().getFile(pathToUserDirectory + "icon", Gdx.files.local(pathToUserDirectory + "icon").file());
            } else {
                Globals.USER.initIcon();
            }
        });
        if (!Gdx.files.local(pathToUserDirectory).exists()) {
            Gdx.files.local(pathToUserDirectory).file().mkdir();
        }
        Toast.makeText(androidLauncher, "Sign-In: Successful.", Toast.LENGTH_SHORT).show();
        Globals.GAME.setScreen(Globals.MAIN_MENU_SCREEN);
    }

    @Override
    public void onSignInFail(Exception e) {
        Toast.makeText(androidLauncher, "Sign-In: Fail.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailIsNotVerified(String domain) {
        Toast.makeText(androidLauncher, "Verify your email.", Toast.LENGTH_LONG).show();
        if (domain != null) {
            IIntentHandler intentHandler = Globals.GAME.getIntentHandler();
            if (domain.equals("gmail.com")) {
                intentHandler.launchAnotherApp(AndroidIntentHandler.ActivityPackage.gmail.getPackageName());
            } else if (domain.equals("yandex.ru")) {
                intentHandler.launchAnotherApp(AndroidIntentHandler.ActivityPackage.yandex.getPackageName());
            } else if (
                    domain.equals("mail.ru")
                            || domain.equals("inbox.ru")
                            || domain.equals("list.ru")
                            || domain.equals("bk.ru")
                            || domain.equals("internet.ru")
            ) {
                intentHandler.launchAnotherApp(AndroidIntentHandler.ActivityPackage.mail.getPackageName());
            }
        }
    }
}
