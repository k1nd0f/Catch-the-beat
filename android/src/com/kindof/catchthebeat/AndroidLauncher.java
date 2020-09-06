package com.kindof.catchthebeat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import android.os.Process;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.tools.RealPathUtil;
import com.kindof.catchthebeat.tools.Util;
import com.kindof.catchthebeat.ui.dialog.AndroidDialogWindow;
import com.kindof.catchthebeat.ui.intenthandler.AndroidIntentHandler;
import com.kindof.catchthebeat.ui.toast.AndroidToastMaker;
import com.kindof.catchthebeat.ui.toastmaker.IToastMaker;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AndroidLauncher extends AndroidApplication {
	private int currentApiVersion;
	private boolean fullscreen;
	private AndroidIntentHandler androidIntentHandler;
	private AndroidToastMaker androidToastMaker;
	private AndroidDialogWindow androidDialogWindow;
	private AndroidNetworkConnection androidNetworkConnection;
	private Authentication firebaseAuth;
	private Database firebaseDatabase;
	private Storage firebaseStorage;
	private int flags;

    @Override
    @SuppressLint({"NewApi"})
    protected void onCreate (Bundle savedInstanceState) {
        if (ProcessPhoenix.isPhoenixProcess(this)) {
            return;
        }

        super.onCreate(savedInstanceState);
        if (
                ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION) != PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_MEDIA_LOCATION
            }, 0);
        }

        androidDialogWindow = new AndroidDialogWindow(this);
        androidNetworkConnection = new AndroidNetworkConnection(this);
        androidIntentHandler = new AndroidIntentHandler(this);
        androidToastMaker = new AndroidToastMaker(this);

        firebaseAuth = new Authentication(this);
        firebaseStorage = new Storage(this);
        firebaseDatabase = new Database();

        // if it is first start of app, this block throw exception, else initialize fullscreen variable from 'config.xml' (local-path/config.xml)
        try {
            Scanner sc = new Scanner(new File(getContext().getFilesDir().getPath() + "/config.xml"));
            String s = "";
            while (sc.hasNext()) {
                s += sc.nextLine();
            }

            XmlReader xmlReader = new XmlReader();
            XmlReader.Element element = xmlReader.parse(s);
            fullscreen = element.getBoolean("Fullscreen");
        } catch (IOException e) {
            e.printStackTrace();
            fullscreen = true;
        }

        currentApiVersion = Build.VERSION.SDK_INT;
        flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && fullscreen) {
            getWindow().getDecorView().setSystemUiVisibility(flags);

            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(visibility -> {
                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                    decorView.setSystemUiVisibility(flags);
                }
            });
        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useCompass = false;
        config.useAccelerometer = false;
        initialize(new CatchTheBeatGame(androidNetworkConnection, androidIntentHandler, androidToastMaker, androidDialogWindow, firebaseAuth, firebaseDatabase, firebaseStorage), config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (resultCode == RESULT_OK && requestCode == AndroidIntentHandler.SELECT_CODE && uri != null) {
                androidIntentHandler.setSelectedFileUri(uri);

                if (Globals.CURRENT_FILE_TYPE == FileType.userIcon) {
                    firebaseStorage.putFile(uri);
                    String
                            uid = Globals.USER.getUid(),
                            localDestPath = Globals.LOCAL_PATH_TO_USERS_DIRECTORY + uid + "/";

                    FileHandle
                            destFileHandle = Gdx.files.local(localDestPath),
                            srcFileHandlde = Gdx.files.absolute(RealPathUtil.getRealPath(this, uri));

                    srcFileHandlde.copyTo(destFileHandle);
                    destFileHandle.child(srcFileHandlde.name()).file().renameTo(Gdx.files.local(localDestPath + "icon").file());
                    Gdx.app.postRunnable(() -> Globals.USER.initIcon());
                } else if (Globals.CURRENT_FILE_TYPE == FileType.beatmapEditorMusic) {
                    copyFileToLocalDirectory(uri, Globals.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY, "audio.mp3");
                    Gdx.app.postRunnable(() -> Globals.BEATMAP_EDITOR_SCREEN.initMusic());
                } else if (Globals.CURRENT_FILE_TYPE == FileType.beatmapEditorBackground) {
                    copyFileToLocalDirectory(uri, Globals.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY, "background");
                    Gdx.app.postRunnable(() -> Globals.BEATMAP_EDITOR_SCREEN.initBackground());
                } else if (Globals.CURRENT_FILE_TYPE == FileType.oszFile) {
                    String
                            oszFilePath = RealPathUtil.getRealPath(this, uri),
                            destinationPath = Gdx.files.getLocalStoragePath() + "osz/";

                    if (oszFilePath == null || !Gdx.files.absolute(oszFilePath).extension().equals("osz")) {
                        androidToastMaker.makeToast("Select another file.", IToastMaker.LENGTH_SHORT);
                        Globals.CURRENT_FILE_TYPE = FileType.unknown;
                        return;
                    }

                    File
                            oszFile = Gdx.files.absolute(oszFilePath).file(),
                            destinationDirectory = Gdx.files.absolute(destinationPath).file();

                    boolean isParsed = Util.unzip(oszFile, destinationDirectory);
                    if (isParsed) {
                        androidToastMaker.makeToast("*.osz file was parsed.", IToastMaker.LENGTH_SHORT);
                    } else {
                        androidToastMaker.makeToast("*.osz file wasn't parsed.", IToastMaker.LENGTH_SHORT);
                    }

                    // Check directory for unzip
                    FileHandle[] fileHandles = Gdx.files.absolute(destinationPath).list();
                    Gdx.app.log("OSZ-PARSER", fileHandles.length + "");
                    for (FileHandle fileHandle : fileHandles) {
                        Gdx.app.log("OSZ-PARSER", fileHandle.file().getAbsolutePath());
                    }
                }
            }
        }
        Globals.CURRENT_FILE_TYPE = FileType.unknown;
    }

    private void copyFileToLocalDirectory(Uri uri, String localPath, String fileName) {
        String absoluteSrcPath = RealPathUtil.getRealPath(this, uri);
        FileHandle srcFile = Gdx.files.absolute(absoluteSrcPath);
        FileHandle destinationDirectory = Gdx.files.local(localPath);
        FileHandle destinationFile = destinationDirectory.child(fileName);

        destinationFile.delete();
        srcFile.copyTo(destinationFile);
    }

    private void copyFileToEditorTmpDirectory(String fileName, Uri uri) {
        String
                absoluteSrcPath = RealPathUtil.getRealPath(this, uri),
                localFilePath = Globals.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + fileName;

        if (Gdx.files.local(localFilePath).exists()) {
            Gdx.files.local(localFilePath).delete();
        }

        Gdx.files.absolute(absoluteSrcPath).copyTo(Gdx.files.local(localFilePath));
    }

    public AndroidIntentHandler getAndroidIntentHandler() {
        return androidIntentHandler;
    }

    public AndroidToastMaker getAndroidToastMaker() {
        return androidToastMaker;
    }

    public AndroidDialogWindow getAndroidDialogWindow() {
        return androidDialogWindow;
    }

    public AndroidNetworkConnection getAndroidNetworkConnection() {
        return androidNetworkConnection;
    }

    public Authentication getFirebaseAuth() {
        return firebaseAuth;
    }

    public Database getFirebaseDatabase() {
        return firebaseDatabase;
    }

    public Storage getFirebaseStorage() {
        return firebaseStorage;
    }

    @Override
    @SuppressLint("NewApi")
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus && fullscreen) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }
}
