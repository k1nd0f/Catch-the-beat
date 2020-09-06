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
import android.os.Process;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.tools.OszParser;
import com.kindof.catchthebeat.tools.RealPathUtil;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class AndroidLauncher extends AndroidApplication implements IToastMaker {
	private int currentApiVersion;
	private boolean fullscreen;
	private AndroidIntentHandler intentHandler;
	private Authentication firebaseAuth;
	private Database firebaseDatabase;
	private Storage firebaseStorage;
	private Context context;
	private int flags;

    @Override
    @SuppressLint({"NewApi"})
    protected void onCreate (Bundle savedInstanceState) {
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

        context = getContext();
        fullscreen = true;
        firebaseAuth = new Authentication(context);
        firebaseDatabase = new Database();
        firebaseStorage = new Storage(context);

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
        }

        currentApiVersion = Build.VERSION.SDK_INT;
        flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && fullscreen) {
            getWindow().getDecorView().setSystemUiVisibility(flags);

            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        intentHandler = new AndroidIntentHandler(this);
        config.useCompass = false;
        config.useAccelerometer = false;
        initialize(new CatchTheBeatGame(this, firebaseAuth, firebaseDatabase, firebaseStorage, intentHandler), config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            Uri uri = data.getData();
            if (resultCode == RESULT_OK && requestCode == AndroidIntentHandler.SELECT_CODE && uri != null) {
                intentHandler.setSelectedFileUri(uri);

                if (Res.CURRENT_FILE_TYPE == FileType.userIcon) {
                    firebaseStorage.putFile(uri);
                    String
                            uid = Res.USER.getUid(),
                            localDestPath = Res.LOCAL_PATH_TO_USERS_DIRECTORY + uid + "/";

                    FileHandle
                            destFileHandle = Gdx.files.local(localDestPath),
                            srcFileHandlde = Gdx.files.absolute(RealPathUtil.getRealPath(context, uri));

                    srcFileHandlde.copyTo(destFileHandle);
                    destFileHandle.child(srcFileHandlde.name()).file().renameTo(Gdx.files.local(localDestPath + "icon").file());
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Res.USER.initIcon();
                        }
                    });
                } else if (Res.CURRENT_FILE_TYPE == FileType.beatmapEditorMusic) {
                    copyFileToEditorTmpDirectory("audio.mp3", uri);
                    Res.BEATMAP_EDITOR_SCREEN.setMusic(Gdx.audio.newMusic(Gdx.files.local(Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + "audio.mp3")));
                    Res.CURRENT_FILE_TYPE = FileType.unknown;
                } else if (Res.CURRENT_FILE_TYPE == FileType.beatmapEditorBackground) {
                    copyFileToEditorTmpDirectory("background", uri);
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Res.BEATMAP_EDITOR_SCREEN.initBackground();
                        }
                    });
                } else if (Res.CURRENT_FILE_TYPE == FileType.oszFile) {
                    String
                            oszFilePath = RealPathUtil.getRealPath(context, uri),
                            destinationPath = Gdx.files.getLocalStoragePath() + "osz/";

                    if (oszFilePath == null || !Gdx.files.absolute(oszFilePath).extension().equals("osz")) {
                        makeToast("Select another file.", 0);
                        Res.CURRENT_FILE_TYPE = FileType.unknown;
                        return;
                    }

                    File
                            oszFile = Gdx.files.absolute(oszFilePath).file(),
                            destinationDirectory = Gdx.files.absolute(destinationPath).file();

                    boolean isParsed = OszParser.unzipOsz(oszFile, destinationDirectory);
                    if (isParsed) {
                        makeToast("*.osz file was parsed.", 0);
                    } else {
                        makeToast("*.osz file wasn't parsed.", 0);
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
        Res.CURRENT_FILE_TYPE = FileType.unknown;
    }

    private void copyFileToEditorTmpDirectory(String fileName, Uri uri) {
        String
                absoluteSrcPath = RealPathUtil.getRealPath(context, uri),
                localFilePath = Res.LOCAL_PATH_TO_BEATMAP_EDITOR_TMP_DIRECTORY + fileName;

        if (Gdx.files.local(localFilePath).exists()) {
            Gdx.files.local(localFilePath).delete();
        }

        Gdx.files.absolute(absoluteSrcPath).copyTo(Gdx.files.local(localFilePath));
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
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
    }

    @Override
    public void makeToast(final String text, final int length) {
        this.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, length).show();
            }
        });
    }
}
