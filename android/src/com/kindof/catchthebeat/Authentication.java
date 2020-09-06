package com.kindof.catchthebeat;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.authentication.IAuthentication;
import com.kindof.catchthebeat.authentication.AuthenticationListener;
import com.kindof.catchthebeat.screens.authentication.SignInScreen;
import com.kindof.catchthebeat.screens.authentication.SignUpScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.SideBar;
import com.kindof.catchthebeat.users.User;

public class Authentication implements IAuthentication, AuthenticationListener {
    private FirebaseAuth auth;
    private Context context;
    private FirebaseUser currentUser;

    public Authentication(Context context) {
        auth = FirebaseAuth.getInstance();
        this.context = context;
        currentUser = auth.getCurrentUser();
    }

    @Override
    public void signUp(final String email, final String password, final String nickname) {
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        onSendEmailSuccessful();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onSendEmailFail(e);
                    }
                });
                auth.signInWithEmailAndPassword(email, password);
                Res.GAME.getDatabase().setUser(new User(auth.getCurrentUser().getUid(), nickname, 0, 0, 0, 0, 0, 0, 0));
                auth.signOut();
                currentUser = null;
                onSignUpSuccessful();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onSignUpFail(e);
            }
        });
    }

    @Override
    public void signIn(String email, String password) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                currentUser = auth.getCurrentUser();
                if (currentUser.isEmailVerified()) {
                    onSignInSuccessful();
                } else {
                    onEmailIsNotVerified();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onSignInFail(e);
            }
        });
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
    public void onSendEmailSuccessful() {
        onEmailIsNotVerified();
    }

    @Override
    public void onSendEmailFail(Exception e) {
        Toast.makeText(context, "Email: Fail to Send.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSignUpSuccessful() {
        Toast.makeText(context, "Sign-Up: Successful.", Toast.LENGTH_SHORT).show();
        SignUpScreen signUpScreen = Res.AUTHENTICATION_SCREEN.getSignUpScreen();
        SignInScreen signInScreen = Res.AUTHENTICATION_SCREEN.getSignInScreen();
        signInScreen.getEmailTextField().setText(signUpScreen.getEmail());
        Res.GAME.setScreen(signInScreen);
        signUpScreen.clearAllFields();
    }

    @Override
    public void onSignUpFail(Exception e) {
        Toast.makeText(context, "Sign-Up: Fail.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSignInSuccessful() {
        Res.USER = Res.GAME.getDatabase().getUser(currentUser.getUid());
        Res.STATISTIC_SCREEN.reInitLabels();
        final SideBar sideBar = Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
        final String pathToUserDirectory = Res.LOCAL_PATH_TO_USERS_DIRECTORY + Res.USER.getUid() + "/";
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                sideBar.initFriendScrollPane();
                sideBar.getCurrentUserScreen().getAddDeleteFriendScreen().initUserScrollPaneItems();
                sideBar.initCurrentUserNicknameLabel();
                if (!Gdx.files.local(pathToUserDirectory + "icon").exists()) {
                    Res.CURRENT_FILE_TYPE = FileType.userIcon;
                    Res.GAME.getStorage().getFile(pathToUserDirectory + "icon", Gdx.files.local(pathToUserDirectory + "icon").file());
                } else {
                    Res.USER.initIcon();
                }
            }
        });
        if (!Gdx.files.local(pathToUserDirectory).exists()) {
            Gdx.files.local(pathToUserDirectory).file().mkdir();
        }
        Toast.makeText(context, "Sign-In: Successful.", Toast.LENGTH_SHORT).show();
        Res.GAME.setScreen(Res.MAIN_MENU_SCREEN);
    }

    @Override
    public void onSignInFail(Exception e) {
        Toast.makeText(context, "Sign-In: Fail.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailIsNotVerified() {
        Toast.makeText(context, "Verify your email.", Toast.LENGTH_LONG).show();
    }
}
