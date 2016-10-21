package com.rksomayaji.work.orthoscores2.helper;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rksomayaji.work.orthoscores2.OrthoScores;

/**
 * Created by sushanth on 10/21/16.
 */

public class DownloadBroadcastReceiverHelper extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        int notID = intent.getIntExtra(OrthoScores.NOTIFICATION_ID,1);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = sPref.edit();

        switch (action){
            case OrthoScores.DOWNLOAD_UPDATE:
                Uri address = Uri.parse(intent.getStringExtra(OrthoScores.TAG_OR_URL));
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                long downloadID = manager.enqueue(new DownloadManager.Request(address));

                nm.cancel(notID);
                Log.i("BR", "Downloading " + String.valueOf(downloadID));
                break;
            case OrthoScores.IGNORE_UPDATE:
                String tag = intent.getStringExtra(OrthoScores.TAG_OR_URL);
                edit.putString(OrthoScores.IGNORE_TAG, tag);
                edit.apply();

                nm.cancel(notID);
                Log.i("BR", "Ignoring");
        }
    }
}
