package com.kindof.catchthebeat;

import android.net.Uri;
import android.widget.Toast;
import com.badlogic.gdx.Gdx;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.storage.StorageListener;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.downloadbeatmap.BeatmapDownloadScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.FriendScrollPaneItem;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.SideBar;
import com.kindof.catchthebeat.user.User;

import java.io.File;

public class Storage implements IStorage, StorageListener {
    private StorageReference storageRef;
    private AndroidLauncher androidLauncher;
    private int getFileCounter, putFileCounter;

    public Storage(AndroidLauncher androidLauncher) {
        getFileCounter = putFileCounter = 0;
        this.androidLauncher = androidLauncher;
        storageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public void resetGetFileCounter() {
        getFileCounter = 0;
    }

    public void resetPutFileCounter() {
        putFileCounter = 0;
    }

    @Override
    public void putFile(Object uri) {
        final String path = Globals.LOCAL_PATH_TO_USERS_DIRECTORY + Globals.USER.getUid() + "/icon";
        storageRef.child(path).putFile((Uri) uri)
                .addOnSuccessListener(taskSnapshot -> Toast.makeText(androidLauncher, "Your icon was uploaded.", Toast.LENGTH_SHORT).show())
                .addOnCanceledListener(() -> Toast.makeText(androidLauncher, "Your icon wasn't uploaded.", Toast.LENGTH_LONG).show())
                .addOnFailureListener(e -> Toast.makeText(androidLauncher, "Your icon wasn't uploaded.", Toast.LENGTH_LONG).show())
                .addOnProgressListener(this::onProgressPutFile);
    }

    @Override
    public void putFile(String path, final File file) {
        storageRef.child(path).putFile(Uri.fromFile(file)).addOnSuccessListener(taskSnapshot -> {
            if (Globals.CURRENT_FILE_TYPE == FileType.beatmap) {
                if (Globals.CURRENT_BEATMAP_ID != null) {
                    putFileCounter++;
                    if (putFileCounter == Globals.CURRENT_FILE_TYPE.fileCount()) {
                        Gdx.app.postRunnable(() -> {
                            BeatmapDownloadScreen beatmapDownloadScreen = Globals.BEATMAP_SELECTION_MENU_SCREEN.getSideBar().getCurrentUserScreen().getBeatmapDownloadScreen();
                            beatmapDownloadScreen.addScrollPaneItem(Globals.GAME.getDatabase().getBeatmaps().get(Globals.CURRENT_BEATMAP_ID));
                            resetPutFileCounter();
                            Globals.CURRENT_BEATMAP_ID = null;
                            Globals.CURRENT_FILE_TYPE = FileType.unknown;
                        });
                        Toast.makeText(androidLauncher, "The beatmap was published.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                onSuccessfulPutFile();
            }
        }).addOnCanceledListener(() -> {
            onCanceledPutFile();
            resetPutFileCounter();
        }).addOnFailureListener(e -> {
            onFailurePutFile(e);
            resetPutFileCounter();
        }).addOnProgressListener(this::onProgressPutFile);
    }

    @Override
    public void getFile(String path, final File file) {
        storageRef.child(path).getFile(file).addOnSuccessListener(taskSnapshot -> {
            if (Globals.CURRENT_FILE_TYPE == FileType.beatmap) {
                if (Globals.CURRENT_BEATMAP_ID != null && putFileCounter == 0) {
                    getFileCounter++;
                    if (getFileCounter == Globals.CURRENT_FILE_TYPE.fileCount()) {
                        Gdx.app.postRunnable(() -> {
                            Beatmap beatmap = new Beatmap(Globals.CURRENT_BEATMAP_ID);
                            Globals.BEATMAP_SELECTION_MENU_SCREEN.addScrollPaneItem(beatmap);
                            resetGetFileCounter();
                            Globals.CURRENT_BEATMAP_ID = null;
                            Globals.CURRENT_FILE_TYPE = FileType.unknown;
                        });
                        Toast.makeText(androidLauncher, "The beatmap was downloaded.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (Globals.CURRENT_FILE_TYPE == FileType.userIcon) {
                Gdx.app.postRunnable(() -> {
                    Globals.USER.initIcon();
                    Globals.CURRENT_FILE_TYPE = FileType.unknown;
                });
            } else {
                onSuccessfulGetFile();
            }
        }).addOnCanceledListener(() -> {
            if (Globals.CURRENT_FILE_TYPE == FileType.userIcon) {
                initUserDefaultIcon();
            } else {
                onCanceledGetFile();
                resetGetFileCounter();
            }
        }).addOnFailureListener(e -> {
            if (Globals.CURRENT_FILE_TYPE == FileType.userIcon) {
                initUserDefaultIcon();
            } else {
                onFailureGetFile(e);
                resetGetFileCounter();
            }
        }).addOnProgressListener(this::onProgressGetFile);
    }

    private void initUserDefaultIcon() {
        Gdx.app.postRunnable(() -> {
            Globals.USER.initDefaultIcon();
            Globals.CURRENT_FILE_TYPE = FileType.unknown;
        });
    }

    @Override
    public void getFile(String path, final File file, final User friend, final float itemWidth, final float itemHeight, final float itemPadLeftRight, final float itemPadTopBottom) {
        storageRef.child(path).getFile(file)
                .addOnSuccessListener(taskSnapshot -> Gdx.app.postRunnable(() -> {
                    SideBar sideBar = Globals.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
                    FriendScrollPaneItem friendScrollPaneItem = sideBar.initFriendScrollPaneItemWithIcon(friend, itemWidth, itemHeight);
                    sideBar.addFriendScrollPaneItem(friendScrollPaneItem, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom);
                }))
                .addOnCanceledListener(() -> initFriendScrollPaneItemWithDefaultIcon(friend, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom))
                .addOnFailureListener(e -> initFriendScrollPaneItemWithDefaultIcon(friend, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom))
                .addOnProgressListener(taskSnapshot -> {

                });
    }

    private void initFriendScrollPaneItemWithDefaultIcon(final User friend, final float itemWidth, final float itemHeight, final float itemPadLeftRight, final float itemPadTopBottom) {
        Gdx.app.postRunnable(() -> {
            SideBar sideBar = Globals.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
            FriendScrollPaneItem friendScrollPaneItem = sideBar.initFriendScrollPaneItemWithDefaultIcon(friend, itemWidth, itemHeight);
            sideBar.addFriendScrollPaneItem(friendScrollPaneItem, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom);
        });
    }

    @Override
    public void onSuccessfulGetFile() {
        Toast.makeText(androidLauncher, "The file was downloaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCanceledGetFile() {
        Toast.makeText(androidLauncher, "The file wasn't downloaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressGetFile(Object taskSnapshot) {
        FileDownloadTask.TaskSnapshot _taskSnapshot = (FileDownloadTask.TaskSnapshot) taskSnapshot;
    }

    @Override
    public void onFailureGetFile(Exception e) {
        System.err.println(e.toString());
    }

    @Override
    public void onSuccessfulPutFile() {
        Toast.makeText(androidLauncher, "The file was uploaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCanceledPutFile() {
        Toast.makeText(androidLauncher, "The file wasn't uploaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressPutFile(Object taskSnapshot) {
        UploadTask.TaskSnapshot _taskSnapshot = (UploadTask.TaskSnapshot) taskSnapshot;
    }

    @Override
    public void onFailurePutFile(Exception e) {
        System.err.println(e.toString());
    }
}
