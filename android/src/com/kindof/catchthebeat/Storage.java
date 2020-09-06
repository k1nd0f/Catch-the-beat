package com.kindof.catchthebeat;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.badlogic.gdx.Gdx;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.resources.FileType;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.storage.StorageListener;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.downloadbeatmap.BeatmapDownloadScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.FriendScrollPaneItem;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.SideBar;
import com.kindof.catchthebeat.users.User;

import java.io.File;

public class Storage implements IStorage, StorageListener {
    private StorageReference storageRef;
    private Context context;
    private int getFileCounter, putFileCounter;

    public Storage(Context context) {
        getFileCounter = putFileCounter = 0;
        this.context = context;
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
        final String path = Res.LOCAL_PATH_TO_USERS_DIRECTORY + Res.USER.getUid() + "/icon";
        storageRef.child(path).putFile((Uri) uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Your icon was uploaded.", Toast.LENGTH_SHORT).show();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(context, "Your icon wasn't uploaded.", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Your icon wasn't uploaded.", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                onProgressPutFile(taskSnapshot);
            }
        });
    }

    @Override
    public void putFile(String path, final File file) {
        storageRef.child(path).putFile(Uri.fromFile(file)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (Res.CURRENT_FILE_TYPE == FileType.beatmap) {
                    if (Res.CURRENT_BEATMAP_ID != null) {
                        putFileCounter++;
                        if (putFileCounter == 4) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    BeatmapDownloadScreen beatmapDownloadScreen = Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar().getCurrentUserScreen().getBeatmapDownloadScreen();
                                    beatmapDownloadScreen.addScrollPaneItem(Res.GAME.getDatabase().getBeatmaps().get(Res.CURRENT_BEATMAP_ID));
                                    resetPutFileCounter();
                                    Res.CURRENT_BEATMAP_ID = null;
                                    Res.CURRENT_FILE_TYPE = FileType.unknown;
                                }
                            });
                            makeToast("The beatmap was published.", Toast.LENGTH_SHORT);
                        }
                    }
                } else {
                    onSuccessfulPutFile();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                onCanceledPutFile();
                resetPutFileCounter();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onFailurePutFile(e);
                resetPutFileCounter();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                onProgressPutFile(taskSnapshot);
            }
        });
    }

    @Override
    public void getFile(String path, final File file) {
        storageRef.child(path).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                if (Res.CURRENT_FILE_TYPE == FileType.beatmap) {
                    if (Res.CURRENT_BEATMAP_ID != null && putFileCounter == 0) {
                        getFileCounter++;
                        if (getFileCounter == 4) {
                            Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                Beatmap beatmap = new Beatmap(Res.CURRENT_BEATMAP_ID);
                                Res.BEATMAP_SELECTION_MENU_SCREEN.addScrollPaneItem(beatmap);
                                resetGetFileCounter();
                                Res.CURRENT_BEATMAP_ID = null;
                                Res.CURRENT_FILE_TYPE = FileType.unknown;
                                }
                            });
                            makeToast("The beatmap was downloaded.", Toast.LENGTH_SHORT);
                        }
                    }
                } else if (Res.CURRENT_FILE_TYPE == FileType.userIcon) {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            Res.USER.initIcon();
                            Res.CURRENT_FILE_TYPE = FileType.unknown;
                        }
                    });
                } else {
                    onSuccessfulGetFile();
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                if (Res.CURRENT_FILE_TYPE == FileType.userIcon) {
                    initUserDefaultIcon();
                } else {
                    onCanceledGetFile();
                    resetGetFileCounter();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (Res.CURRENT_FILE_TYPE == FileType.userIcon) {
                    initUserDefaultIcon();
                } else {
                    onFailureGetFile(e);
                    resetGetFileCounter();
                }
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {
                onProgressGetFile(taskSnapshot);
            }
        });
    }

    private void initUserDefaultIcon() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                Res.USER.initDefaultIcon();
                Res.CURRENT_FILE_TYPE = FileType.unknown;
            }
        });
    }

    @Override
    public void getFile(String path, final File file, final User friend, final float itemWidth, final float itemHeight, final float itemPadLeftRight, final float itemPadTopBottom) {
        storageRef.child(path).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        SideBar sideBar = Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
                        FriendScrollPaneItem friendScrollPaneItem = sideBar.initFriendScrollPaneItemWithIcon(friend, itemWidth, itemHeight);
                        sideBar.addFriendScrollPaneItem(friendScrollPaneItem, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom);
                    }
                });
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                initFriendScrollPaneItemWithDefaultIcon(friend, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                initFriendScrollPaneItemWithDefaultIcon(friend, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom);
            }
        }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull FileDownloadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    private void initFriendScrollPaneItemWithDefaultIcon(final User friend, final float itemWidth, final float itemHeight, final float itemPadLeftRight, final float itemPadTopBottom) {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                SideBar sideBar = Res.BEATMAP_SELECTION_MENU_SCREEN.getSideBar();
                FriendScrollPaneItem friendScrollPaneItem = sideBar.initFriendScrollPaneItemWithDefaultIcon(friend, itemWidth, itemHeight);
                sideBar.addFriendScrollPaneItem(friendScrollPaneItem, itemWidth, itemHeight, itemPadLeftRight, itemPadTopBottom);

            }
        });
    }

    @Override
    public void onSuccessfulGetFile() {
        Toast.makeText(context, "The file was downloaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCanceledGetFile() {
        Toast.makeText(context, "The file wasn't downloaded.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(context, "The file was uploaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCanceledPutFile() {
        Toast.makeText(context, "The file wasn't uploaded.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressPutFile(Object taskSnapshot) {
        UploadTask.TaskSnapshot _taskSnapshot = (UploadTask.TaskSnapshot) taskSnapshot;
    }

    @Override
    public void onFailurePutFile(Exception e) {
        System.err.println(e.toString());
    }

    private void makeToast(String text, int length) {
        Toast.makeText(context, text, length).show();
    }
}
