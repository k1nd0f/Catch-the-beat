package com.kindof.catchthebeat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.badlogic.gdx.Gdx;
import com.kindof.catchthebeat.tools.networkconnection.INetworkConnection;
import com.kindof.catchthebeat.tools.networkconnection.NetworkConnectionListener;
import com.kindof.catchthebeat.ui.actors.dialog.IDialogWindow;

public class AndroidNetworkConnection implements INetworkConnection, NetworkConnectionListener {
    private ConnectivityManager connectivityManager;
    private IDialogWindow dialogWindow;
    private boolean isConnected;

    public AndroidNetworkConnection(AndroidLauncher androidLauncher) {
        this.connectivityManager = (ConnectivityManager) androidLauncher.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.dialogWindow = androidLauncher.getAndroidDialogWindow();
    }

    private void update(Runnable runnable) {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && !networkInfo.isRoaming() && networkInfo.isConnected();
        if (isConnected) {
            if (dialogWindow.isAdded()) {
                dialogWindow.dismiss();
            }
            runnable.run();
        } else {
            onNetworkFailure();
        }
    }

    @Override
    public void networkAction(Runnable runnable) {
        do {
            update(runnable);
        } while (!isConnected);
    }

    @Override
    public void onNetworkFailure() {
        try {
            Thread.sleep(1000);
            if (!dialogWindow.isAdded()) {
                dialogWindow.set("Network Connection Error", "Waiting for connection", true, false, null, null, null);
                dialogWindow.show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
